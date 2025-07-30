package com.dvo.courier_service.service;

import com.dvo.courier_service.entity.DeliveryAssignment;
import com.dvo.courier_service.web.model.request.UpsertDeliveryAssignmentRequest;

import java.util.List;

public interface DeliveryAssignmentService {
    List<DeliveryAssignment> findAll();

    DeliveryAssignment findById(Long id);

    DeliveryAssignment create(UpsertDeliveryAssignmentRequest request);

    DeliveryAssignment update(UpsertDeliveryAssignmentRequest request, Long id);

    void deleteById(Long id);
}
