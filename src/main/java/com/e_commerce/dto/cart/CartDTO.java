package com.e_commerce.dto.cart;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CartDTO {
    private Long id;
    private Double totalPrice;
    private Integer totalItems;
    private List<CartItemDTO> cartItems;
}
