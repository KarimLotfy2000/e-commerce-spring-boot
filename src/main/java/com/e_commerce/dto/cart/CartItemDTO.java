package com.e_commerce.dto.cart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDTO {
    private Long id;
    private Integer quantity;
    private Double subtotal;
    private CartSizeVariantDTO sizeVariant;

}
