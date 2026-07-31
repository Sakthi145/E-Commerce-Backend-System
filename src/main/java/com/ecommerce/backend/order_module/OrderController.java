package com.ecommerce.backend.order_module;

import com.ecommerce.backend.order_module.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ecommerce/orders")
@Tag(
        name = "Orders",
        description = "Manage customer orders and order status"
)
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }



    @Operation(summary = "Place Order (Checkout)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order placed successfully"),
            @ApiResponse(responseCode = "400", description = "Cart is empty or insufficient stock"),
            @ApiResponse(responseCode = "401", description = "User not authenticated")
    })
    @PostMapping("/checkout")
    public ResponseEntity<CheckoutResponse> checkout() {

        return ResponseEntity.ok(

                orderService.checkout()

        );

    }


    @Operation(summary = "View My Orders")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Orders retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "User not authenticated")
    })
    @GetMapping
    public ResponseEntity<List<OrderSummaryResponse>> getMyOrders() {

        return ResponseEntity.ok(

                orderService.getMyOrders()

        );

    }


    @Operation(summary = "View Order Details")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order details retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "User not authenticated"),
            @ApiResponse(responseCode = "403", description = "You are not authorized to view this order"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDetailsResponse> getOrderDetails(

            @PathVariable Long orderId

    ) {

        return ResponseEntity.ok(

                orderService.getOrderDetails(orderId)

        );

    }



    @Operation(summary = "Cancel Order")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order cancelled successfully"),
            @ApiResponse(responseCode = "400", description = "Order cannot be cancelled"),
            @ApiResponse(responseCode = "401", description = "User not authenticated"),
            @ApiResponse(responseCode = "403", description = "You are not authorized to cancel this order"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<CancelOrderResponse> cancelOrder(

            @PathVariable Long orderId

    ) {

        return ResponseEntity.ok(

                orderService.cancelOrder(orderId)

        );

    }


    @Operation(summary = "Track Order Status")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order status retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "User not authenticated"),
            @ApiResponse(responseCode = "403", description = "You are not authorized to view this order"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @GetMapping("/{orderId}/status")
    public ResponseEntity<OrderStatusResponse> trackOrderStatus(

            @PathVariable Long orderId

    ) {

        return ResponseEntity.ok(

                orderService.trackOrderStatus(orderId)

        );

    }


    @Operation(summary = "Update Order Status (Admin Only)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order status updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid status transition"),
            @ApiResponse(responseCode = "401", description = "User not authenticated"),
            @ApiResponse(responseCode = "403", description = "Only ADMIN can update order status"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @PutMapping("/{orderId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UpdateOrderStatusResponse> updateOrderStatus(

            @PathVariable Long orderId,

            @Valid @RequestBody UpdateOrderStatusRequest request

    ) {

        return ResponseEntity.ok(

                orderService.updateOrderStatus(

                        orderId,

                        request

                )

        );

    }

}