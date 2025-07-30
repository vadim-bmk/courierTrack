package com.dvo.order_service.web.controller;

import com.dvo.order_service.mapper.OrderItemMapper;
import com.dvo.order_service.service.OrderItemService;
import com.dvo.order_service.web.model.request.UpdateOrderItemRequest;
import com.dvo.order_service.web.model.request.UpsertOrderItemRequest;
import com.dvo.order_service.web.model.response.OrderItemResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orderItems")
@RequiredArgsConstructor
public class OrderItemController {
    private final OrderItemService orderItemService;
    private final OrderItemMapper orderItemMapper;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all order items")
    public ResponseEntity<List<OrderItemResponse>> findAll() {
        return ResponseEntity.ok(
                orderItemService.findAll()
                        .stream()
                        .map(orderItemMapper::orderItemToResponse)
                        .toList()
        );
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get order item by ID")
    public ResponseEntity<OrderItemResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(
                orderItemMapper.orderItemToResponse(
                        orderItemService.findById(id)
                )
        );
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create order item")
    public ResponseEntity<OrderItemResponse> create(@RequestBody @Valid UpsertOrderItemRequest request) {
        return ResponseEntity.ok(
                orderItemMapper.orderItemToResponse(
                        orderItemService.create(
                                orderItemMapper.requestToOrderItem(request)
                        )
                )
        );
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update order item")
    public ResponseEntity<OrderItemResponse> update(@RequestBody @Valid UpdateOrderItemRequest request,
                                                    @PathVariable Long id) {
        return ResponseEntity.ok(
                orderItemMapper.orderItemToResponse(
                        orderItemService.update(request, id)
                )
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete order item by ID")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        orderItemService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
