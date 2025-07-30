package com.dvo.order_service.web.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {
    private Long id;
    private Long customerId;
    private String description;
    private List<OrderItemResponse> orderItems;
    private String deliveryAddress;
    private String status;
    private LocalDateTime createdAt;
}
