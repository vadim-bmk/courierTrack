package com.dvo.order_service.web.controller;

import com.dvo.order_service.entity.OrderStatus;
import com.dvo.order_service.mapper.OrderMapper;
import com.dvo.order_service.service.OrderService;
import com.dvo.order_service.validation.ValidOrderStatus;
import com.dvo.order_service.web.model.request.UpsertOrderRequest;
import com.dvo.order_service.web.model.response.OrderResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all orders")
    public ResponseEntity<List<OrderResponse>> findAll() {
        return ResponseEntity.ok(
                orderService.findAll()
                        .stream()
                        .map(orderMapper::orderToResponse)
                        .toList()
        );
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get order by ID")
    public ResponseEntity<OrderResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(
                orderMapper.orderToResponse(
                        orderService.findById(id)
                )
        );
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create order")
    public ResponseEntity<OrderResponse> create(@RequestBody @Valid UpsertOrderRequest request) {
        return ResponseEntity.ok(
                orderMapper.orderToResponse(
                        orderService.create(
                                orderMapper.requestToOrder(request)
                        )
                )
        );
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update order")
    public ResponseEntity<OrderResponse> update(@RequestBody @Valid UpsertOrderRequest request,
                                                @PathVariable Long id) {
        return ResponseEntity.ok(
                orderMapper.orderToResponse(
                        orderService.update(request, id)
                )
        );
    }


    @PutMapping("/{id}/status")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Set order status")
    public ResponseEntity<Void> setOrderStatus(@PathVariable Long id,
                                               @RequestParam @ValidOrderStatus String status) {
        orderService.setOrderStatus(id, OrderStatus.valueOf(status));

        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/orderItem")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Add order item to order")
    public ResponseEntity<Void> addOrderItem(@PathVariable Long id,
                                             @RequestParam Long orderItemId) {
        orderService.addOrderItem(id, orderItemId);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/delete/orderItem")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Delete order item from order")
    public ResponseEntity<Void> deleteOrderItemFromOrder(@PathVariable Long id,
                                                         @RequestParam Long orderItemId) {
        orderService.deleteOrderItemFromOrder(id, orderItemId);

        return ResponseEntity.ok().build();
    }
}
