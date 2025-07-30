package com.dvo.courier_service.web.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeliveryAssignmentShortResponse {
    private Long id;
    private Long orderId;
    private LocalDateTime assignedAt;
}
