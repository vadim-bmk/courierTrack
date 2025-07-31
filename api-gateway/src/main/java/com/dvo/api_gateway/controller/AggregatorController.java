package com.dvo.api_gateway.controller;

import com.dvo.api_gateway.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AggregatorController {
    private final WebClient webClient;

    @Value("${order-service.url}")
    private String orderServiceUrl;

    @Value("${user-service.url}")
    private String userServiceUrl;

    @Value("${courier-service.url}")
    private String courierServiceUrl;

    @GetMapping("/order-details/{orderId}")
    @Operation(summary = "Get order details")
    public Mono<ResponseEntity> getOrderDetails(@PathVariable Long orderId) {
        return webClient.get()
                .uri(orderServiceUrl + "/api/orders/" + orderId)
                .retrieve()
                .onStatus(status-> status.value()==404,
                        response -> Mono.error(new RuntimeException("Order not found")))
                .bodyToMono(OrderDto.class)
                .flatMap(order -> {
                    Mono<UserDto> userDto = webClient.get()
                            .uri(userServiceUrl + "/api/users/" + order.getCustomerId())
                            .retrieve()
                            .bodyToMono(UserDto.class)
                            .onErrorResume(ex->Mono.empty());

                    Mono<DeliveryAssignmentDto> deliveryAssignmentDto = webClient.get()
                            .uri(courierServiceUrl + "/api/deliveryAssignments/order/" + order.getId())
                            .retrieve()
                            .bodyToMono(DeliveryAssignmentDto.class)
                            .onErrorResume(ex->Mono.empty());

                    return Mono.zip(userDto.defaultIfEmpty(new UserDto()), deliveryAssignmentDto.defaultIfEmpty(new DeliveryAssignmentDto()))
                            .map(tuple -> {
                                        UserDto user = tuple.getT1();
                                        DeliveryAssignmentDto delivery = tuple.getT2();

                                        OrderDetailsResponse response = OrderDetailsResponse.builder()
                                                .orderId(order.getId())
                                                .description(order.getDescription())
                                                .deliveryAddress(order.getDeliveryAddress())
                                                .status(order.getStatus())
                                                .createdAt(order.getCreatedAt())
                                                .customerId(user.getId())
                                                .customerUsername(user.getUsername())
                                                .customerEmail(user.getEmail())
                                                .courierId(delivery.getCourierId())
                                                .assignedAt(delivery.getAssignedAt())
                                                .build();

                                        return ResponseEntity.ok(response);
                                    }
                            )
                            .cast(ResponseEntity.class);
                })
                .onErrorResume(ex->{
                    ErrorResponse error = new ErrorResponse(ex.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(error));
                })
                ;
    }
}
