package com.dvo.courier_service.service.impl;

import com.dvo.courier_service.client.OrderClient;
import com.dvo.courier_service.client.dto.OrderDto;
import com.dvo.courier_service.entity.Courier;
import com.dvo.courier_service.entity.DeliveryAssignment;
import com.dvo.courier_service.event.DeliveryAssignmentsEvent;
import com.dvo.courier_service.exception.EntityNotFoundException;
import com.dvo.courier_service.mapper.DeliveryAssignmentMapper;
import com.dvo.courier_service.repository.CourierRepository;
import com.dvo.courier_service.repository.DeliveryAssignmentRepository;
import com.dvo.courier_service.service.DeliveryAssignmentService;
import com.dvo.courier_service.web.model.request.UpsertDeliveryAssignmentRequest;
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
public class DeliveryAssignmentServiceImpl implements DeliveryAssignmentService {
    private final DeliveryAssignmentRepository deliveryAssignmentRepository;
    private final DeliveryAssignmentMapper deliveryAssignmentMapper;
    private final OrderClient orderClient;
    private final CourierRepository courierRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public List<DeliveryAssignment> findAll() {
        log.info("Call findAll in DeliveryAssignmentServiceImpl");

        return deliveryAssignmentRepository.findAll();
    }

    @Override
    public DeliveryAssignment findById(Long id) {
        log.info("Call findById in DeliveryAssignmentServiceImpl with ID: {}", id);

        return deliveryAssignmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("DeliveryAssignment with ID: {0} not found", id)));
    }

    @Override
    @Transactional
    public DeliveryAssignment create(UpsertDeliveryAssignmentRequest request) {
        log.info("Call create in DeliveryAssignmentServiceImpl with request: {}", request);

        try {
            OrderDto order = orderClient.getOrderById(request.getOrderId());
        } catch (FeignException.NotFound e) {
            throw new EntityNotFoundException(MessageFormat.format("Order with ID: {0} not found", request.getOrderId()));
        }

        Courier courier = courierRepository.findById(request.getCourierId())
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Courier with ID: {0} not found", request.getCourierId())));

        DeliveryAssignment deliveryAssignment = deliveryAssignmentMapper.requestToDeliveryAssignment(request, courier);

        DeliveryAssignmentsEvent event = DeliveryAssignmentsEvent.builder()
                .courierId(deliveryAssignment.getCourier().getId())
                .orderId(deliveryAssignment.getOrderId())
                .customerId(orderClient.getOrderById(request.getOrderId()).getCustomerId())
                .assignedAt(deliveryAssignment.getAssignedAt())
                .build();

        kafkaTemplate.send("delivery-assignment-topic", event);

        return deliveryAssignmentRepository.save(deliveryAssignment);
    }

    @Override
    @Transactional
    public DeliveryAssignment update(UpsertDeliveryAssignmentRequest request, Long id) {
        log.info("Call update in DeliveryAssignmentServiceImpl with request: {}, ID: {}", request, id);

        try {
            OrderDto order = orderClient.getOrderById(request.getOrderId());
        } catch (FeignException.NotFound e) {
            throw new EntityNotFoundException(MessageFormat.format("Order with ID: {0} not found", request.getOrderId()));
        }

        Courier courier = courierRepository.findById(request.getCourierId())
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Courier with ID: {0} not found", request.getCourierId())));

        DeliveryAssignment existedDeliveryAssignment = deliveryAssignmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("DeliveryAssignment with ID: {0} not found", id)));

        deliveryAssignmentMapper.updateRequestToDeliveryAssignment(request, existedDeliveryAssignment, courier);

        return deliveryAssignmentRepository.save(existedDeliveryAssignment);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        log.info("Call deleteById in DeliveryAssignmentServiceImpl with ID: {}", id);

        deliveryAssignmentRepository.deleteById(id);
    }

    @Override
    public DeliveryAssignment findByOrderId(Long orderId) {
        log.info("Call findByOrderId in DeliveryAssignmentServiceImpl with orderID: {}", orderId);

        return deliveryAssignmentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("DeliveryAssignment with order ID: {0} not found", orderId)));
    }
}
