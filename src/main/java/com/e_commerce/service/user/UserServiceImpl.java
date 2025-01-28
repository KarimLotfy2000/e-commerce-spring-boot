package com.e_commerce.service.user;

import com.e_commerce.dto.order.AddressDTO;
import com.e_commerce.entity.Address;
import com.e_commerce.entity.User;
import com.e_commerce.exceptions.ResourceNotFoundException;
import com.e_commerce.repository.AddressRepository;
import com.e_commerce.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final ModelMapper modelMapper;
    private final AddressRepository addressRepository;
    private final JwtUtils jwtUtils;

    @Override
    public AddressDTO addAddress(AddressDTO addressDTO) {
        User user = jwtUtils.getCurrentUser();
        Address address = toAddress(addressDTO);
        address.setUser(user);
        Address savedAddress = addressRepository.save(address);
        return toAddressDTO(savedAddress);
    }

    @Override
    public List<AddressDTO> showALlUserAddresses() {
        User user = jwtUtils.getCurrentUser();
        List<Address> addresses = addressRepository.findByUserId(user.getId());
        if (addresses.isEmpty()) {
            throw new ResourceNotFoundException("No addresses added");
        }
        return addresses.stream()
                .map(this::toAddressDTO)
                .toList();

    }

    @Override
    public AddressDTO updateAddress(AddressDTO addressDTO, Long addressId) {
        User user = jwtUtils.getCurrentUser();
        Address address = addressRepository.findById(addressId).
                orElseThrow(() -> new ResourceNotFoundException("Address not found"));
        if(!address.getUser().getId().equals(user.getId())){
            throw new ResourceNotFoundException("Address not found");
        }
        address.setCity(addressDTO.getCity());
        address.setCountry(addressDTO.getCountry());
        address.setStreet(addressDTO.getStreet());
        address.setZipCode(addressDTO.getZipCode());
        Address updatedAddress = addressRepository.save(address);
        return toAddressDTO(updatedAddress);
     }

    @Override
    public void deleteAddress(Long addressId) {
        User user = jwtUtils.getCurrentUser();
        Address address = addressRepository.findById(addressId).
                orElseThrow(() -> new ResourceNotFoundException("Address not found"));
        if(!address.getUser().getId().equals(user.getId())){
            throw new ResourceNotFoundException("Address not found");
        }
        addressRepository.delete(address);
    }


    private Address toAddress(AddressDTO addressDTO){
        return modelMapper.map(addressDTO, Address.class);
    }
    private AddressDTO toAddressDTO(Address address){
        return modelMapper.map(address, AddressDTO.class);
    }
}
