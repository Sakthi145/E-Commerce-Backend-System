package com.ecommerce.backend.order_module.dto;

import com.ecommerce.backend.order_module.models.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class OrderDetailsResponse {

    private Long orderId;

    private LocalDateTime orderDate;

    private BigDecimal totalAmount;

    private OrderStatus status;

    private List<OrderItemResponse> items;

}