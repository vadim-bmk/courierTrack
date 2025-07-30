package com.dvo.courier_service.web.controller;

import com.dvo.courier_service.mapper.CourierMapper;
import com.dvo.courier_service.service.CourierService;
import com.dvo.courier_service.web.model.request.UpdateCourierRequest;
import com.dvo.courier_service.web.model.request.UpsertCourierRequest;
import com.dvo.courier_service.web.model.response.CourierResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/couriers")
@RequiredArgsConstructor
public class CourierController {
    private final CourierService courierService;
    private final CourierMapper courierMapper;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all couriers")
    public ResponseEntity<List<CourierResponse>> findAll() {
        return ResponseEntity.ok(
                courierService.findAll()
                        .stream()
                        .map(courierMapper::courierToResponse)
                        .toList()
        );
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get courier by ID")
    public ResponseEntity<CourierResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(
                courierMapper.courierToResponse(
                        courierService.findById(id)
                )
        );
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create courier")
    public ResponseEntity<CourierResponse> create(@RequestBody @Valid UpsertCourierRequest request) {
        return ResponseEntity.ok(
                courierMapper.courierToResponse(
                        courierService.create(request)
                )
        );
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update courier")
    public ResponseEntity<CourierResponse> update(@RequestBody @Valid UpdateCourierRequest request,
                                                  @PathVariable Long id) {
        return ResponseEntity.ok(
                courierMapper.courierToResponse(
                        courierService.update(request, id)
                )
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete courier")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        courierService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
