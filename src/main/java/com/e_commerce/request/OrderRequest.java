package com.e_commerce.request;

import com.e_commerce.dto.order.AddressDTO;
import com.e_commerce.entity.Order;
import lombok.Data;

@Data
public class OrderRequest {
    private Long savedAddressId;
    private AddressDTO newAddress;
    private String paymentMethod;
}
