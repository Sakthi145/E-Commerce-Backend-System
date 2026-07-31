package com.ecommerce.backend.product_module;

import com.ecommerce.backend.category_module.CategoryRepository;
import com.ecommerce.backend.category_module.models.Category;
import com.ecommerce.backend.common.PageResponse;
import com.ecommerce.backend.exceptions.CategoryNotFoundException;
import com.ecommerce.backend.exceptions.ProductAlreadyExistsException;
import com.ecommerce.backend.exceptions.ProductNotFoundException;
import com.ecommerce.backend.product_module.dto.ProductRequest;
import com.ecommerce.backend.product_module.dto.ProductResponse;
import com.ecommerce.backend.product_module.models.Product;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.data.domain.Sort;


@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(
            ProductRepository productRepository,
            CategoryRepository categoryRepository
    ) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public ProductResponse createProduct(ProductRequest request) {

        Optional<Product> existingProduct =
                productRepository.findByName(request.getName());

        if (existingProduct.isPresent()) {
            throw new ProductAlreadyExistsException(
                    "Product already exists."
            );
        }

        Category category =
                categoryRepository.findById(request.getCategoryId())
                        .orElseThrow(() ->
                                new CategoryNotFoundException(
                                        "Category not found."
                                ));

        Product product = new Product();

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());
        product.setImageUrl(request.getImageUrl());
        product.setCategory(category);

        Product savedProduct = productRepository.save(product);

        return mapToResponse(savedProduct);
    }

    public List<ProductResponse> getAllProducts() {

        return productRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

    }

    public ProductResponse getProductById(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException(
                                "Product not found."
                        ));

        return mapToResponse(product);

    }

    public ProductResponse updateProduct(Long id, ProductRequest request) {

        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException("Product not found."));

        Optional<Product> productWithSameName =
                productRepository.findByName(request.getName());

        if (productWithSameName.isPresent()
                && !productWithSameName.get().getId().equals(id)) {

            throw new ProductAlreadyExistsException(
                    "Product already exists."
            );
        }

        Category category = categoryRepository.findById(
                        request.getCategoryId())
                .orElseThrow(() ->
                        new CategoryNotFoundException(
                                "Category not found."
                        ));

        existingProduct.setName(request.getName());
        existingProduct.setDescription(request.getDescription());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setStockQuantity(request.getStockQuantity());
        existingProduct.setImageUrl(request.getImageUrl());
        existingProduct.setCategory(category);

        Product updatedProduct =
                productRepository.save(existingProduct);

        return mapToResponse(updatedProduct);

    }

    public void deleteProduct(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException(
                                "Product not found."
                        ));

        productRepository.delete(product);

    }

    public List<ProductResponse> getProductsByCategory(Long categoryId) {

        categoryRepository.findById(categoryId)
                .orElseThrow(() ->
                        new CategoryNotFoundException(
                                "Category not found."
                        ));

        return productRepository.findByCategoryId(categoryId)
                .stream()
                .map(this::mapToResponse)
                .toList();

    }


    public PageResponse<ProductResponse> getProducts(
            int page,
            int size,
            String sortBy
    ) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(sortBy)
        );

        Page<Product> productPage =
                productRepository.findAll(pageable);

        List<ProductResponse> products =
                productPage.getContent()
                        .stream()
                        .map(this::mapToResponse)
                        .toList();

        return new PageResponse<>(
                products,
                productPage.getNumber(),
                productPage.getSize(),
                productPage.getTotalElements(),
                productPage.getTotalPages(),
                productPage.isFirst(),
                productPage.isLast()
        );
    }

    public PageResponse<ProductResponse> getProducts(
            int page,
            int size,
            String sortBy,
            String direction
    ) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(
                page,
                size,
                sort
        );

        Page<Product> productPage =
                productRepository.findAll(pageable);

        List<ProductResponse> products =
                productPage.getContent()
                        .stream()
                        .map(this::mapToResponse)
                        .toList();

        return new PageResponse<>(
                products,
                productPage.getNumber(),
                productPage.getSize(),
                productPage.getTotalElements(),
                productPage.getTotalPages(),
                productPage.isFirst(),
                productPage.isLast()
        );
    }

    public List<ProductResponse> searchProducts(String keyword) {

        return productRepository
                .findByNameContainingIgnoreCase(keyword)
                .stream()
                .map(this::mapToResponse)
                .toList();

    }

    private ProductResponse mapToResponse(Product product) {

        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getImageUrl(),
                product.getCategory().getName()
        );

    }

}