package com.dvo.courier_service.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDto {
    private Long id;
    private Long customerId;
    private String description;
    private List<OrderItemDto> orderItems;
    private String deliveryAddress;
    private String status;
    private LocalDateTime createdAt;
}
