package com.ecommerce.backend.cart_module.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class CartItemResponse {

    private Long cartItemId;

    private Long productId;

    private String productName;

    private BigDecimal price;

    private Integer quantity;

    private BigDecimal totalPrice;

}