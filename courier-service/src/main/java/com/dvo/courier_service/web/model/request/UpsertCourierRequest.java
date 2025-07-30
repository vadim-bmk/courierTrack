package com.dvo.courier_service.web.model.request;

import com.dvo.courier_service.validation.ValidCourierStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpsertCourierRequest {
    @NotNull(message = "User ID is required")
    @Min(value = 1, message = "User ID must be greater than 0")
    private Long userId;

    @NotBlank(message = "Region is required")
    private String region;

    @NotBlank(message = "Current status is required")
    @ValidCourierStatus
    private String currentStatus;

}
