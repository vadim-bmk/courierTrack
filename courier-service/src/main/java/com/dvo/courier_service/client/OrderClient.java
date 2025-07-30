package com.dvo.courier_service.client;

import com.dvo.courier_service.client.dto.OrderDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "order-service", url = "${order-service.url}")
public interface OrderClient {
    @GetMapping("/api/orders/{id}")
    OrderDto getOrderById(@PathVariable Long id);
}
