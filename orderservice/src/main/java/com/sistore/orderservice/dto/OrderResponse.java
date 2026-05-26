package com.sistore.orderservice.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class OrderResponse {
    private Long orderId;
    private String orderStatus;
    private Double totalAmount;
    private LocalDateTime orderedAt;
    private List<OrderItemResponse> items;
}
