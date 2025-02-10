package com.e_commerce.service.order;

import com.e_commerce.dto.order.AddressDTO;
import com.e_commerce.dto.order.OrderDTO;
import com.e_commerce.dto.order.OrderItemDTO;
import com.e_commerce.entity.*;
import com.e_commerce.enums.PaymentMethod;
import com.e_commerce.enums.RoleName;
import com.e_commerce.enums.Status;
import com.e_commerce.exceptions.ResourceNotFoundException;
import com.e_commerce.repository.AddressRepository;
import com.e_commerce.repository.CartRepository;
import com.e_commerce.repository.OrderRepository;
import com.e_commerce.repository.SizeVariantRepository;
import com.e_commerce.request.OrderRequest;
import com.e_commerce.security.JwtUtils;
import com.e_commerce.service.cart.CartService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
 import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final SizeVariantRepository sizeVariantRepository;
    private final AddressRepository addressRepository;
    private final CartService cartService;
    private final JwtUtils jwtUtils;

    @Override
    @Transactional
    public OrderDTO createOrder(OrderRequest orderRequest) {
        User user = jwtUtils.getCurrentUser();

        Cart cart = Optional.ofNullable(user.getCart())
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user: " + user.getEmail()));

        if (cart.getCartItems().isEmpty()) {
            throw new IllegalArgumentException("Cart is empty, cannot create order");
        }

        Order.AddressDetails addressDetails;

        if (orderRequest.getSavedAddressId() != null) {
            Address savedAddress = addressRepository.findById(orderRequest.getSavedAddressId())
                    .orElseThrow(() -> new ResourceNotFoundException("Saved Address not found"));

            if (!savedAddress.getUser().getId().equals(user.getId())) {
                throw new IllegalArgumentException("Address does not belong to the current user");
            }

            addressDetails = new Order.AddressDetails(
                    savedAddress.getStreet(),
                    savedAddress.getCity(),
                    savedAddress.getCountry(),
                    savedAddress.getZipCode()
            );
        } else if (orderRequest.getNewAddress() != null) {
            addressDetails = new Order.AddressDetails(
                    orderRequest.getNewAddress().getStreet(),
                    orderRequest.getNewAddress().getCity(),
                    orderRequest.getNewAddress().getCountry(),
                    orderRequest.getNewAddress().getZipCode()
            );
        } else {
            throw new IllegalArgumentException("Address not provided");
        }

        // Create the order
        Order order = new Order();
        order.setUser(user);
        order.setAddress(addressDetails);
        order.setPaymentMethod(PaymentMethod.valueOf(orderRequest.getPaymentMethod()));
        order.setStatus(Status.PENDING);
        order.setOrderDate(LocalDateTime.now());
        order.setTotalPrice(cart.getTotalPrice() + 5.0); // Shipping cost simulation

        // Create order items from the cart items
        cart.getCartItems().forEach(cartItem -> {
            SizeVariant sizeVariant = cartItem.getSizeVariant();

            if (sizeVariant.getStock() < cartItem.getQuantity()) {
                throw new RuntimeException("Insufficient stock for size variant: " + sizeVariant.getSize());
            }

            sizeVariant.setStock(sizeVariant.getStock() - cartItem.getQuantity());
            sizeVariantRepository.save(sizeVariant);

            // Create the order item
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setSizeVariant(cartItem.getSizeVariant());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setSubtotal(cartItem.getSubtotal());
            order.getOrderItems().add(orderItem);
        });

        Order savedOrder = orderRepository.save(order);

        cart.getCartItems().clear();
        cart.setTotalPrice(0.0);
        cart.setTotalItems(0);
        cartRepository.save(cart);

        return toOrderDTO(savedOrder);
    }


    @Override
    public OrderDTO getOrderById(Long orderId) {
        User user = jwtUtils.getCurrentUser();
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        if (!order.getUser().getId().equals(user.getId()) && !isAdmin(user)) {
            throw new RuntimeException("Access denied to this order");
        }
        return toOrderDTO(order);
    }

    private boolean isAdmin(User user) {
        return user.getUserRoles().stream()
                .anyMatch(role -> role.getRole().getName().equals(RoleName.ROLE_ADMIN));
    }



    @Override
    public List<OrderDTO> getAllUserOrders() {
        User user = jwtUtils.getCurrentUser();
        List<Order> orders = orderRepository.findAllByUserId(user.getId());
        return orders.stream()
                .map(this::toOrderDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::toOrderDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDTO> getRecentUserOrders() {
        return orderRepository.findTop5ByUserIdOrderByOrderDateDesc(jwtUtils.getCurrentUser().getId())
                .stream()
                .map(this::toOrderDTO)
                .collect(Collectors.toList());
    }

    // Convert Order to OrderDTO
    private OrderDTO toOrderDTO(Order order) {
        OrderDTO orderDTO = new OrderDTO();

        orderDTO.setId(order.getId());
        orderDTO.setOrderDate(order.getOrderDate());
        orderDTO.setTotalPrice(order.getTotalPrice());
        orderDTO.setPaymentMethod(order.getPaymentMethod().name());
        orderDTO.setStatus(order.getStatus().name());

        Order.AddressDetails address = order.getAddress();
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setStreet(address.getStreet());
        addressDTO.setCity(address.getCity());
        addressDTO.setCountry(address.getCountry());
        addressDTO.setZipCode(address.getZipCode());

        orderDTO.setAddress(addressDTO);

        orderDTO.setOrderItems(order.getOrderItems().stream().map(
                orderItem -> {
                    OrderItemDTO orderItemDTO = new OrderItemDTO();
                    orderItemDTO.setId(orderItem.getId());
                    orderItemDTO.setQuantity(orderItem.getQuantity());
                    orderItemDTO.setSubtotal(orderItem.getSubtotal());
                    orderItemDTO.setSizeVariant(cartService.toCartOrderSizeVariantDTO(orderItem.getSizeVariant()));
                    return orderItemDTO;
                }).toList());

          return orderDTO;
    }


}
