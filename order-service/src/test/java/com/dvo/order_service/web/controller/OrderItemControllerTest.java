package com.dvo.order_service.web.controller;

import com.dvo.order_service.entity.OrderItem;
import com.dvo.order_service.mapper.OrderItemMapper;
import com.dvo.order_service.service.OrderItemService;
import com.dvo.order_service.web.model.request.UpdateOrderItemRequest;
import com.dvo.order_service.web.model.request.UpsertOrderItemRequest;
import com.dvo.order_service.web.model.response.OrderItemResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderItemController.class)
public class OrderItemControllerTest {
    @MockBean
    private OrderItemService orderItemService;

    @MockBean
    private OrderItemMapper orderItemMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private OrderItem orderItem;
    private OrderItemResponse orderItemResponse;
    private final String URL = "/api/orderItems";

    @BeforeEach
    void setUp() {
        orderItem = OrderItem.builder()
                .id(1L)
                .productName("product")
                .build();

        orderItemResponse = OrderItemResponse.builder()
                .id(1L)
                .productName("product")
                .build();
    }

    @Test
    void testFindAll() throws Exception {
        when(orderItemService.findAll()).thenReturn(List.of(orderItem));
        when(orderItemMapper.orderItemToResponse(orderItem)).thenReturn(orderItemResponse);

        mockMvc.perform(get(URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(orderItemResponse.getId()));

        verify(orderItemService).findAll();
        verify(orderItemMapper).orderItemToResponse(orderItem);
    }

    @Test
    void testFindById() throws Exception {
        when(orderItemService.findById(1L)).thenReturn(orderItem);
        when(orderItemMapper.orderItemToResponse(orderItem)).thenReturn(orderItemResponse);

        mockMvc.perform(get(URL + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(orderItemResponse.getId()));

        verify(orderItemService).findById(1L);
        verify(orderItemMapper).orderItemToResponse(orderItem);
    }

    @Test
    void testCreate() throws Exception {
        UpsertOrderItemRequest request = UpsertOrderItemRequest.builder()
                .productName("product")
                .quantity(1)
                .price(BigDecimal.valueOf(100))
                .build();

        when(orderItemMapper.requestToOrderItem(request)).thenReturn(orderItem);
        when(orderItemService.create(orderItem)).thenReturn(orderItem);
        when(orderItemMapper.orderItemToResponse(orderItem)).thenReturn(orderItemResponse);

        mockMvc.perform(post(URL + "/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(orderItemResponse.getId()));

        verify(orderItemMapper).requestToOrderItem(request);
        verify(orderItemService).create(orderItem);
        verify(orderItemMapper).orderItemToResponse(orderItem);
    }

    @Test
    void testUpdate() throws Exception {
        UpdateOrderItemRequest request = UpdateOrderItemRequest.builder()
                .productName("product")
                .build();

        when(orderItemService.update(request, 1L)).thenReturn(orderItem);
        when(orderItemMapper.orderItemToResponse(orderItem)).thenReturn(orderItemResponse);

        mockMvc.perform(put(URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(orderItemResponse.getId()));

        verify(orderItemService).update(request, 1L);
        verify(orderItemMapper).orderItemToResponse(orderItem);
    }

    @Test
    void testDeleteById() throws Exception {
        doNothing().when(orderItemService).deleteById(1L);

        mockMvc.perform(delete(URL + "/1"))
                .andExpect(status().isNoContent());
    }
}
