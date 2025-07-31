package com.dvo.order_service.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderChangedOrderItem {
    private Long orderId;
    private Long customerId;
    private Long orderItemId;
    private String action;
}
