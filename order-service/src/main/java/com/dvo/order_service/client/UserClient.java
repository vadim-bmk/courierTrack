package com.dvo.order_service.client;

import com.dvo.order_service.client.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(name = "user-service", url = "${user-service.url}")
public interface UserClient {
    @GetMapping("/api/users/{id}")
    UserDto getUserById(@PathVariable Long id);
}
