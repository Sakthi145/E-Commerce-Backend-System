package com.ecommerce.backend.cart_module;

import com.ecommerce.backend.cart_module.models.Cart;
import com.ecommerce.backend.user_module.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository
        extends JpaRepository<Cart, Long> {

    Optional<Cart> findByUserId(Long userId);


}