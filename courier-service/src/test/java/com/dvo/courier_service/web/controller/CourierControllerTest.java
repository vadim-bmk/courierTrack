package com.dvo.courier_service.web.controller;

import com.dvo.courier_service.entity.Courier;
import com.dvo.courier_service.entity.CourierStatus;
import com.dvo.courier_service.mapper.CourierMapper;
import com.dvo.courier_service.service.CourierService;
import com.dvo.courier_service.web.model.request.UpdateCourierRequest;
import com.dvo.courier_service.web.model.request.UpsertCourierRequest;
import com.dvo.courier_service.web.model.response.CourierResponse;
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

@WebMvcTest(CourierController.class)
public class CourierControllerTest {
    @MockBean
    private CourierService courierService;

    @MockBean
    private CourierMapper courierMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Courier courier;
    private CourierResponse courierResponse;
    private final String URL = "/api/couriers";

    @BeforeEach
    void setUp() {
        courier = Courier.builder()
                .id(1L)
                .userId(1L)
                .currentStatus(CourierStatus.AVAILABLE)
                .build();

        courierResponse = CourierResponse.builder()
                .id(1L)
                .userId(1L)
                .currentStatus(CourierStatus.AVAILABLE.name())
                .build();
    }

    @Test
    void testFindAll() throws Exception {
        when(courierService.findAll()).thenReturn(List.of(courier));
        when(courierMapper.courierToResponse(courier)).thenReturn(courierResponse);

        mockMvc.perform(get(URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));

        verify(courierService).findAll();
        verify(courierMapper).courierToResponse(courier);
    }

    @Test
    void testFindById() throws Exception {
        when(courierService.findById(1L)).thenReturn(courier);
        when(courierMapper.courierToResponse(courier)).thenReturn(courierResponse);

        mockMvc.perform(get(URL + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(courierService).findById(1L);
        verify(courierMapper).courierToResponse(courier);
    }

    @Test
    void testCreate() throws Exception {
        UpsertCourierRequest request = UpsertCourierRequest.builder()
                .userId(1L)
                .currentStatus(CourierStatus.AVAILABLE.toString())
                .region("moscow")
                .build();

        when(courierService.create(request)).thenReturn(courier);
        when(courierMapper.courierToResponse(courier)).thenReturn(courierResponse);

        mockMvc.perform(post(URL + "/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(courierService).create(request);
        verify(courierMapper).courierToResponse(courier);
    }

    @Test
    void testUpdate() throws Exception {
        UpdateCourierRequest request = UpdateCourierRequest.builder()
                .userId(1L)
                .currentStatus(CourierStatus.AVAILABLE.name())
                .build();

        when(courierService.update(request, 1L)).thenReturn(courier);
        when(courierMapper.courierToResponse(courier)).thenReturn(courierResponse);

        mockMvc.perform(put(URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(courierService).update(request, 1L);
        verify(courierMapper).courierToResponse(courier);
    }

    @Test
    void testDeleteById() throws Exception {
        mockMvc.perform(delete(URL + "/1"))
                .andExpect(status().isNoContent());

        verify(courierService).deleteById(1L);
    }
}
