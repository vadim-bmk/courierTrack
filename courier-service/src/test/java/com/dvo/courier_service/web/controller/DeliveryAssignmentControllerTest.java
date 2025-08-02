package com.dvo.courier_service.web.controller;

import com.dvo.courier_service.entity.DeliveryAssignment;
import com.dvo.courier_service.mapper.DeliveryAssignmentMapper;
import com.dvo.courier_service.service.DeliveryAssignmentService;
import com.dvo.courier_service.web.model.request.UpsertDeliveryAssignmentRequest;
import com.dvo.courier_service.web.model.response.DeliveryAssignmentResponse;
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

@WebMvcTest(DeliveryAssignmentController.class)
public class DeliveryAssignmentControllerTest {
    @MockBean
    private DeliveryAssignmentService deliveryAssignmentService;

    @MockBean
    private DeliveryAssignmentMapper deliveryAssignmentMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private DeliveryAssignment deliveryAssignment;
    private DeliveryAssignmentResponse response;
    private final String URL = "/api/deliveryAssignments";

    @BeforeEach
    void setUp() {
        deliveryAssignment = DeliveryAssignment.builder()
                .id(1L)
                .orderId(1L)
                .build();

        response = DeliveryAssignmentResponse.builder()
                .id(1L)
                .orderId(1L)
                .courierId(1L)
                .build();
    }

    @Test
    void testFindAll() throws Exception {
        when(deliveryAssignmentService.findAll()).thenReturn(List.of(deliveryAssignment));
        when(deliveryAssignmentMapper.deliveryAssignmentToResponse(deliveryAssignment)).thenReturn(response);

        mockMvc.perform(get(URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));

        verify(deliveryAssignmentService).findAll();
        verify(deliveryAssignmentMapper).deliveryAssignmentToResponse(deliveryAssignment);
    }

    @Test
    void testFindById() throws Exception {
        when(deliveryAssignmentService.findById(1L)).thenReturn(deliveryAssignment);
        when(deliveryAssignmentMapper.deliveryAssignmentToResponse(deliveryAssignment)).thenReturn(response);

        mockMvc.perform(get(URL + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(deliveryAssignmentService).findById(1L);
        verify(deliveryAssignmentMapper).deliveryAssignmentToResponse(deliveryAssignment);
    }

    @Test
    void testCreate() throws Exception {
        UpsertDeliveryAssignmentRequest request = UpsertDeliveryAssignmentRequest.builder()
                .orderId(1L)
                .courierId(1L)
                .build();

        when(deliveryAssignmentService.create(request)).thenReturn(deliveryAssignment);
        when(deliveryAssignmentMapper.deliveryAssignmentToResponse(deliveryAssignment)).thenReturn(response);

        mockMvc.perform(post(URL + "/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(deliveryAssignmentService).create(request);
        verify(deliveryAssignmentMapper).deliveryAssignmentToResponse(deliveryAssignment);
    }

    @Test
    void testUpdate() throws Exception {
        UpsertDeliveryAssignmentRequest request = UpsertDeliveryAssignmentRequest.builder()
                .courierId(1L)
                .orderId(1L)
                .build();

        when(deliveryAssignmentService.update(request, 1L)).thenReturn(deliveryAssignment);
        when(deliveryAssignmentMapper.deliveryAssignmentToResponse(deliveryAssignment)).thenReturn(response);

        mockMvc.perform(put(URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(deliveryAssignmentService).update(request, 1L);
        verify(deliveryAssignmentMapper).deliveryAssignmentToResponse(deliveryAssignment);
    }

    @Test
    void testDeleteById() throws Exception {
        mockMvc.perform(delete(URL + "/1"))
                .andExpect(status().isNoContent());

        verify(deliveryAssignmentService).deleteById(1L);
    }

    @Test
    void testFindByOrderId() throws Exception {
        when(deliveryAssignmentService.findByOrderId(1L)).thenReturn(deliveryAssignment);
        when(deliveryAssignmentMapper.deliveryAssignmentToResponse(deliveryAssignment)).thenReturn(response);

        mockMvc.perform(get(URL + "/order/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(deliveryAssignmentService).findByOrderId(1L);
        verify(deliveryAssignmentMapper).deliveryAssignmentToResponse(deliveryAssignment);
    }
}
