package com.dvo.courier_service.web.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourierResponse {
    private Long id;
    private Long userId;
    private String region;
    private String currentStatus;
    private List<DeliveryAssignmentShortResponse> assignments;
}
