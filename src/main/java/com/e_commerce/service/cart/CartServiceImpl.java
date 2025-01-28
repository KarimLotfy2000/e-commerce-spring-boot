package com.e_commerce.service.cart;

import com.e_commerce.dto.cart.CartDTO;
import com.e_commerce.dto.cart.CartItemDTO;
import com.e_commerce.dto.cart.CartOrderSizeVariantDTO;
import com.e_commerce.dto.cart.ProductSummaryDTO;
import com.e_commerce.entity.*;
import com.e_commerce.exceptions.ResourceNotFoundException;
import com.e_commerce.repository.CartRepository;
import com.e_commerce.repository.SizeVariantRepository;
import com.e_commerce.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final SizeVariantRepository sizeVariantRepository;
    private final JwtUtils jwtUtils;

    @Override
     public CartDTO getCartForCurrentUser() {
        Cart cart = getOrCreateCartForCurrentUser();
        return toCartDTO(cart);
    }

    @Override
    public CartDTO addItemToCart(Long sizeVariantId, Integer quantity) {
        validateQuantity(quantity);

        Cart cart = getOrCreateCartForCurrentUser();
        initializeCartItemsIfNeeded(cart);

        SizeVariant sizeVariant = sizeVariantRepository.findById(sizeVariantId)
                .orElseThrow(() -> new ResourceNotFoundException("Size Variant not found"));

        CartItem existingItem = cart.getCartItems().stream()
                .filter(item -> item.getSizeVariant().getId().equals(sizeVariant.getId()))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            log.info("Updating quantity of existing item: {}", existingItem);
            int newQuantity = existingItem.getQuantity() + quantity;
            existingItem.setQuantity(newQuantity);
            existingItem.setSubtotal(newQuantity * sizeVariant.getPrice());

        } else {
            log.info("Adding new item to cart: sizeVariantId={}, quantity={}", sizeVariantId, quantity);
            CartItem newCartItem = new CartItem();
            newCartItem.setCart(cart);
            newCartItem.setSizeVariant(sizeVariant);
            newCartItem.setQuantity(quantity);
            newCartItem.setSubtotal(quantity * sizeVariant.getPrice());
            cart.getCartItems().add(newCartItem);
        }

        updateCartTotalPrice(cart);
        return toCartDTO(cartRepository.save(cart));
    }

    @Override
    public CartDTO removeItemFromCart(Long cartItemId) {
        Cart cart = getOrCreateCartForCurrentUser();

        boolean removed = cart.getCartItems().removeIf(item -> item.getId().equals(cartItemId));
        if (!removed) {
            throw new ResourceNotFoundException("Cart Item with ID " + cartItemId + " not found");
        }

        updateCartTotalPrice(cart);
        cartRepository.save(cart);
        return toCartDTO(cart);
    }

    @Override
    public CartDTO updateItemQuantity(Long cartItemId, Integer quantity) {
        validateQuantity(quantity);

        Cart cart = getOrCreateCartForCurrentUser();
        CartItem cartItem = cart.getCartItems().stream()
                .filter(item -> item.getId().equals(cartItemId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Cart Item not found"));

        SizeVariant sizeVariant = cartItem.getSizeVariant();
        if (quantity > sizeVariant.getStock()) {
            throw new IllegalArgumentException("Insufficient stock for size variant: " + sizeVariant.getSize());
        }

        cartItem.setQuantity(quantity);
        cartItem.setSubtotal(quantity * sizeVariant.getPrice());
        updateCartTotalPrice(cart);
        cartRepository.save(cart);
        return toCartDTO(cart);
    }

    @Override
    public CartDTO clearCart() {
        Cart cart = getOrCreateCartForCurrentUser();
        cart.getCartItems().clear();
        cart.setTotalPrice(0.0);
        cartRepository.save(cart);
        return toCartDTO(cart);
    }

    @Override
    public void validateAndReduceStockForOrder() {
        Cart cart = getOrCreateCartForCurrentUser();
        if (cart.getCartItems().isEmpty()) {
            throw new IllegalStateException("Cart is empty");
        }

        for (CartItem item : cart.getCartItems()) {
            SizeVariant sizeVariant = item.getSizeVariant();
            if (sizeVariant.getStock() < item.getQuantity()) {
                throw new IllegalStateException(
                        "Insufficient stock for size variant: " + sizeVariant.getSize());
            }
            sizeVariant.setStock(sizeVariant.getStock() - item.getQuantity());
            sizeVariantRepository.save(sizeVariant);
        }
    }

    // Helper Methods
    private Cart getOrCreateCartForCurrentUser() {
        User user = jwtUtils.getCurrentUser();
        return Optional.ofNullable(user.getCart()).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            newCart.setCartItems(new ArrayList<>());
            user.setCart(newCart);
            return cartRepository.save(newCart);
        });
    }

    private void updateCartTotalPrice(Cart cart) {
        double totalPrice = cart.getCartItems() == null || cart.getCartItems().isEmpty()
                ? 0.0
                : cart.getCartItems().stream()
                .mapToDouble(item -> item.getSubtotal() != null ? item.getSubtotal() : 0.0)
                .sum();

        cart.setTotalPrice(totalPrice);
    }

    private void validateQuantity(Integer quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
    }

    private void initializeCartItemsIfNeeded(Cart cart) {
        if (cart.getCartItems() == null) {
            cart.setCartItems(new ArrayList<>());
        }
    }


    // Conversion Methods
    private CartDTO toCartDTO(Cart cart) {
        List<CartItemDTO> cartItemDTOs = cart.getCartItems().stream()
                .map(this::toCartItemDTO)
                .toList();
        return new CartDTO(cart.getId(), cart.getTotalPrice(), cartItemDTOs);
    }

    private CartItemDTO toCartItemDTO(CartItem cartItem) {
        CartOrderSizeVariantDTO sizeVariantDTO = toCartOrderSizeVariantDTO(cartItem.getSizeVariant());
        return new CartItemDTO(
                cartItem.getId(),
                cartItem.getQuantity(),
                cartItem.getSubtotal(),
                sizeVariantDTO
        );
    }
    @Override
    public CartOrderSizeVariantDTO toCartOrderSizeVariantDTO(SizeVariant sizeVariant) {
        Product product = sizeVariant.getProduct();

        ProductSummaryDTO productSummaryDTO = new ProductSummaryDTO();
        productSummaryDTO.setId(product.getId());
        productSummaryDTO.setBrand(product.getBrand());
        productSummaryDTO.setName(product.getName());
        productSummaryDTO.setImage(product.getImages().isEmpty() ? null : product.getImages().getFirst());

        CartOrderSizeVariantDTO dto = new CartOrderSizeVariantDTO();
        dto.setId(sizeVariant.getId());
        dto.setSize(sizeVariant.getSize());
        dto.setPrice(sizeVariant.getPrice());
        dto.setProduct(productSummaryDTO);

        return dto;
    }
}
