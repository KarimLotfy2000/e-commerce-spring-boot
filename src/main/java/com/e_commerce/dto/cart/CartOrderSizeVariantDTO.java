package com.e_commerce.dto.cart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartOrderSizeVariantDTO {
    private Long id;
    private String size;
    private Double price;
     private ProductSummaryDTO product;
}
