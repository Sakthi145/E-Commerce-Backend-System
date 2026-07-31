package com.ecommerce.backend.product_module.dto;

import java.math.BigDecimal;

public class ProductResponse {

    private Long id;

    private String name;

    private String description;

    private BigDecimal price;

    private Integer stockQuantity;

    private String imageUrl;

    private String categoryName;

    public ProductResponse() {
    }

    public ProductResponse(
            Long id,
            String name,
            String description,
            BigDecimal price,
            Integer stockQuantity,
            String imageUrl,
            String categoryName
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.imageUrl = imageUrl;
        this.categoryName = categoryName;
    }

    public Long getId() {
        return id;
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

    public String getCategoryName() {
        return categoryName;
    }

    public void setId(Long id) {
        this.id = id;
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

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}