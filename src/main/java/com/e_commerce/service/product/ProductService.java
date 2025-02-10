package com.e_commerce.service.product;

import com.e_commerce.dto.product.ProductDTO;
import com.e_commerce.dto.product.ProductFilterDTO;
import com.e_commerce.dto.product.ProductPreviewDTO;
import com.e_commerce.dto.product.SizeVariantDTO;
import com.e_commerce.enums.Gender;
import com.e_commerce.request.ProductUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    ProductDTO createProduct(ProductDTO productDTO);

    List<ProductDTO> addProducts(List<ProductDTO> productDTOs);

    List<ProductDTO> getAllProducts();

    Page<ProductPreviewDTO> getFilteredProducts(List<Gender> gender,
                                                String category,
                                                String brand,
                                                String color,
                                                Double minPrice,
                                                Double maxPrice,
                                                String sortBy,
                                                String order,
                                                int page,
                                                int size);

    List<ProductPreviewDTO> getAllProductPreviews();

    ProductDTO updateProduct(ProductUpdateRequest productUpdateRequest, Long productId);

    ProductDTO addImagesToProduct(Long productId, List<String> imageUrls);

    ProductDTO addSizeVariantToProduct(SizeVariantDTO sizeVariantDTO, Long productId);

    ProductDTO updateStockForSizeVariant(Long sizeVariantId, Integer newStock);

    ProductDTO getProductById(Long productId);

    ProductDTO getProductByNameAndBrand(String name, String brand);

    void deleteProduct(Long productId);

    List<ProductDTO> getProductsByCategory(Long id);

    List<ProductPreviewDTO> getProductsByGender(Gender gender);

    ProductFilterDTO getProductFilters();
}
