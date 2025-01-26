package com.e_commerce.repository;

import com.e_commerce.entity.Category;
import com.e_commerce.entity.Product;
import com.e_commerce.enums.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByNameAndBrand(String name, String brand);
    List<Product> findByCategory(Category category);

    List<Product> findByGenderIn(List<Gender> genders);
}
