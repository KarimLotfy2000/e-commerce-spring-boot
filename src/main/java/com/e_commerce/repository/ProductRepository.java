package com.e_commerce.repository;

import com.e_commerce.dto.ProductDTO;
import com.e_commerce.entity.Category;
import com.e_commerce.entity.Product;
import com.e_commerce.enums.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByNameAndBrand(String name, String brand);
    List<Product> findByCategory(Category category);

    List<Product> findByGenderIn(List<Gender> genders);
}
