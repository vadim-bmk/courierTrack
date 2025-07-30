package com.dvo.courier_service.web.model.request;

import com.dvo.courier_service.validation.ValidCourierStatus;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateCourierRequest {
    @Min(value = 1, message = "User ID must be greater than 0")
    private Long userId;
    private String region;
    @ValidCourierStatus
    private String currentStatus;
}
