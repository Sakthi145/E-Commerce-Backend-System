package com.ecommerce.backend.cart_module.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CartResponse {

    private Long cartId;

    private List<CartItemResponse> items;

    private BigDecimal grandTotal;

}