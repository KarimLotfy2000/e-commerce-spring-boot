package com.e_commerce.dto.product;

import com.e_commerce.enums.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class ProductDTO {

    private Long id;

    @NotBlank(message = "Product name is required")
    private String name;

    private String description;
    private String color;

    @NotBlank(message = "Brand is required")
    private String brand;

    private String material;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be greater than zero")
    private Double price;

    @NotNull(message = "Gender is required")
    private Gender gender;

    @NotNull(message = "Category ID is required")
    private Long categoryId;
    private String categoryName;

    private List<String> images;

    @Size(max = 15, message = "A product can have up to 15 size variants")
    private List<SizeVariantDTO> sizeVariants;
}
