package com.dvo.user_service.web.controller;

import com.dvo.user_service.mapper.UserMapper;
import com.dvo.user_service.service.UserService;
import com.dvo.user_service.web.model.request.UpdateUserRequest;
import com.dvo.user_service.web.model.request.UpsertUserRequest;
import com.dvo.user_service.web.model.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all users")
    public ResponseEntity<List<UserResponse>> findAll() {
        return ResponseEntity.ok(
                userService.findAll()
                        .stream()
                        .map(userMapper::userToResponse)
                        .toList());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get user by ID")
    public ResponseEntity<UserResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(
                userMapper.userToResponse(
                        userService.findById(id)));
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create user")
    public ResponseEntity<UserResponse> create(@RequestBody @Valid UpsertUserRequest request) {
        return ResponseEntity.ok(
                userMapper.userToResponse(
                        userService.create(
                                userMapper.requestToUser(request)
                        )
                ));
    }

    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update user")
    public ResponseEntity<UserResponse> update(@RequestBody @Valid UpdateUserRequest request,
                                               @PathVariable Long id) {
        return ResponseEntity.ok(
                userMapper.userToResponse(
                        userService.update(request, id)
                )
        );
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete user by ID")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
