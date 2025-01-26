package com.e_commerce.repository;

import com.e_commerce.entity.Cart;
import com.e_commerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
}