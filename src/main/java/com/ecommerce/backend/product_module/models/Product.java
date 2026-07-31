package com.ecommerce.backend.product_module.models;

import com.ecommerce.backend.cart_module.models.CartItem;
import com.ecommerce.backend.category_module.models.Category;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Product {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    @NotBlank(message = "Product name cannot be blank.")
    private String name;

    @Column(length = 1000)
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

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "product")
    private List<CartItem> cartItems = new ArrayList<>();
}
