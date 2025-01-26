package com.e_commerce.dto.product;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryDTO {
    private Long id;
    @NotBlank
    private String name;
}
