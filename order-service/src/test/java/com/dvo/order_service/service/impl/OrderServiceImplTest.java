package com.dvo.order_service.service.impl;

import com.dvo.order_service.client.UserClient;
import com.dvo.order_service.client.dto.UserDto;
import com.dvo.order_service.entity.Order;
import com.dvo.order_service.entity.OrderItem;
import com.dvo.order_service.entity.OrderStatus;
import com.dvo.order_service.exception.EntityExistsException;
import com.dvo.order_service.exception.EntityNotFoundException;
import com.dvo.order_service.mapper.OrderMapper;
import com.dvo.order_service.repository.OrderItemRepository;
import com.dvo.order_service.repository.OrderRepository;
import com.dvo.order_service.web.model.request.UpsertOrderRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {
    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderItemRepository orderItemRepository;
    @Mock
    private OrderMapper orderMapper;
    @Mock
    private UserClient userClient;
    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    private Order order;
    private OrderItem orderItem;

    @BeforeEach
    void setUp() {
        order = Order.builder()
                .id(1L)
                .description("description")
                .status(OrderStatus.NEW)
                .customerId(1L)
                .orderItems(new ArrayList<>())
                .build();

        orderItem = OrderItem.builder()
                .id(1L)
                .price(BigDecimal.valueOf(100))
                .build();
    }

    @Test
    void testFindAll() {
        when(orderRepository.findAll()).thenReturn(List.of(order));

        List<Order> result = orderService.findAll();

        assertEquals(1, result.size());
        verify(orderRepository).findAll();
    }

    @Test
    void testFindById() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        Order result = orderService.findById(1L);

        assertEquals(order, result);
        verify(orderRepository).findById(1L);
    }

    @Test
    void testFindById_whenNotFound() {
        when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> orderService.findById(1L));
    }

    @Test
    void testCreate() {
        when(userClient.getUserById(1L)).thenReturn(new UserDto());
        when(orderRepository.save(order)).thenReturn(order);

        Order result = orderService.create(order);

        assertEquals(order, result);
        verify(userClient).getUserById(1L);
        verify(orderRepository).save(order);
    }

    @Test
    void testCreate_whenUserNotFound() {
        when(userClient.getUserById(anyLong())).thenThrow(EntityNotFoundException.class);
        assertThrows(EntityNotFoundException.class, () -> orderService.create(order));
        verify(orderRepository, never()).save(any(Order.class));
        verify(kafkaTemplate, never()).send(anyString(), any());
    }

    @Test
    void testUpdate() {
        UpsertOrderRequest request = UpsertOrderRequest.builder()
                .customerId(1L)
                .description("description")
                .build();

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(userClient.getUserById(1L)).thenReturn(new UserDto());
        doNothing().when(orderMapper).updateRequestToOrder(request, order);
        when(orderRepository.save(order)).thenReturn(order);

        Order result = orderService.update(request, 1L);

        assertEquals(order, result);
        verify(orderRepository).findById(1L);
        verify(userClient).getUserById(1L);
        verify(orderRepository).save(order);
    }

    @Test
    void testUpdate_whenOrderNotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> orderService.update(new UpsertOrderRequest(), 1L));
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void testUpdate_whenUserNotFound() {
        UpsertOrderRequest request = UpsertOrderRequest.builder()
                .customerId(1L)
                .description("description")
                .build();

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(userClient.getUserById(1L)).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> orderService.update(request, 1L));
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void testSetOrderStatus() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);

        orderService.setOrderStatus(1L, OrderStatus.CANCELED);
        assertEquals(OrderStatus.CANCELED, order.getStatus());
        verify(orderRepository).save(order);
    }

    @Test
    void testSetOrderStatus_whenOrderNotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> orderService.setOrderStatus(1L, OrderStatus.CANCELED));
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void testAddOrderItem() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderItemRepository.findById(1L)).thenReturn(Optional.of(orderItem));
        when(orderRepository.save(order)).thenReturn(order);

        orderService.addOrderItem(1L, 1L);

        assertEquals(1, order.getOrderItems().size());
        assertEquals(orderItem.getId(), order.getOrderItems().getFirst().getId());
        verify(orderRepository).findById(1L);
        verify(orderItemRepository).findById(1L);
        verify(orderRepository).save(order);
    }

    @Test
    void testAddOrderItem_whenOrderNotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> orderService.addOrderItem(1L, 1L));
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void testAddOrderItem_whenOrderItemNotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderItemRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> orderService.addOrderItem(1L, 1L));
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void testAddOrderItem_whenOrderItemAlreadyAdded() {
        order.getOrderItems().add(orderItem);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderItemRepository.findById(1L)).thenReturn(Optional.of(orderItem));

        assertThrows(EntityExistsException.class, () -> orderService.addOrderItem(1L, 1L));
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void testDeleteOrderItemFromOrder() {
        order.getOrderItems().add(orderItem);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderItemRepository.findById(1L)).thenReturn(Optional.of(orderItem));
        when(orderRepository.save(order)).thenReturn(order);

        orderService.deleteOrderItemFromOrder(1L, 1L);
        assertEquals(0, order.getOrderItems().size());
        verify(orderRepository).findById(1L);
        verify(orderItemRepository).findById(1L);
        verify(orderRepository).save(order);
    }

    @Test
    void testDeleteOrderItemFromOrder_whenOrderNotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> orderService.deleteOrderItemFromOrder(1L, 1L));
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void testDeleteOrderItemFromOrder_whenOrderItemNotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderItemRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> orderService.deleteOrderItemFromOrder(1L, 1L));
        verify(orderRepository, never()).save(any(Order.class));
    }
}
