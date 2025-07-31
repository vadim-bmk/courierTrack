package com.dvo.user_service.web.controller;

import com.dvo.user_service.entity.User;
import com.dvo.user_service.mapper.UserMapper;
import com.dvo.user_service.service.UserService;
import com.dvo.user_service.web.model.request.UpdateUserRequest;
import com.dvo.user_service.web.model.request.UpsertUserRequest;
import com.dvo.user_service.web.model.response.UserResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {
    @MockBean
    private UserService userService;

    @MockBean
    private UserMapper userMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;
    private UserResponse userResponse;
    private final String URL = "/api/users";

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .username("user")
                .email("email@mail.ru")
                .build();

        userResponse = UserResponse.builder()
                .id(1L)
                .username("user")
                .email("email@mail.ru")
                .build();
    }

    @Test
    void testFindAll() throws Exception {
        when(userService.findAll()).thenReturn(List.of(user));
        when(userMapper.userToResponse(user)).thenReturn(userResponse);

        mockMvc.perform(get(URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(1L));

        verify(userService).findAll();
        verify(userMapper).userToResponse(user);
    }

    @Test
    void testFindById() throws Exception {
        when(userService.findById(1L)).thenReturn(user);
        when(userMapper.userToResponse(user)).thenReturn(userResponse);

        mockMvc.perform(get(URL + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(userService).findById(1L);
        verify(userMapper).userToResponse(user);
    }

    @Test
    void testCreate() throws Exception {
        UpsertUserRequest request = UpsertUserRequest.builder()
                .username("user")
                .email("email@mail.ru")
                .roleType("USER")
                .build();
        when(userMapper.requestToUser(request)).thenReturn(user);
        when(userService.create(user)).thenReturn(user);
        when(userMapper.userToResponse(user)).thenReturn(userResponse);

        mockMvc.perform(post(URL + "/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(userMapper).requestToUser(request);
        verify(userService).create(user);
        verify(userMapper).userToResponse(user);
    }

    @Test
    void testUpdate() throws Exception {
        UpdateUserRequest request = UpdateUserRequest.builder()
                .email("new@mail.ru")
                .roleType("USER")
                .build();

        when(userService.update(request, 1L)).thenReturn(user);
        when(userMapper.userToResponse(user)).thenReturn(userResponse);

        mockMvc.perform(put(URL + "/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(userService).update(request, 1L);
        verify(userMapper).userToResponse(user);
    }

    @Test
    void testDeleteById() throws Exception {
        doNothing().when(userService).deleteById(1L);

        mockMvc.perform(delete(URL + "/delete/1"))
                .andExpect(status().isNoContent());
    }
}
