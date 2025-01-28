package com.e_commerce.dto.order;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDTO {
    private Long id;
    private LocalDateTime orderDate;
    private AddressDTO address;
    private String status;
    private String paymentMethod;
    private Double totalPrice;
    private List<OrderItemDTO> orderItems;
}
