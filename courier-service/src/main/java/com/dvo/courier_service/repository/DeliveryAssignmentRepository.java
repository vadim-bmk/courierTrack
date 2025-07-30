package com.dvo.courier_service.repository;

import com.dvo.courier_service.entity.DeliveryAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryAssignmentRepository extends JpaRepository<DeliveryAssignment, Long> {
}
