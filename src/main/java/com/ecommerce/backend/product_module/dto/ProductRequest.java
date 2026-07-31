package com.ecommerce.backend.product_module.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;


public class ProductRequest {

    @NotBlank(message = "Product name cannot be blank.")
    private String name;

    @NotBlank(message = "Description cannot be blank.")
    private String description;

    @NotNull(message = "Price is required.")
    @Positive(message = "Price must be greater than zero.")
    private BigDecimal price;

    @NotNull(message = "Stock quantity is required.")
    @PositiveOrZero(message = "Stock cannot be negative.")
    private Integer stockQuantity;

    @NotBlank(message = "Image URL cannot be blank.")
    private String imageUrl;

    @NotNull(message = "Category ID is required.")
    private Long categoryId;

    public ProductRequest() {
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}