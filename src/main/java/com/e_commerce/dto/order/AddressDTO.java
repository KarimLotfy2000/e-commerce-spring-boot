package com.e_commerce.dto.order;

import lombok.Data;

@Data
public class AddressDTO {
    private Long id;
    private String street;
    private String city;
    private String country;
    private String zipCode;
}
