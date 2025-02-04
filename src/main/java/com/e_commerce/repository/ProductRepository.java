package com.e_commerce.repository;

import com.e_commerce.dto.product.ProductPreviewDTO;
import com.e_commerce.entity.Category;
import com.e_commerce.entity.Product;
import com.e_commerce.enums.Gender;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
 import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("""
    SELECT new com.e_commerce.dto.product.ProductPreviewDTO(
        p.id, p.name, p.brand, p.category.name,
        (SELECT i FROM p.images i ORDER BY i LIMIT 1), p.price
    )
    FROM Product p
    WHERE (:category IS NULL OR p.category.name = :category)
      AND (:brand IS NULL OR p.brand = :brand)
      AND (:minPrice IS NULL OR p.price >= :minPrice)
      AND (:maxPrice IS NULL OR p.price <= :maxPrice)
      AND (
            :gender IS NULL\s
            OR p.gender IN :gender
            OR p.gender = 'UNISEX'
      )
""")
    Page<ProductPreviewDTO> findFilteredProducts(
            List<Gender> gender,
            String category,  // Exact match with category name
            String brand,  // Exact match with brand name
            Double minPrice,
            Double maxPrice,
            Pageable pageable
    );



    Optional<Product> findByNameAndBrand(String name, String brand);
    List<Product> findByCategory(Category category);

    @Query("SELECT new com.e_commerce.dto.product.ProductPreviewDTO(" +
            "p.id, p.name, p.brand, p.category.name, " +
            "(SELECT i FROM p.images i ORDER BY i ASC LIMIT 1), p.price) " +
            "FROM Product p " +
            "WHERE p.gender IN :genders")
    List<ProductPreviewDTO> findProductPreviewsByGender(@Param("genders") List<Gender> genders);

    @Query("SELECT DISTINCT p.brand FROM Product p")
    List<String> findAllBrands();

    @Query("SELECT new com.e_commerce.dto.product.ProductPreviewDTO(p.id, p.name, p.brand, p.category.name, " +
            "(SELECT i FROM p.images i ORDER BY i LIMIT 1), p.price) FROM Product p")
    List<ProductPreviewDTO> findAllProductPreviews();

}
