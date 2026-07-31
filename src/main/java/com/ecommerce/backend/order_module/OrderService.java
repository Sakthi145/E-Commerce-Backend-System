package com.ecommerce.backend.order_module;

import com.ecommerce.backend.cart_module.CartItemRepository;
import com.ecommerce.backend.cart_module.CartRepository;
import com.ecommerce.backend.cart_module.models.Cart;
import com.ecommerce.backend.cart_module.models.CartItem;
import com.ecommerce.backend.exceptions.*;
import com.ecommerce.backend.order_module.dto.*;
import com.ecommerce.backend.order_module.models.Order;
import com.ecommerce.backend.order_module.models.OrderItem;
import com.ecommerce.backend.order_module.models.OrderStatus;
import com.ecommerce.backend.product_module.ProductRepository;
import com.ecommerce.backend.product_module.models.Product;
import com.ecommerce.backend.user_module.UserRepository;
import com.ecommerce.backend.user_module.models.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public OrderService(
            OrderRepository orderRepository,
            OrderItemRepository orderItemRepository,
            CartRepository cartRepository,
            CartItemRepository cartItemRepository,
            ProductRepository productRepository,
            UserRepository userRepository
    ) {

        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
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

    @Transactional
    public CheckoutResponse checkout() {

        User user = getLoggedInUser();

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() ->
                        new CartNotFoundException("Cart not found."));

        List<CartItem> cartItems =
                cartItemRepository.findByCartId(cart.getId());

        if (cartItems.isEmpty()) {

            throw new CartEmptyException("Cart is empty.");

        }

        for (CartItem cartItem : cartItems) {

            Product product = cartItem.getProduct();

            if (cartItem.getQuantity() > product.getStockQuantity()) {

                throw new InsufficientStockException(
                        product.getName()
                                + " has only "
                                + product.getStockQuantity()
                                + " item(s) in stock."
                );

            }

        }

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (CartItem cartItem : cartItems) {

            BigDecimal itemTotal = cartItem.getProduct()
                    .getPrice()
                    .multiply(
                            BigDecimal.valueOf(cartItem.getQuantity())
                    );

            totalAmount = totalAmount.add(itemTotal);

        }

        Order order = new Order();

        order.setUser(user);

        order.setStatus(OrderStatus.PLACED);

        order.setOrderDate(LocalDateTime.now());

        order.setTotalAmount(totalAmount);

        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : cartItems) {

            OrderItem orderItem = new OrderItem();

            orderItem.setOrder(order);

            orderItem.setProduct(cartItem.getProduct());

            orderItem.setQuantity(cartItem.getQuantity());

            orderItem.setPrice(
                    cartItem.getProduct().getPrice()
            );

            orderItems.add(orderItem);

        }

        Order savedOrder = orderRepository.save(order);

        for (OrderItem orderItem : orderItems) {

            orderItem.setOrder(savedOrder);

        }

        orderItemRepository.saveAll(orderItems);

        for (CartItem cartItem : cartItems) {

            Product product = cartItem.getProduct();

            product.setStockQuantity(

                    product.getStockQuantity()
                            - cartItem.getQuantity()

            );

            productRepository.save(product);

        }
        cart.getCartItems().clear();

        cartRepository.save(cart);

        return new CheckoutResponse(

                savedOrder.getId(),

                savedOrder.getOrderDate(),

                savedOrder.getTotalAmount(),

                savedOrder.getStatus()

        );

    }

    public List<OrderSummaryResponse> getMyOrders() {

        User user = getLoggedInUser();

        List<Order> orders = orderRepository.findByUserId(user.getId());

        List<OrderSummaryResponse> response = new ArrayList<>();

        for (Order order : orders) {

            response.add(

                    new OrderSummaryResponse(

                            order.getId(),

                            order.getOrderDate(),

                            order.getTotalAmount(),

                            order.getStatus()

                    )

            );

        }

        return response;

    }

    public OrderDetailsResponse getOrderDetails(Long orderId) {

        User user = getLoggedInUser();

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new OrderNotFoundException("Order not found."));

        if (!order.getUser().getId().equals(user.getId())) {

            throw new OrderAccessDeniedException(
                    "You are not authorized to view this order."
            );

        }

        List<OrderItemResponse> items = new ArrayList<>();

        for (OrderItem orderItem : order.getOrderItems()) {

            BigDecimal totalPrice = orderItem.getPrice()
                    .multiply(
                            BigDecimal.valueOf(
                                    orderItem.getQuantity()
                            )
                    );

            items.add(

                    new OrderItemResponse(

                            orderItem.getProduct().getId(),

                            orderItem.getProduct().getName(),

                            orderItem.getQuantity(),

                            orderItem.getPrice(),

                            totalPrice

                    )

            );

        }

        return new OrderDetailsResponse(

                order.getId(),

                order.getOrderDate(),

                order.getTotalAmount(),

                order.getStatus(),

                items

        );

    }

    @Transactional
    public CancelOrderResponse cancelOrder(Long orderId) {

        User user = getLoggedInUser();

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new OrderNotFoundException(
                                "Order not found."
                        ));

        if (!order.getUser().getId().equals(user.getId())) {

            throw new OrderAccessDeniedException(
                    "You are not authorized to cancel this order."
            );

        }

        if (order.getStatus() != OrderStatus.PLACED) {

            throw new OrderCancellationException(
                    "Only placed orders can be cancelled."
            );

        }



        for (OrderItem orderItem : order.getOrderItems()) {

            Product product = orderItem.getProduct();

            product.setStockQuantity(

                    product.getStockQuantity()
                            + orderItem.getQuantity()

            );

            productRepository.save(product);

        }



        order.setStatus(OrderStatus.CANCELLED);

        Order cancelledOrder = orderRepository.save(order);

        return new CancelOrderResponse(

                cancelledOrder.getId(),

                cancelledOrder.getOrderDate(),

                cancelledOrder.getTotalAmount(),

                cancelledOrder.getStatus()

        );

    }

    public OrderStatusResponse trackOrderStatus(Long orderId) {

        User user = getLoggedInUser();

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new OrderNotFoundException(
                                "Order not found."
                        ));

        if (!order.getUser().getId().equals(user.getId())) {

            throw new OrderAccessDeniedException(
                    "You are not authorized to view this order."
            );

        }

        return new OrderStatusResponse(

                order.getId(),

                order.getStatus()

        );

    }

    public UpdateOrderStatusResponse updateOrderStatus(

            Long orderId,

            UpdateOrderStatusRequest request

    ) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new OrderNotFoundException(
                                "Order not found."
                        ));

        OrderStatus currentStatus = order.getStatus();

        OrderStatus newStatus = request.getStatus();

        if (!isValidStatusTransition(currentStatus, newStatus)) {

            throw new InvalidOrderStatusException(
                    "Invalid order status transition from "
                            + currentStatus
                            + " to "
                            + newStatus
            );

        }

        order.setStatus(newStatus);

        orderRepository.save(order);

        return new UpdateOrderStatusResponse(

                order.getId(),

                order.getStatus()

        );

    }

    private boolean isValidStatusTransition(

            OrderStatus current,

            OrderStatus next

    ) {

        return switch (current) {

            case PLACED ->
                    next == OrderStatus.CONFIRMED;

            case CONFIRMED ->
                    next == OrderStatus.SHIPPED;

            case SHIPPED ->
                    next == OrderStatus.OUT_FOR_DELIVERY;

            case OUT_FOR_DELIVERY ->
                    next == OrderStatus.DELIVERED;

            case DELIVERED,
                 CANCELLED -> false;

        };

    }

}