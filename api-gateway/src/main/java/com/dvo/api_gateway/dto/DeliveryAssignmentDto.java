package com.dvo.api_gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeliveryAssignmentDto {
    private Long id;
    private Long courierId;
    private Long orderId;
    private LocalDateTime assignedAt;
}
