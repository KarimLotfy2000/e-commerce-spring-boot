package com.e_commerce.controller;

import com.e_commerce.dto.order.AddressDTO;
import com.e_commerce.response.ApiResponse;
import com.e_commerce.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/addresses")
    public ResponseEntity<ApiResponse<AddressDTO>> addAddress(@RequestBody AddressDTO addressDTO) {
        AddressDTO createdAddress = userService.addAddress(addressDTO);
        return ResponseEntity.ok(new ApiResponse<>("Address added successfully", createdAddress));
    }

    @GetMapping("/addresses")
    public ResponseEntity<ApiResponse<List<AddressDTO>>> showAllUserAddresses() {
        List<AddressDTO> addresses = userService.showALlUserAddresses();
        return ResponseEntity.ok(new ApiResponse<>("Addresses retrieved successfully", addresses));
    }

    @PutMapping("/addresses/{addressId}")
    public ResponseEntity<ApiResponse<AddressDTO>> updateAddress(@RequestBody AddressDTO addressDTO, @PathVariable Long addressId) {
        AddressDTO updatedAddress = userService.updateAddress(addressDTO, addressId);
        return ResponseEntity.ok(new ApiResponse<>("Address updated successfully", updatedAddress));
    }

    @DeleteMapping("/addresses/{addressId}")
    public ResponseEntity<ApiResponse<Void>> deleteAddress(@PathVariable Long addressId) {
        userService.deleteAddress(addressId);
        return ResponseEntity.ok(new ApiResponse<>("Address deleted successfully", null));
    }
}
