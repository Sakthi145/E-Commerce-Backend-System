package com.ecommerce.backend.order_module.dto;

import com.ecommerce.backend.order_module.models.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutResponse {

    private Long orderId;

    private LocalDateTime orderDate;

    private BigDecimal totalAmount;

    private OrderStatus status;

}