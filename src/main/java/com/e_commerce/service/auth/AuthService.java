package com.e_commerce.service.auth;

import com.e_commerce.dto.UserDTO;
import com.e_commerce.request.LoginRequest;
import com.e_commerce.response.LoginResponse;

public interface AuthService {
    void  registerUser(UserDTO userDTO);
    LoginResponse loginUser(LoginRequest loginRequest);
}
