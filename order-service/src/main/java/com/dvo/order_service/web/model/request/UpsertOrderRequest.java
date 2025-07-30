package com.dvo.order_service.web.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UpsertOrderRequest {
    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Delivery address is required")
    private String deliveryAddress;
}
