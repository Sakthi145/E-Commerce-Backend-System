package com.ecommerce.backend.category_module;

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

    public Category createCategory(Category category) {

        Optional<Category> existingCategory = categoryRepository.findByName(category.getName());

        if (existingCategory.isPresent()) {
            throw new CategoryAlreadyExistsException("Category already exists.");
        }

        return categoryRepository.save(category);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found."));
    }

    public Category updateCategory(Long id, Category category) {

        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found."));

        Optional<Category> categoryWithSameName = categoryRepository.findByName(category.getName());

        if (categoryWithSameName.isPresent()
                && !categoryWithSameName.get().getId().equals(id)) {

            throw new CategoryAlreadyExistsException("Category already exists.");
        }

        existingCategory.setName(category.getName());

        return categoryRepository.save(existingCategory);
    }

    public void deleteCategoryById(Long id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new CategoryNotFoundException(
                                "Category not found."));

        categoryRepository.delete(category);
    }




}
