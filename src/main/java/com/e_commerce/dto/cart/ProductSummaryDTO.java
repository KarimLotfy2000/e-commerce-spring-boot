package com.e_commerce.dto.cart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductSummaryDTO {
    private Long id;
    private String brand;
    private String name;
    private String image;
}
