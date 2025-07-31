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
public class OrderDetailsResponse {
    private Long orderId;
    private String description;
    private String deliveryAddress;
    private String status;
    private LocalDateTime createdAt;
    private Long customerId;
    private String customerUsername;
    private String customerEmail;
    private Long courierId;
    private LocalDateTime assignedAt;
}
