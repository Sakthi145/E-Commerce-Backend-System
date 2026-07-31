package com.ecommerce.backend.order_module.dto;

import com.ecommerce.backend.order_module.models.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOrderStatusResponse {

    private Long orderId;

    private OrderStatus status;

}