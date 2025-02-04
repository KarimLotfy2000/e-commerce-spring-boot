package com.e_commerce.dto.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductPreviewDTO {
    private Long id;
    private String name;
    private String brand;
    private String category;
    private String image;
    private Double price;
}
