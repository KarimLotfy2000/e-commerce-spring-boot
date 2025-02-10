package com.e_commerce.dto.product;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;


@Data
@AllArgsConstructor
public class ProductFilterDTO {
    private List<String> brands;
    private List<String> colors;
    private Double minPrice;
    private Double maxPrice;
}
