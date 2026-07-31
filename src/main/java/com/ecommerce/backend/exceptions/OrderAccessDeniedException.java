package com.ecommerce.backend.exceptions;

public class OrderAccessDeniedException extends RuntimeException {

    public OrderAccessDeniedException(String message) {
        super(message);
    }

}