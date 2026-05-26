package com.sistore.orderservice.service;

import com.sistore.orderservice.dto.OrderRequest;
import com.sistore.orderservice.dto.OrderResponse;

public interface OrderService {
    OrderResponse placeOrder(OrderRequest orderRequest);
}
