package com.ecommerce.backend.exceptions;

public class CartAccessDeniedException extends RuntimeException {

    public CartAccessDeniedException(String message) {
        super(message);
    }

}