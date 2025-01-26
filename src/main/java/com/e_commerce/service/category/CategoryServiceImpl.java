package com.e_commerce.service.category;

import com.e_commerce.dto.product.CategoryDTO;
import com.e_commerce.entity.Category;
import com.e_commerce.exceptions.AlreadyExistsException;
import com.e_commerce.exceptions.ResourceNotFoundException;
import com.e_commerce.repository.CategoryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Service
@RequiredArgsConstructor

public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<CategoryDTO> getAllCategories() {
      List<Category> category = categoryRepository.findAll();
        return category.stream()
                .map(this::toCategoryDto)
                .collect(Collectors.toList());

    }

    @Override
    public CategoryDTO getCategoryById(Long id) {
     Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category with id " + id + " not found"));
        return toCategoryDto(category);
    }

    @Override
    public CategoryDTO getCategoryByName(String name) {
        Category category = categoryRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Category with name " + name + " not found"));

        return toCategoryDto(category);
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDto) {
        if(categoryRepository.existsByName(categoryDto.getName())) {
            throw new AlreadyExistsException("Category with name " + categoryDto.getName() + " already exists");
        }
        Category category = toCategory(categoryDto);
        categoryRepository.save(category);
        return toCategoryDto(category);
    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO categoryDTO, Long id) {
         Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category with id " + id + " not found"));
         existingCategory.setName(categoryDTO.getName());
         Category updatedCategory = categoryRepository.save(existingCategory);
         return toCategoryDto(updatedCategory);
    }


    @Override
    public void deleteCategoryById(Long id) {
        if(!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Category with id " + id + " not found");
        }
        categoryRepository.deleteById(id);
    }

    private CategoryDTO toCategoryDto(Category category) {
        return  modelMapper.map(category, CategoryDTO.class);
    }
    private Category toCategory(CategoryDTO categoryDTO) {
        return modelMapper.map(categoryDTO, Category.class);
    }
}
