package com.dvo.order_service.web.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UpsertOrderRequest {
    @NotNull(message = "Customer ID is required")
    private Long customerId;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Delivery address is required")
    private String deliveryAddress;
}
