package com.e_commerce.controller;

import com.e_commerce.dto.product.ProductDTO;
import com.e_commerce.dto.product.ProductFilterDTO;
import com.e_commerce.dto.product.ProductPreviewDTO;
import com.e_commerce.dto.product.SizeVariantDTO;
import com.e_commerce.enums.Gender;
import com.e_commerce.response.ApiResponse;
import com.e_commerce.service.product.ProductService;
import com.e_commerce.request.ProductUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    //  Create a Product
    @PostMapping
    public ResponseEntity<ApiResponse<ProductDTO>> createProduct(@RequestBody @Valid ProductDTO productDTO) {
        ProductDTO createdProduct = productService.createProduct(productDTO);
        return ResponseEntity.ok(new ApiResponse<>("Product created successfully", createdProduct));
    }

    // Multiple Products
    @PostMapping("/bulk")
    public ResponseEntity<ApiResponse<List<ProductDTO>>> addProducts(@RequestBody @Valid List<ProductDTO> productDTOs) {
        List<ProductDTO> createdProducts = productService.addProducts(productDTOs);
        return ResponseEntity.ok(new ApiResponse<>("Products created successfully", createdProducts));
    }

    //  Get Product by ID
    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductDTO>> getProductById(@PathVariable Long productId) {
        ProductDTO product = productService.getProductById(productId);
        return ResponseEntity.ok(new ApiResponse<>("Product retrieved successfully", product));
    }

    //  Get All Products
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductDTO>>> getAllProducts() {
        List<ProductDTO> products = productService.getAllProducts();
        return ResponseEntity.ok(new ApiResponse<>("Products retrieved successfully", products));
    }
    // Filter Products
    @GetMapping("/filter")
    public ResponseEntity<Page<ProductPreviewDTO>> getFilteredProducts(
            @RequestParam(required = false) List<Gender> gender,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String color,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String order,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<ProductPreviewDTO> products = productService.getFilteredProducts(
                gender, category, brand, color, minPrice, maxPrice, sortBy, order, page, size);

        return ResponseEntity.ok(products);
    }

    // Get All Product Previews
    @GetMapping("/previews")
    public ResponseEntity<ApiResponse<List<ProductPreviewDTO>>> getAllProductPreviews() {
        List<ProductPreviewDTO> productPreviews = productService.getAllProductPreviews();
        return ResponseEntity.ok(new ApiResponse<>("Products retrieved successfully", productPreviews));
    }

    // Update a Product
    @PutMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductDTO>> updateProduct(
            @PathVariable Long productId,
            @RequestBody @Valid ProductUpdateRequest productUpdateRequest) {
        ProductDTO updatedProduct = productService.updateProduct(productUpdateRequest, productId);
        return ResponseEntity.ok(new ApiResponse<>("Product updated successfully", updatedProduct));
    }

    //  Add Images to a Product
    @PutMapping("/{productId}/images")
    public ResponseEntity<ApiResponse<ProductDTO>> addImagesToProduct(
            @PathVariable Long productId,
            @RequestBody List<String> imageUrls) {
        ProductDTO updatedProduct = productService.addImagesToProduct(productId, imageUrls);
        return ResponseEntity.ok(new ApiResponse<>("Images added successfully", updatedProduct));
    }

    //  Add a Size Variant to a Product
    @PostMapping("/{productId}/size-variant")
    public ResponseEntity<ApiResponse<ProductDTO>> addSizeVariantToProduct(
            @PathVariable Long productId,
            @RequestBody @Valid SizeVariantDTO sizeVariantDTO) {
        ProductDTO updatedProduct = productService.addSizeVariantToProduct(sizeVariantDTO, productId);
        return ResponseEntity.ok(new ApiResponse<>("Size variant added successfully", updatedProduct));
    }

    //  Update Stock for a Size Variant
    @PatchMapping("/size-variants/{sizeVariantId}/stock")
    public ResponseEntity<ApiResponse<ProductDTO>> updateStockForSizeVariant(
            @PathVariable Long sizeVariantId,
            @RequestBody Integer newStock) {
        ProductDTO updatedProduct = productService.updateStockForSizeVariant(sizeVariantId, newStock);
        return ResponseEntity.ok(new ApiResponse<>("Stock updated successfully", updatedProduct));
    }



    //  Get Product by Name and Brand
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<ProductDTO>> getProductByNameAndBrand(
            @RequestParam String name,
            @RequestParam String brand) {
        ProductDTO product = productService.getProductByNameAndBrand(name, brand);

        return ResponseEntity.ok(new ApiResponse<>("Product retrieved successfully", product));
    }

    //  Delete a Product
    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.ok(new ApiResponse<>("Product deleted successfully", null));
    }

    //  Get Products by Category
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<List<ProductDTO>>> getProductsByCategory(@PathVariable Long categoryId) {
        List<ProductDTO> products = productService.getProductsByCategory(categoryId);
        return ResponseEntity.ok(new ApiResponse<>("Products by category retrieved successfully", products));
    }

    //  Get Products by Gender
    @GetMapping("/gender")
    public ResponseEntity<ApiResponse<List<ProductPreviewDTO>>> getProductsByGender(@RequestParam Gender gender) {
        List<ProductPreviewDTO> products = productService.getProductsByGender(gender);
        return ResponseEntity.ok(new ApiResponse<>("Products by gender retrieved successfully", products));
    }


    // Get ALl Filters
    @GetMapping("/filters")
    public ResponseEntity<ApiResponse<ProductFilterDTO>> getProductFilters() {
        ProductFilterDTO filterData = productService.getProductFilters();
        return ResponseEntity.ok(new ApiResponse<>("Product filters retrieved successfully", filterData));
    }


}
