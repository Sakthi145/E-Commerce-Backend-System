package com.ecommerce.backend.order_module;

import com.ecommerce.backend.order_module.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository
        extends JpaRepository<Order, Long> {

    List<Order> findByUserId(Long userId);

}