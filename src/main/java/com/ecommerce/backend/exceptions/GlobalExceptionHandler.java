package com.ecommerce.backend.exceptions;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.LocalDateTime;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CategoryAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleCategoryAlreadyExistsException(
            CategoryAlreadyExistsException ex,
            HttpServletRequest request) {

        return buildErrorResponse(
                ex.getMessage(),
                HttpStatus.CONFLICT,
                request
        );
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCategoryNotFoundException(
            CategoryNotFoundException ex,
            HttpServletRequest request) {

        return buildErrorResponse(
                ex.getMessage(),
                HttpStatus.NOT_FOUND,
                request
        );
    }

    @ExceptionHandler(ProductAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleProductAlreadyExistsException(
            ProductAlreadyExistsException ex,
            HttpServletRequest request
    ) {

        return buildErrorResponse(
                ex.getMessage(),
                HttpStatus.CONFLICT,
                request
        );

    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProductNotFoundException(
            ProductNotFoundException ex,
            HttpServletRequest request
    ) {

        return buildErrorResponse(
                ex.getMessage(),
                HttpStatus.NOT_FOUND,
                request
        );

    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExistsException(

            UserAlreadyExistsException ex,

            HttpServletRequest request

    ) {

        return buildErrorResponse(

                ex.getMessage(),

                HttpStatus.CONFLICT,

                request

        );

    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(

            UserNotFoundException ex,

            HttpServletRequest request

    ){

        return buildErrorResponse(

                ex.getMessage(),

                HttpStatus.NOT_FOUND,

                request

        );

    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCredentialsException(

            InvalidCredentialsException ex,

            HttpServletRequest request

    ){

        return buildErrorResponse(

                ex.getMessage(),

                HttpStatus.UNAUTHORIZED,

                request

        );

    }

    @ExceptionHandler(CartItemNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCartItemNotFoundException(

            CartItemNotFoundException ex,

            HttpServletRequest request

    ) {

        return buildErrorResponse(

                ex.getMessage(),

                HttpStatus.NOT_FOUND,

                request

        );

    }

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCartNotFoundException(

            CartNotFoundException ex,

            HttpServletRequest request

    ) {

        return buildErrorResponse(

                ex.getMessage(),

                HttpStatus.NOT_FOUND,

                request

        );

    }

    @ExceptionHandler(CartEmptyException.class)
    public ResponseEntity<ErrorResponse> handleCartEmptyException(

            CartEmptyException ex,

            HttpServletRequest request

    ) {

        return buildErrorResponse(

                ex.getMessage(),

                HttpStatus.BAD_REQUEST,

                request

        );

    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleOrderNotFoundException(

            OrderNotFoundException ex,
            HttpServletRequest request

    ){

        return buildErrorResponse(
                ex.getMessage(),
                HttpStatus.NOT_FOUND,
                request
        );

    }

    @ExceptionHandler(OrderAccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleOrderAccessDeniedException(

            OrderAccessDeniedException ex,
            HttpServletRequest request

    ){

        return buildErrorResponse(
                ex.getMessage(),
                HttpStatus.FORBIDDEN,
                request
        );

    }

    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<ErrorResponse> handleInsufficientStockException(

            InsufficientStockException ex,
            HttpServletRequest request

    ){

        return buildErrorResponse(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST,
                request
        );

    }

    @ExceptionHandler(OrderCancellationException.class)
    public ResponseEntity<ErrorResponse> handleOrderCancellationException(

            OrderCancellationException ex,
            HttpServletRequest request

    ){

        return buildErrorResponse(

                ex.getMessage(),

                HttpStatus.BAD_REQUEST,

                request

        );

    }

    @ExceptionHandler(CartAccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleCartAccessDeniedException(

            CartAccessDeniedException ex,
            HttpServletRequest request

    ) {

        return buildErrorResponse(

                ex.getMessage(),

                HttpStatus.FORBIDDEN,

                request

        );

    }

    @ExceptionHandler(InvalidOrderStatusException.class)
    public ResponseEntity<ErrorResponse> handleInvalidOrderStatusException(

            InvalidOrderStatusException ex,

            HttpServletRequest request

    ) {

        return buildErrorResponse(

                ex.getMessage(),

                HttpStatus.BAD_REQUEST,

                request

        );

    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(
            String message,
            HttpStatus status,
            HttpServletRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(
                request.getRequestURI(),
                status.value(),
                LocalDateTime.now(),
                message
        );

        return new ResponseEntity<>(errorResponse, status);
    }
}