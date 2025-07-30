package com.dvo.order_service.service;

import com.dvo.order_service.entity.Order;
import com.dvo.order_service.entity.OrderStatus;
import com.dvo.order_service.web.model.request.UpsertOrderRequest;

import java.util.List;

public interface OrderService {
    List<Order> findAll();

    Order findById(Long id);

    Order create(Order order);

    Order update(UpsertOrderRequest request, Long id);

    void setOrderStatus(Long id, OrderStatus status);

    void addOrderItem(Long id, Long orderItemId);
}
