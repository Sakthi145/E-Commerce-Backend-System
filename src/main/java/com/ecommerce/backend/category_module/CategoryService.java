package com.ecommerce.backend.category_module;

import com.ecommerce.backend.category_module.dto.CategoryRequest;
import com.ecommerce.backend.category_module.dto.CategoryResponse;
import com.ecommerce.backend.exceptions.CategoryAlreadyExistsException;
import com.ecommerce.backend.category_module.models.Category;
import com.ecommerce.backend.exceptions.CategoryNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;


    }

    public CategoryResponse createCategory(CategoryRequest request) {

        Optional<Category> existingCategory =
                categoryRepository.findByName(request.getName());

        if (existingCategory.isPresent()) {
            throw new CategoryAlreadyExistsException(
                    "Category already exists.");
        }

        Category category = new Category();

        category.setName(request.getName());

        Category savedCategory =
                categoryRepository.save(category);

        return mapToResponse(savedCategory);
    }

    public List<CategoryResponse> getAllCategories() {

        return categoryRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();

    }

    public CategoryResponse getCategoryById(Long id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new CategoryNotFoundException(
                                "Category not found."));

        return mapToResponse(category);

    }

    public CategoryResponse updateCategory(
            Long id,
            CategoryRequest request) {

        Category existingCategory =
                categoryRepository.findById(id)
                        .orElseThrow(() ->
                                new CategoryNotFoundException(
                                        "Category not found."));

        Optional<Category> categoryWithSameName =
                categoryRepository.findByName(request.getName());

        if (categoryWithSameName.isPresent()
                && !categoryWithSameName.get().getId().equals(id)) {

            throw new CategoryAlreadyExistsException(
                    "Category already exists.");
        }

        existingCategory.setName(request.getName());

        Category updatedCategory =
                categoryRepository.save(existingCategory);

        return mapToResponse(updatedCategory);

    }

    public void deleteCategoryById(Long id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new CategoryNotFoundException(
                                "Category not found."));

        categoryRepository.delete(category);
    }

    private CategoryResponse mapToResponse(Category category) {

        return new CategoryResponse(
                category.getId(),
                category.getName()
        );

    }


}
