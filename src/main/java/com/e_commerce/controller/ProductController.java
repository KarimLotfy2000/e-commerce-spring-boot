package com.e_commerce.controller;

import com.e_commerce.dto.product.ProductDTO;
import com.e_commerce.dto.product.SizeVariantDTO;
import com.e_commerce.enums.Gender;
import com.e_commerce.response.ApiResponse;
import com.e_commerce.service.product.ProductService;
import com.e_commerce.request.ProductUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // 1. Create a Product
    @PostMapping
    public ResponseEntity<ApiResponse<ProductDTO>> createProduct(@RequestBody @Valid ProductDTO productDTO) {
        ProductDTO createdProduct = productService.createProduct(productDTO);
        return ResponseEntity.ok(new ApiResponse<>("Product created successfully", createdProduct));
    }

    // 2. Add Multiple Products
    @PostMapping("/bulk")
    public ResponseEntity<ApiResponse<List<ProductDTO>>> addProducts(@RequestBody @Valid List<ProductDTO> productDTOs) {
        List<ProductDTO> createdProducts = productService.addProducts(productDTOs);
        return ResponseEntity.ok(new ApiResponse<>("Products created successfully", createdProducts));
    }

    // 3. Get All Products
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductDTO>>> getAllProducts() {
        List<ProductDTO> products = productService.getAllProducts();
        return ResponseEntity.ok(new ApiResponse<>("Products retrieved successfully", products));
    }

    // 4. Update a Product
    @PutMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductDTO>> updateProduct(
            @PathVariable Long productId,
            @RequestBody @Valid ProductUpdateRequest productUpdateRequest) {
        ProductDTO updatedProduct = productService.updateProduct(productUpdateRequest, productId);
        return ResponseEntity.ok(new ApiResponse<>("Product updated successfully", updatedProduct));
    }

    // 5. Add Images to a Product
    @PutMapping("/{productId}/images")
    public ResponseEntity<ApiResponse<ProductDTO>> addImagesToProduct(
            @PathVariable Long productId,
            @RequestBody List<String> imageUrls) {
        ProductDTO updatedProduct = productService.addImagesToProduct(productId, imageUrls);
        return ResponseEntity.ok(new ApiResponse<>("Images added successfully", updatedProduct));
    }

    // 6. Add a Size Variant to a Product
    @PostMapping("/{productId}/size-variant")
    public ResponseEntity<ApiResponse<ProductDTO>> addSizeVariantToProduct(
            @PathVariable Long productId,
            @RequestBody @Valid SizeVariantDTO sizeVariantDTO) {
        ProductDTO updatedProduct = productService.addSizeVariantToProduct(sizeVariantDTO, productId);
        return ResponseEntity.ok(new ApiResponse<>("Size variant added successfully", updatedProduct));
    }

    // 7. Update Stock for a Size Variant
    @PatchMapping("/size-variants/{sizeVariantId}/stock")
    public ResponseEntity<ApiResponse<ProductDTO>> updateStockForSizeVariant(
            @PathVariable Long sizeVariantId,
            @RequestBody Integer newStock) {
        ProductDTO updatedProduct = productService.updateStockForSizeVariant(sizeVariantId, newStock);
        return ResponseEntity.ok(new ApiResponse<>("Stock updated successfully", updatedProduct));
    }

    // 8. Get Product by ID
    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductDTO>> getProductById(@PathVariable Long productId) {
        ProductDTO product = productService.getProductById(productId);
        return ResponseEntity.ok(new ApiResponse<>("Product retrieved successfully", product));
    }

    // 9. Get Product by Name and Brand
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<ProductDTO>> getProductByNameAndBrand(
            @RequestParam String name,
            @RequestParam String brand) {
        ProductDTO product = productService.getProductByNameAndBrand(name, brand);

        return ResponseEntity.ok(new ApiResponse<>("Product retrieved successfully", product));
    }

    // 10. Delete a Product
    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.ok(new ApiResponse<>("Product deleted successfully", null));
    }

    // 11. Get Products by Category
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<List<ProductDTO>>> getProductsByCategory(@PathVariable Long categoryId) {
        List<ProductDTO> products = productService.getProductsByCategory(categoryId);
        return ResponseEntity.ok(new ApiResponse<>("Products by category retrieved successfully", products));
    }

    // 12. Get Products by Gender
    @GetMapping("/gender")
    public ResponseEntity<ApiResponse<List<ProductDTO>>> getProductsByGender(@RequestParam Gender gender) {
        List<ProductDTO> products = productService.getProductsByGender(gender);
        return ResponseEntity.ok(new ApiResponse<>("Products by gender retrieved successfully", products));
    }

    //13. Get ALl Brands
    @GetMapping("/brands")
    public ResponseEntity<ApiResponse<List<String>>> getAllBrands() {
        List<String> brands = productService.getAllBrands();
        return ResponseEntity.ok(new ApiResponse<>("Brands retrieved successfully", brands));
    }
    // CHANGE RESPONSES TO INCLUDE ONLY THE NEEDED FIELDS FOR THE FRONTEND TO DISPLAY THE PRODUCTS
    // ADD  "ADD TO LIKES" FEATURE
    // ADD "REVIEW" FEATURE
    // ADD FILTERING, SORTING AND  PAGINATION TO THE GET ALL PRODUCTS ENDPOINT
}
