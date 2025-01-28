package com.e_commerce.service.product;

import com.e_commerce.dto.product.ProductDTO;
import com.e_commerce.dto.product.SizeVariantDTO;
import com.e_commerce.entity.Category;
import com.e_commerce.entity.Product;
import com.e_commerce.entity.SizeVariant;
import com.e_commerce.enums.Gender;
import com.e_commerce.exceptions.ResourceNotFoundException;
import com.e_commerce.repository.CategoryRepository;
import com.e_commerce.repository.ProductRepository;
import com.e_commerce.repository.SizeVariantRepository;
import com.e_commerce.request.ProductUpdateRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements  ProductService{

    private final ProductRepository productRepository;
    private final SizeVariantRepository sizeVariantRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public ProductDTO createProduct(ProductDTO productDTO) {
        Category category = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        Product product = toProduct(productDTO);
        product.setCategory(category);
        if (product.getSizeVariants() != null) {
            product.getSizeVariants().forEach(variant -> variant.setProduct(product));
        }
        Product savedProduct = productRepository.save(product);
        return toProductDto(savedProduct);
    }

    @Override
    @Transactional
    public List<ProductDTO> addProducts(List<ProductDTO> productDTOs) {
        List<Product> products  =  productDTOs.stream()
                .map(productDTO -> {
                    Category category = categoryRepository.findById(productDTO.getCategoryId())
                            .orElseThrow(() -> new ResourceNotFoundException("Category with id " + productDTO.getCategoryId() + " not found"));
                    Product product = toProduct(productDTO);
                    product.setCategory(category);
                    if (product.getSizeVariants() != null) {
                        product.getSizeVariants().forEach(variant -> variant.setProduct(product));
                    }
                    return product;
                }).toList();
        List<Product> savedProducts = productRepository.saveAll(products);
        return savedProducts.stream()
                .map(this::toProductDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(this::toProductDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProductDTO updateProduct(ProductUpdateRequest productUpdateRequest, Long productId) {
         Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + productId + " not found"));

        Optional.ofNullable(productUpdateRequest.getName()).ifPresent(existingProduct::setName);
        Optional.ofNullable(productUpdateRequest.getDescription()).ifPresent(existingProduct::setDescription);
        Optional.ofNullable(productUpdateRequest.getColor()).ifPresent(existingProduct::setColor);
        Optional.ofNullable(productUpdateRequest.getBrand()).ifPresent(existingProduct::setBrand);
        Optional.ofNullable(productUpdateRequest.getMaterial()).ifPresent(existingProduct::setMaterial);
        Optional.ofNullable(productUpdateRequest.getPrice()).ifPresent(existingProduct::setPrice);
        Optional.ofNullable(productUpdateRequest.getGender()).ifPresent(existingProduct::setGender);

        if (productUpdateRequest.getCategoryId() != null) {
            Category newCategory = categoryRepository.findById(productUpdateRequest.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category with id " + productUpdateRequest.getCategoryId() + " not found"));
            existingProduct.setCategory(newCategory);
        }

        if (productUpdateRequest.getImages() != null) {
            existingProduct.setImages(productUpdateRequest.getImages());
        }

        if (productUpdateRequest.getSizeVariants() != null) {
            existingProduct.getSizeVariants().clear();
            productUpdateRequest.getSizeVariants().forEach(variant -> {
                SizeVariant sizeVariant = modelMapper.map(variant, SizeVariant.class);
                sizeVariant.setProduct(existingProduct);
                existingProduct.getSizeVariants().add(sizeVariant);
            });
        }
        Product updatedProduct = productRepository.save(existingProduct);
        return toProductDto(updatedProduct);
    }



    @Override
     public ProductDTO addImagesToProduct(Long productId, List<String> imageUrls) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + productId + " not found"));
        if (existingProduct.getImages() != null) {
            existingProduct.getImages().addAll(imageUrls);
        } else {
            existingProduct.setImages(imageUrls);
        }
       Product updatedProductWithImages = productRepository.save(existingProduct);
        return toProductDto(updatedProductWithImages);
    }

    @Override
    @Transactional
    public ProductDTO addSizeVariantToProduct(SizeVariantDTO sizeVariantDTO, Long productId) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + productId + " not found"));
        SizeVariant sizeVariant = modelMapper.map(sizeVariantDTO, SizeVariant.class);
        sizeVariant.setProduct(existingProduct);
        SizeVariant savedSizeVariant = sizeVariantRepository.save(sizeVariant);
        existingProduct.getSizeVariants().add(savedSizeVariant);
        return toProductDto(existingProduct);
    }

    @Override
    public ProductDTO updateStockForSizeVariant(Long sizeVariantId, Integer newStock) {
         SizeVariant sizeVariant = sizeVariantRepository.findById(sizeVariantId)
                .orElseThrow(() -> new ResourceNotFoundException("SizeVariant with id " + sizeVariantId + " not found"));
         sizeVariant.setStock(newStock);
        sizeVariantRepository.save(sizeVariant);
        return modelMapper.map(sizeVariant.getProduct(), ProductDTO.class);
    }

    @Override
    public ProductDTO getProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + productId + " not found"));
        return toProductDto(product);
    }

    @Override
    public ProductDTO getProductByNameAndBrand(String name, String brand) {
        Product product = productRepository.findByNameAndBrand(name, brand)
                .orElseThrow(() -> new ResourceNotFoundException("Product with name " + name + " and brand " + brand + " not found"));
        return toProductDto(product);
    }

    @Override
    public void deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + productId + " not found"));
        productRepository.delete(product);
    }

    @Override
    public List<ProductDTO> getProductsByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category with id " + categoryId + " not found"));
        return productRepository.findByCategory(category)
                .stream()
                .map(this::toProductDto)
                .collect(Collectors.toList());
    }
    @Override
    public List<ProductDTO> getProductsByGender(Gender gender) {
        return productRepository.findByGenderIn(Arrays.asList(gender, Gender.UNISEX))
                .stream()
                .map(this::toProductDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getAllBrands() {
        return productRepository.findAllBrands();
    }


    private ProductDTO toProductDto(Product product){
        return modelMapper.map(product, ProductDTO.class);
    }
    private Product toProduct(ProductDTO productDTO){
        return modelMapper.map(productDTO, Product.class);
    }

}
