package com.dvo.order_service.service;

import com.dvo.order_service.entity.OrderItem;
import com.dvo.order_service.web.model.request.UpdateOrderItemRequest;

import java.util.List;

public interface OrderItemService {
    List<OrderItem> findAll();

    OrderItem findById(Long id);

    OrderItem create(OrderItem orderItem);

    OrderItem update(UpdateOrderItemRequest request, Long id);

    void deleteById(Long id);
}
