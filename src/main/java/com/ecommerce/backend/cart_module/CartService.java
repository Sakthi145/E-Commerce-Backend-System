package com.ecommerce.backend.cart_module;

import com.ecommerce.backend.cart_module.dto.CartItemResponse;
import com.ecommerce.backend.cart_module.dto.CartRequest;
import com.ecommerce.backend.cart_module.dto.CartResponse;
import com.ecommerce.backend.cart_module.dto.UpdateCartRequest;
import com.ecommerce.backend.cart_module.models.Cart;
import com.ecommerce.backend.cart_module.models.CartItem;
import com.ecommerce.backend.exceptions.*;
import com.ecommerce.backend.product_module.ProductRepository;
import com.ecommerce.backend.product_module.models.Product;
import com.ecommerce.backend.user_module.UserRepository;
import com.ecommerce.backend.user_module.models.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public CartService(
            CartRepository cartRepository,
            CartItemRepository cartItemRepository,
            ProductRepository productRepository,
            UserRepository userRepository
    ) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    private User getLoggedInUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found."
                        ));
    }

    public CartResponse addToCart(CartRequest request) {

        User user = getLoggedInUser();

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() ->
                        new ProductNotFoundException(
                                "Product not found."
                        ));

        if (request.getQuantity() > product.getStockQuantity()) {
            throw new InsufficientStockException(
                    "Requested quantity exceeds available stock."
            );
        }

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseGet(() -> {

                    Cart newCart = new Cart();

                    newCart.setUser(user);

                    return cartRepository.save(newCart);

                });

        CartItem cartItem = cartItemRepository
                .findByCartIdAndProductId(
                        cart.getId(),
                        product.getId()
                )
                .orElse(new CartItem());

        if (cartItem.getId() != null) {

            int newQuantity =
                    cartItem.getQuantity() + request.getQuantity();

            if (newQuantity > product.getStockQuantity()) {

                throw new InsufficientStockException(
                        "Requested quantity exceeds available stock."
                );

            }

            cartItem.setQuantity(newQuantity);

        } else {

            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(request.getQuantity());

        }

        cartItemRepository.save(cartItem);

        return getCart();

    }

    public CartResponse getCart() {

        User user = getLoggedInUser();

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseGet(() -> {

                    Cart newCart = new Cart();

                    newCart.setUser(user);

                    return cartRepository.save(newCart);

                });

        List<CartItem> cartItems =
                cartItemRepository.findByCartId(cart.getId());

        List<CartItemResponse> items = new ArrayList<>();

        BigDecimal grandTotal = BigDecimal.ZERO;

        for (CartItem item : cartItems) {

            BigDecimal totalPrice =
                    item.getProduct()
                            .getPrice()
                            .multiply(
                                    BigDecimal.valueOf(
                                            item.getQuantity()
                                    )
                            );

            grandTotal = grandTotal.add(totalPrice);

            items.add(

                    new CartItemResponse(

                            item.getId(),

                            item.getProduct().getId(),

                            item.getProduct().getName(),

                            item.getProduct().getPrice(),

                            item.getQuantity(),

                            totalPrice

                    )

            );

        }

        return new CartResponse(

                cart.getId(),

                items,

                grandTotal

        );

    }

    public CartResponse updateCartItem(

            Long cartItemId,

            UpdateCartRequest request

    ) {

        User user = getLoggedInUser();

        Cart cart = cartRepository.findByUserId(user.getId())

                .orElseThrow(() ->

                        new CartNotFoundException("Cart not found.")

                );

        CartItem cartItem = cartItemRepository.findById(cartItemId)

                .orElseThrow(() ->

                        new CartItemNotFoundException("Cart item not found.")
                );

        if (!cartItem.getCart().getId().equals(cart.getId())) {

            throw new CartAccessDeniedException(
                    "This cart item doesn't belong to you."
            );

        }

        cartItem.setQuantity(

                request.getQuantity()

        );

        cartItemRepository.save(cartItem);

        return getCart();

    }

    public CartResponse removeCartItem(Long cartItemId) {

        User user = getLoggedInUser();

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() ->
                        new CartNotFoundException("Cart not found."));

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() ->
                        new CartItemNotFoundException(
                                "Cart item not found."
                        ));

        if (!cartItem.getCart().getId().equals(cart.getId())) {

            throw new CartAccessDeniedException(
                    "This cart item doesn't belong to you."
            );

        }

        cartItemRepository.delete(cartItem);

        return getCart();

    }

    public void clearCart() {

        User user = getLoggedInUser();

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() ->
                        new CartNotFoundException("Cart not found."));

        cart.getCartItems().clear();

        cartRepository.save(cart);

    }
}