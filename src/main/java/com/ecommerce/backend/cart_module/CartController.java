package com.ecommerce.backend.cart_module;

import com.ecommerce.backend.cart_module.dto.CartRequest;
import com.ecommerce.backend.cart_module.dto.CartResponse;
import com.ecommerce.backend.cart_module.dto.UpdateCartRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ecommerce/cart")
@Tag(
        name = "Shopping Cart",
        description = "Manage customer shopping cart"
)
public class CartController {

    private final CartService cartService;

    public CartController(
            CartService cartService
    ) {
        this.cartService = cartService;
    }


    @Operation(summary = "Add Product to Cart")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product added to cart successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "401", description = "User not authenticated"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @PostMapping("/add")
    public ResponseEntity<CartResponse> addToCart(

            @Valid
            @RequestBody
            CartRequest request

    ) {

        return new ResponseEntity<>(

                cartService.addToCart(request),

                HttpStatus.OK

        );

    }


    @Operation(summary = "View Shopping Cart")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cart retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "User not authenticated")
    })
    @GetMapping
    public ResponseEntity<CartResponse> getCart() {

        return ResponseEntity.ok(

                cartService.getCart()

        );

    }


    @Operation(summary = "Update Cart Item Quantity")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cart updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid quantity"),
            @ApiResponse(responseCode = "401", description = "User not authenticated"),
            @ApiResponse(responseCode = "404", description = "Cart item not found")
    })
    @PutMapping("/{cartItemId}")
    public ResponseEntity<CartResponse> updateCartItem(

            @PathVariable
            Long cartItemId,

            @Valid
            @RequestBody
            UpdateCartRequest request

    ) {

        return ResponseEntity.ok(

                cartService.updateCartItem(
                        cartItemId,
                        request
                )

        );

    }


    @Operation(summary = "Remove Product from Cart")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product removed from cart"),
            @ApiResponse(responseCode = "401", description = "User not authenticated"),
            @ApiResponse(responseCode = "404", description = "Cart item not found")
    })
    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<CartResponse> removeCartItem(

            @PathVariable Long cartItemId

    ) {

        return ResponseEntity.ok(

                cartService.removeCartItem(cartItemId)

        );

    }


    @Operation(summary = "Clear Shopping Cart")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Cart cleared successfully"),
            @ApiResponse(responseCode = "401", description = "User not authenticated")
    })
    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart() {

        cartService.clearCart();

        return ResponseEntity.noContent().build();

    }

}