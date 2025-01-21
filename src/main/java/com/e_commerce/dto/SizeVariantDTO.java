package com.e_commerce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class SizeVariantDTO {

    @NotBlank(message = "Size is required")
    private String size;

    @NotNull(message = "Stock is required")
    @PositiveOrZero(message = "Stock must be zero or greater")
    private Integer stock;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be greater than zero")
    private Double price;
}