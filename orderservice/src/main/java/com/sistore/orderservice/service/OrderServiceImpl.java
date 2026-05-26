package com.sistore.orderservice.service;

import com.sistore.orderservice.client.ProductClient;
import com.sistore.orderservice.dto.*;
import com.sistore.orderservice.entity.Order;
import com.sistore.orderservice.entity.OrderItem;
import com.sistore.orderservice.entity.User;
import com.sistore.orderservice.exception.ResourceNotFoundException;
import com.sistore.orderservice.repository.OrderRepository;
import com.sistore.orderservice.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ProductClient productClient;

    public OrderServiceImpl(UserRepository userRepository, OrderRepository orderRepository,ProductClient productClient){
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.productClient = productClient;
    }

    @Transactional
    public OrderResponse placeOrder(OrderRequest request) {
        User user = userRepository.findById(request.getUserId()).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        ));
        Order order = new Order();
        order.setUser(user);
        order.setOrderedAt(LocalDateTime.now());
        order.setOrderStatus("PLACED");

        List<OrderItem> orderItems = new ArrayList<>();

        BigDecimal total = BigDecimal.ZERO;
        for (OrderItemRequest itemRequest : request.getItems()) {
            ProductResponse product = productClient.getProductById(itemRequest.getProductId());
            if (!product.getAvailable()) {
                throw new RuntimeException("Product unavailable");
            }
            if (product.getStock() < itemRequest.getQuantity()) {
                throw new RuntimeException("Insufficient stock");
            }

            OrderItem item = new OrderItem();
            item.setProductId(product.getId());
            item.setProductName(product.getName());
            item.setPrice(product.getPrice());
            item.setQuantity(itemRequest.getQuantity());
            item.setOrder(order);
            orderItems.add(item);

            total = total.add(product.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity())));
        }

        order.setOrderItems(orderItems);
        order.setTotalAmount(total.doubleValue());

        Order savedOrder = orderRepository.save(order);
        return mapToResponse(savedOrder);
    }

    private OrderResponse mapToResponse(Order order) {
        List<OrderItemResponse> items = order.getOrderItems()
                            .stream()
                            .map(item ->
                                OrderItemResponse
                                        .builder()
                                        .productName(item.getProductName())
                                        .quantity(item.getQuantity())
                                        .price(item.getPrice())
                                        .build())
                        .toList();

        return OrderResponse.builder()
                .orderId(order.getOrderId())
                .orderStatus(order.getOrderStatus())
                .totalAmount(order.getTotalAmount())
                .orderedAt(order.getOrderedAt())
                .items(items)
                .build();
    }
}
