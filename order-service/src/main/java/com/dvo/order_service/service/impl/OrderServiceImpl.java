package com.dvo.order_service.service.impl;

import com.dvo.order_service.client.UserClient;
import com.dvo.order_service.client.dto.UserDto;
import com.dvo.order_service.entity.Order;
import com.dvo.order_service.entity.OrderItem;
import com.dvo.order_service.entity.OrderStatus;
import com.dvo.order_service.event.OrderChangedOrderItem;
import com.dvo.order_service.event.OrderStatusChangedEvent;
import com.dvo.order_service.exception.EntityExistsException;
import com.dvo.order_service.exception.EntityNotFoundException;
import com.dvo.order_service.mapper.OrderMapper;
import com.dvo.order_service.repository.OrderItemRepository;
import com.dvo.order_service.repository.OrderRepository;
import com.dvo.order_service.service.OrderService;
import com.dvo.order_service.web.model.request.UpsertOrderRequest;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderMapper orderMapper;
    private final UserClient userClient;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public List<Order> findAll() {
        log.info("Call findAll in OrderServiceImpl");

        return orderRepository.findAll();
    }

    @Override
    public Order findById(Long id) {
        log.info("Call findById in OrderServiceImpl with ID: {}", id);

        return orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Order with ID: {0} not found", id)));
    }

    @Override
    @Transactional
    public Order create(Order order) {
        log.info("Call create in OrderServiceImpl with order: {}", order);

        try {
            UserDto userDto = userClient.getUserById(order.getCustomerId());
        } catch (FeignException.NotFound e) {
            throw new EntityNotFoundException(MessageFormat.format("User with ID (customerId): {0} not found", order.getCustomerId()));
        }

        Order newOrder = orderRepository.save(order);

        OrderStatusChangedEvent event = OrderStatusChangedEvent.builder()
                .orderId(newOrder.getId())
                .customerId(newOrder.getCustomerId())
                .status(newOrder.getStatus().toString())
                .build();

        kafkaTemplate.send("order-status-topic", event);

        return newOrder;
    }

    @Override
    @Transactional
    public Order update(UpsertOrderRequest request, Long id) {
        log.info("Call update in OrderServiceImpl for ID: {}, with request: {}", id, request);

        Order existedOrder = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Order with ID: {0} not found", id)));
        try {
            UserDto userDto = userClient.getUserById(request.getCustomerId());
        } catch (FeignException.NotFound e) {
            throw new EntityNotFoundException(MessageFormat.format("User with ID (customerId): {0} not found", request.getCustomerId()));
        }
        orderMapper.updateRequestToOrder(request, existedOrder);

        return orderRepository.save(existedOrder);
    }

    @Override
    @Transactional
    public void setOrderStatus(Long id, OrderStatus status) {
        log.info("Call setOrderStatus in OrderServiceImpl for ID: {}, with status: {}", id, status);

        Order existedOrder = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Order with ID: {0} not found", id)));

        existedOrder.setStatus(status);

        orderRepository.save(existedOrder);
        log.info("Order with ID: {} updated with status: {}", id, status);

        OrderStatusChangedEvent event = OrderStatusChangedEvent.builder()
                .orderId(existedOrder.getId())
                .customerId(existedOrder.getCustomerId())
                .status(existedOrder.getStatus().toString())
                .build();

        kafkaTemplate.send("order-status-topic", event);
    }

    @Override
    @Transactional
    public void addOrderItem(Long id, Long orderItemId) {
        log.info("Call addOrderItem in OrderServiceImpl for ID: {}, with orderItemId: {}", id, orderItemId);

        Order existedOrder = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Order with ID: {0} not found", id)));

        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("OrderItem with ID: {0} not found", orderItemId)));

        boolean alreadyExists = existedOrder.getOrderItems().stream()
                .anyMatch(item -> item.getId().equals(orderItem.getId()));

        if (alreadyExists) {
            throw new EntityExistsException(MessageFormat.format("OrderItem with ID: {0} already exists in Order with ID: {1}", orderItemId, id));
        }

        orderItem.setOrder(existedOrder);
        existedOrder.getOrderItems().add(orderItem);
        orderRepository.save(existedOrder);
        log.info("Order with ID: {} updated with orderItemId: {}", id, orderItemId);

        OrderChangedOrderItem event = OrderChangedOrderItem.builder()
                .orderId(existedOrder.getId())
                .customerId(existedOrder.getCustomerId())
                .orderItemId(orderItemId)
                .action("добавлен")
                .build();

        kafkaTemplate.send("order-changed-order-item-topic", event);
    }

    @Override
    public void deleteOrderItemFromOrder(Long id, Long orderItemId) {
        log.info("Call deleteOrderItemFromOrder in OrderServiceImpl for ID: {}, with orderItemId: {}", id, orderItemId);

        Order existedOrder = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Order with ID: {0} not found", id)));

        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("OrderItem with ID: {0} not found", orderItemId)));

        orderItem.setOrder(null);
        existedOrder.getOrderItems().removeIf(item -> item.getId().equals(orderItemId));

        orderRepository.save(existedOrder);

        OrderChangedOrderItem event = OrderChangedOrderItem.builder()
                .orderId(existedOrder.getId())
                .customerId(existedOrder.getCustomerId())
                .orderItemId(orderItemId)
                .action("удален")
                .build();

        kafkaTemplate.send("order-changed-order-item-topic", event);
    }
}
