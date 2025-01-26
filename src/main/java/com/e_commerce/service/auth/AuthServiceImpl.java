package com.e_commerce.service.auth;

import com.e_commerce.dto.UserDTO;
import com.e_commerce.entity.Role;
import com.e_commerce.entity.User;
import com.e_commerce.entity.UserRole;
import com.e_commerce.enums.RoleName;
import com.e_commerce.repository.RoleRepository;
import com.e_commerce.repository.UserRepository;
import com.e_commerce.repository.UserRoleRepository;
import com.e_commerce.request.LoginRequest;
import com.e_commerce.response.LoginResponse;
import com.e_commerce.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final ModelMapper modelMapper;
    @Override
    public void registerUser(UserDTO userDTO) {
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new RuntimeException("This email is already taken");
        }
         User user = modelMapper.map(userDTO, User.class);
         user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
         Role userRole = roleRepository.findByName(RoleName.USER)
                .orElseThrow(() -> new RuntimeException("Role User not found"));
        UserRole userRoleEntity = new UserRole();
        userRoleEntity.setUser(user);
        userRoleEntity.setRole(userRole);
        user.setUserRoles(Set.of(userRoleEntity));
        userRepository.save(user);
        userRoleRepository.save(userRoleEntity);
    }


    @Override
    public LoginResponse loginUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        User user = (User) authentication.getPrincipal();
        String token = jwtUtils.generateToken(user);
        Set<String> roles = user.getUserRoles().stream()
                .map(userRole -> userRole.getRole().getName().name())
                .collect(Collectors.toSet());
        return new LoginResponse(
                token,
                user.getEmail(),
                user.getName()
                ,roles
        );
    }
}
