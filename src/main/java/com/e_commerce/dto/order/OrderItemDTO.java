package com.e_commerce.dto.order;

import com.e_commerce.dto.cart.CartOrderSizeVariantDTO;
import lombok.Data;

@Data
public class OrderItemDTO {
    private Long id;
    private Integer quantity;
    private Double subtotal;
    private CartOrderSizeVariantDTO sizeVariant;
}
