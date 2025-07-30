package com.dvo.courier_service.web.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpsertDeliveryAssignmentRequest {
    @NotNull(message = "Courier ID is required")
    private Long courierId;

    @NotNull(message = "Order ID is required")
    private Long orderId;
}
