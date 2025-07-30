package com.dvo.order_service.web.model.request;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateOrderItemRequest {
    private String productName;

    @Min(value = 1, message = "Quantity must be greater than 0")
    private Integer quantity;

    @Min(value = 0, message = "Price must be greater than or equal to 0")
    private BigDecimal price;
}
