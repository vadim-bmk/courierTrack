package com.dvo.api_gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ErrorResponse {
    private String message;
    private Map<String, String> details;

    public ErrorResponse(String message) {
        this.message = message;
    }
}
