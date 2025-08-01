package com.dvo.order_service.service.impl;

import com.dvo.order_service.entity.OrderItem;
import com.dvo.order_service.exception.EntityNotFoundException;
import com.dvo.order_service.mapper.OrderItemMapper;
import com.dvo.order_service.repository.OrderItemRepository;
import com.dvo.order_service.web.model.request.UpdateOrderItemRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderItemServiceImplTest {
    @InjectMocks
    private OrderItemServiceImpl orderItemService;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private OrderItemMapper orderItemMapper;

    private OrderItem orderItem;

    @BeforeEach
    public void setUp() {
        orderItem = OrderItem.builder()
                .id(1L)
                .productName("product")
                .build();
    }

    @Test
    void testFindAll() {
        when(orderItemRepository.findAll()).thenReturn(List.of(orderItem));
        List<OrderItem> orderItems = orderItemService.findAll();

        assertEquals(1, orderItems.size());
        verify(orderItemRepository).findAll();
    }

    @Test
    void testFindById() {
        when(orderItemRepository.findById(1L)).thenReturn(Optional.of(orderItem));
        OrderItem result = orderItemService.findById(1L);

        assertEquals(orderItem, result);
        verify(orderItemRepository).findById(1L);
    }

    @Test
    void testFindById_whenNotFound() {
        when(orderItemRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> orderItemService.findById(1L));
        verify(orderItemRepository).findById(1L);
    }

    @Test
    void testCreate() {
        when(orderItemRepository.save(orderItem)).thenReturn(orderItem);
        OrderItem result = orderItemService.create(orderItem);

        assertEquals(orderItem, result);
        verify(orderItemRepository).save(orderItem);
    }

    @Test
    void testUpdate() {
        UpdateOrderItemRequest request = UpdateOrderItemRequest.builder()
                .productName("new name")
                .build();

        when(orderItemRepository.findById(1L)).thenReturn(Optional.of(orderItem));
        when(orderItemRepository.save(orderItem)).thenReturn(orderItem);
        OrderItem result = orderItemService.update(request, 1L);

        assertEquals(orderItem, result);
        verify(orderItemRepository).findById(1L);
        verify(orderItemRepository).save(orderItem);
    }

    @Test
    void testUpdate_whenNotFound() {
        when(orderItemRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> orderItemService.update(new UpdateOrderItemRequest(), 1L));
    }

    @Test
    void testDeleteById() {
        orderItemService.deleteById(1L);
        verify(orderItemRepository).deleteById(1L);
    }

}
