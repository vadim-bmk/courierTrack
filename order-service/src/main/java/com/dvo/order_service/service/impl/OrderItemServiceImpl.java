package com.dvo.order_service.service.impl;

import com.dvo.order_service.entity.OrderItem;
import com.dvo.order_service.exception.EntityNotFoundException;
import com.dvo.order_service.mapper.OrderItemMapper;
import com.dvo.order_service.repository.OrderItemRepository;
import com.dvo.order_service.service.OrderItemService;
import com.dvo.order_service.web.model.request.UpdateOrderItemRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {
    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;

    @Override
    public List<OrderItem> findAll() {
        log.info("Call findAll in OrderItemServiceImpl");

        return orderItemRepository.findAll();
    }

    @Override
    public OrderItem findById(Long id) {
        log.info("Call findById in OrderItemServiceImpl with ID: {}", id);

        return orderItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("OrderItem with ID: {0} not found", id)));
    }

    @Override
    @Transactional
    public OrderItem create(OrderItem orderItem) {
        log.info("Call create in OrderItemServiceImpl with orderItem: {}", orderItem);

        return orderItemRepository.save(orderItem);
    }

    @Override
    @Transactional
    public OrderItem update(UpdateOrderItemRequest request, Long id) {
        log.info("Call update in OrderItemServiceImpl for ID: {}, with request: {}", id, request);

        OrderItem existedOrderItem = orderItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("OrderItem with ID: {0} not found", id)));

        orderItemMapper.updateRequestToOrderItem(request, existedOrderItem);
        return orderItemRepository.save(existedOrderItem);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        log.info("Call deleteById in OrderItemServiceImpl with ID: {}", id);

        orderItemRepository.deleteById(id);
    }
}
