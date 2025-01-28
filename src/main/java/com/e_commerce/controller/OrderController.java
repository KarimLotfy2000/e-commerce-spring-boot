package com.e_commerce.controller;

import com.e_commerce.dto.order.OrderDTO;
import com.e_commerce.request.OrderRequest;
import com.e_commerce.response.ApiResponse;
import com.e_commerce.service.order.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/order")
public class OrderController {
    private final OrderService orderService;



    @PostMapping
    public ResponseEntity<ApiResponse<OrderDTO>> createOrder(@RequestBody @Valid OrderRequest orderRequest) {
        OrderDTO orderDTO = orderService.createOrder(orderRequest);
        return ResponseEntity.ok(new ApiResponse<>("Order created successfully", orderDTO));
    }

    //Accessible by owner or admin
    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderDTO>> getOrderById(@PathVariable Long orderId) {
        OrderDTO orderDTO = orderService.getOrderById(orderId);
        return ResponseEntity.ok(new ApiResponse<>("Order retrieved successfully", orderDTO));
    }

    // Get all orders for the current user
    @GetMapping("/my-orders")
    public ResponseEntity<ApiResponse<List<OrderDTO>>> getAllUserOrders() {
        List<OrderDTO> orders = orderService.getAllUserOrders();
        return ResponseEntity.ok(new ApiResponse<>("Your Orders retrieved successfully", orders));
    }

    @GetMapping("/recent-orders")
    public ResponseEntity<ApiResponse<List<OrderDTO>>> getRecentUserOrders() {
        List<OrderDTO> orders = orderService.getRecentUserOrders();
        return ResponseEntity.ok(new ApiResponse<>("Your Recent orders retrieved successfully", orders));
    }

    // Get all orders (Admin only)
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<OrderDTO>>> getAllOrders() {
        List<OrderDTO> allOrders = orderService.getAllOrders();
        return ResponseEntity.ok(new ApiResponse<>("All orders retrieved successfully", allOrders));
    }

}
