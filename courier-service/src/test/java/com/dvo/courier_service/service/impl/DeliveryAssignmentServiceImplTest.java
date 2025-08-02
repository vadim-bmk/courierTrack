package com.dvo.courier_service.service.impl;

import com.dvo.courier_service.client.OrderClient;
import com.dvo.courier_service.client.dto.OrderDto;
import com.dvo.courier_service.entity.Courier;
import com.dvo.courier_service.entity.DeliveryAssignment;
import com.dvo.courier_service.exception.EntityNotFoundException;
import com.dvo.courier_service.mapper.DeliveryAssignmentMapper;
import com.dvo.courier_service.repository.CourierRepository;
import com.dvo.courier_service.repository.DeliveryAssignmentRepository;
import com.dvo.courier_service.web.model.request.UpsertDeliveryAssignmentRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeliveryAssignmentServiceImplTest {
    @InjectMocks
    private DeliveryAssignmentServiceImpl deliveryAssignmentService;

    @Mock
    private DeliveryAssignmentRepository deliveryAssignmentRepository;

    @Mock
    private DeliveryAssignmentMapper deliveryAssignmentMapper;

    @Mock
    private OrderClient orderClient;

    @Mock
    private CourierRepository courierRepository;

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    private DeliveryAssignment deliveryAssignment;
    private Courier courier;
    private UpsertDeliveryAssignmentRequest request;

    @BeforeEach
    void setUp() {
        courier = Courier.builder().id(1L).build();

        deliveryAssignment = DeliveryAssignment.builder()
                .id(1L)
                .courier(courier)
                .orderId(1L)
                .build();

        request = UpsertDeliveryAssignmentRequest.builder().courierId(1L).orderId(1L).build();
    }

    @Test
    void testFindAll() {
        when(deliveryAssignmentRepository.findAll()).thenReturn(List.of(deliveryAssignment));

        List<DeliveryAssignment> result = deliveryAssignmentService.findAll();
        assertEquals(1, result.size());
        verify(deliveryAssignmentRepository).findAll();
    }

    @Test
    void testFindById() {
        when(deliveryAssignmentRepository.findById(1L)).thenReturn(Optional.of(deliveryAssignment));
        DeliveryAssignment result = deliveryAssignmentService.findById(1L);

        assertEquals(deliveryAssignment, result);
        verify(deliveryAssignmentRepository).findById(1L);
    }

    @Test
    void testFindById_whenNotFound() {
        when(deliveryAssignmentRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> deliveryAssignmentService.findById(1L));
        verify(deliveryAssignmentRepository).findById(1L);
    }

    @Test
    void testCreate() {
        when(orderClient.getOrderById(1L)).thenReturn(new OrderDto());
        when(courierRepository.findById(1L)).thenReturn(Optional.of(courier));
        when(deliveryAssignmentMapper.requestToDeliveryAssignment(request, courier)).thenReturn(deliveryAssignment);
        when(deliveryAssignmentRepository.save(deliveryAssignment)).thenReturn(deliveryAssignment);

        DeliveryAssignment result = deliveryAssignmentService.create(request);

        assertEquals(deliveryAssignment, result);
        verify(orderClient, times(2)).getOrderById(1L);
        verify(courierRepository).findById(1L);
        verify(deliveryAssignmentMapper).requestToDeliveryAssignment(request, courier);
        verify(deliveryAssignmentRepository).save(deliveryAssignment);
    }

    @Test
    void testCreate_whenOrderNotFound() {
        when(orderClient.getOrderById(1L)).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> deliveryAssignmentService.create(request));
        verify(deliveryAssignmentRepository, never()).save(any(DeliveryAssignment.class));
    }

    @Test
    void testCreate_whenCourierNotFound() {
        when(orderClient.getOrderById(1L)).thenReturn(new OrderDto());
        when(courierRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> deliveryAssignmentService.create(request));
        verify(deliveryAssignmentRepository, never()).save(any(DeliveryAssignment.class));
    }

    @Test
    void testUpdate() {
        when(orderClient.getOrderById(1L)).thenReturn(new OrderDto());
        when(courierRepository.findById(1L)).thenReturn(Optional.of(courier));
        when(deliveryAssignmentRepository.findById(1L)).thenReturn(Optional.of(deliveryAssignment));
        doNothing().when(deliveryAssignmentMapper).updateRequestToDeliveryAssignment(request, deliveryAssignment, courier);
        when(deliveryAssignmentRepository.save(deliveryAssignment)).thenReturn(deliveryAssignment);

        DeliveryAssignment result = deliveryAssignmentService.update(request, 1L);

        assertEquals(deliveryAssignment, result);
        verify(orderClient, times(1)).getOrderById(1L);
        verify(courierRepository).findById(1L);
        verify(deliveryAssignmentRepository).findById(1L);
        verify(deliveryAssignmentRepository).save(deliveryAssignment);
    }

    @Test
    void testUpdate_whenOrderNotFound() {
        when(orderClient.getOrderById(1L)).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> deliveryAssignmentService.update(request, 1L));
        verify(deliveryAssignmentRepository, never()).save(any(DeliveryAssignment.class));
    }

    @Test
    void testUpdate_whenCourierNotFound() {
        when(orderClient.getOrderById(1L)).thenReturn(new OrderDto());
        when(courierRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> deliveryAssignmentService.update(request, 1L));
        verify(deliveryAssignmentRepository, never()).save(any(DeliveryAssignment.class));
    }

    @Test
    void testUpdate_whenDeliveryAssignmentNotFound() {
        when(orderClient.getOrderById(1L)).thenReturn(new OrderDto());
        when(courierRepository.findById(1L)).thenReturn(Optional.of(courier));
        when(deliveryAssignmentRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> deliveryAssignmentService.update(request, 1L));
        verify(deliveryAssignmentRepository, never()).save(any(DeliveryAssignment.class));
    }

    @Test
    void testDeleteById() {
        deliveryAssignmentService.deleteById(1L);

        verify(deliveryAssignmentRepository).deleteById(1L);
    }

    @Test
    void testFindByOrderId() {
        when(deliveryAssignmentRepository.findByOrderId(1L)).thenReturn(Optional.of(deliveryAssignment));

        DeliveryAssignment result = deliveryAssignmentService.findByOrderId(1L);
        assertEquals(deliveryAssignment, result);
        verify(deliveryAssignmentRepository).findByOrderId(1L);
    }

    @Test
    void testFindByOrderId_whenNotFound() {
        when(deliveryAssignmentRepository.findByOrderId(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> deliveryAssignmentService.findByOrderId(1L));
    }
}
