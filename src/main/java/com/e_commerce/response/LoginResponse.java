package com.e_commerce.response;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private  String email;
    private String name;
    private Set<String> role;
}
