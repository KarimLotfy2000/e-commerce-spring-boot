package com.e_commerce.service.category;

import com.e_commerce.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {

    List<CategoryDTO> getAllCategories();
    CategoryDTO getCategoryById(Long id);
    CategoryDTO getCategoryByName(String Name);
    CategoryDTO createCategory(CategoryDTO category);
    CategoryDTO updateCategory(CategoryDTO category, Long id);
    void deleteCategoryById(Long id);















}
