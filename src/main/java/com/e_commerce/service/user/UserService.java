package com.e_commerce.service.user;

import com.e_commerce.dto.order.AddressDTO;

import java.util.List;

public interface UserService {

    AddressDTO addAddress(AddressDTO addressDTO);
    List<AddressDTO> showALlUserAddresses();
    AddressDTO updateAddress(AddressDTO addressDTO, Long addressId);
    void deleteAddress(Long addressId);


}
