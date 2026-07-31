package com.ecommerce.backend.exceptions;

public class OrderCancellationException extends RuntimeException {

    public OrderCancellationException(String message) {
        super(message);
    }

}