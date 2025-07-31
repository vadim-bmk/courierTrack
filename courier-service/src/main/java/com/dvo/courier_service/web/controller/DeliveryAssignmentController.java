package com.dvo.courier_service.web.controller;

import com.dvo.courier_service.mapper.DeliveryAssignmentMapper;
import com.dvo.courier_service.service.DeliveryAssignmentService;
import com.dvo.courier_service.web.model.request.UpsertDeliveryAssignmentRequest;
import com.dvo.courier_service.web.model.response.DeliveryAssignmentResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/deliveryAssignments")
@RequiredArgsConstructor
public class DeliveryAssignmentController {
    private final DeliveryAssignmentService deliveryAssignmentService;
    private final DeliveryAssignmentMapper deliveryAssignmentMapper;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all delivery assignments")
    public ResponseEntity<List<DeliveryAssignmentResponse>> findAll(){
        return ResponseEntity.ok(
                deliveryAssignmentService.findAll()
                        .stream()
                        .map(deliveryAssignmentMapper::deliveryAssignmentToResponse)
                        .toList()
        );
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get delivery assignment by ID")
    public ResponseEntity<DeliveryAssignmentResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(
                deliveryAssignmentMapper.deliveryAssignmentToResponse(
                        deliveryAssignmentService.findById(id)
                )
        );
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create delivery assignment")
    public ResponseEntity<DeliveryAssignmentResponse> create(@RequestBody @Valid UpsertDeliveryAssignmentRequest request){
        return ResponseEntity.ok(
                deliveryAssignmentMapper.deliveryAssignmentToResponse(
                        deliveryAssignmentService.create(request)
                )
        );
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update delivery assignment")
    public ResponseEntity<DeliveryAssignmentResponse> update(@RequestBody @Valid UpsertDeliveryAssignmentRequest request,
                                                             @PathVariable Long id){
        return ResponseEntity.ok(
                deliveryAssignmentMapper.deliveryAssignmentToResponse(
                        deliveryAssignmentService.update(request, id)
                )
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete delivery assignment")
    public ResponseEntity<Void> deleteById(@PathVariable Long id){
        deliveryAssignmentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/order/{orderId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get delivery assignment by order ID")
    public ResponseEntity<DeliveryAssignmentResponse> findByOrderId(@PathVariable Long orderId){
        return ResponseEntity.ok(
                deliveryAssignmentMapper.deliveryAssignmentToResponse(
                        deliveryAssignmentService.findByOrderId(orderId)
                )
        );
    }
}
