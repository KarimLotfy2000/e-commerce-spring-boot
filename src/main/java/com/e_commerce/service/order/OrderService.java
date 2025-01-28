package com.e_commerce.service.order;

import com.e_commerce.dto.order.OrderDTO;
import com.e_commerce.request.OrderRequest;

import java.util.List;

public interface OrderService {
    OrderDTO createOrder(OrderRequest orderRequest);
    OrderDTO getOrderById(Long orderId);
    List<OrderDTO> getAllUserOrders();
    List<OrderDTO> getAllOrders();
    List<OrderDTO> getRecentUserOrders();
}
