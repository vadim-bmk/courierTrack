package com.dvo.notification_service.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeliveryAssignmentsEvent {
    private Long courierId;
    private Long orderId;
    private Long customerId;
    private LocalDateTime assignedAt;
}
