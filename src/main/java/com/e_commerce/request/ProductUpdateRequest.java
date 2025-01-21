package com.e_commerce.request;

import com.e_commerce.dto.SizeVariantDTO;
import com.e_commerce.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductUpdateRequest {

    private String name;
    private String description;
    private String color;
    private String brand;
    private String material;
    private Double price;
    private Gender gender;
    private Long categoryId;
    private List<String> images;
    private List<SizeVariantDTO> sizeVariants;
}
