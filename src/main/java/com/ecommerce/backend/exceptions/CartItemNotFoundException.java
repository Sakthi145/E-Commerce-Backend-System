package com.ecommerce.backend.exceptions;

public class CartItemNotFoundException extends RuntimeException {

    public CartItemNotFoundException(String message) {
        super(message);
    }

}