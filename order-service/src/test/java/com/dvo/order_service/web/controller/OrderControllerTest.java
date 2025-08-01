package com.dvo.order_service.web.controller;

import com.dvo.order_service.entity.Order;
import com.dvo.order_service.entity.OrderStatus;
import com.dvo.order_service.mapper.OrderMapper;
import com.dvo.order_service.service.OrderService;
import com.dvo.order_service.web.model.request.UpsertOrderRequest;
import com.dvo.order_service.web.model.response.OrderResponse;
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

@WebMvcTest(OrderController.class)
public class OrderControllerTest {
    @MockBean
    private OrderService orderService;

    @MockBean
    private OrderMapper orderMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private OrderResponse orderResponse;
    private Order order;
    private final String URL = "/api/orders";

    @BeforeEach
    void setUp() {
        order = Order.builder()
                .id(1L)
                .description("description")
                .build();

        orderResponse = OrderResponse.builder()
                .id(1L)
                .description("description")
                .build();
    }

    @Test
    void testFindAll() throws Exception {
        when(orderService.findAll()).thenReturn(List.of(order));
        when(orderMapper.orderToResponse(order)).thenReturn(orderResponse);

        mockMvc.perform(get(URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(1L));

        verify(orderService).findAll();
        verify(orderMapper).orderToResponse(order);
    }

    @Test
    void testFindById() throws Exception {
        when(orderService.findById(1L)).thenReturn(order);
        when(orderMapper.orderToResponse(order)).thenReturn(orderResponse);

        mockMvc.perform(get(URL + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(orderService).findById(1L);
        verify(orderMapper).orderToResponse(order);
    }

    @Test
    void testCreate() throws Exception {
        UpsertOrderRequest request = UpsertOrderRequest.builder()
                .description("description")
                .deliveryAddress("address")
                .customerId(1L)
                .build();
        when(orderMapper.requestToOrder(request)).thenReturn(order);
        when(orderService.create(order)).thenReturn(order);
        when(orderMapper.orderToResponse(order)).thenReturn(orderResponse);

        mockMvc.perform(post(URL + "/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(orderMapper).requestToOrder(request);
        verify(orderService).create(order);
        verify(orderMapper).orderToResponse(order);
    }

    @Test
    void testUpdate() throws Exception {
        UpsertOrderRequest request = UpsertOrderRequest.builder()
                .description("description")
                .deliveryAddress("address")
                .customerId(1L)
                .build();

        when(orderMapper.orderToResponse(order)).thenReturn(orderResponse);
        when(orderService.update(request, 1L)).thenReturn(order);

        mockMvc.perform(put(URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(orderMapper).orderToResponse(order);
        verify(orderService).update(request, 1L);
    }

    @Test
    void testSetOrderStatus() throws Exception {
        doNothing().when(orderService).setOrderStatus(1L, OrderStatus.CANCELED);
        mockMvc.perform(put(URL + "/1/status")
                        .param("status", "CANCELED"))
                .andExpect(status().isOk());
    }

    @Test
    void testAddOrderItem() throws Exception {
        doNothing().when(orderService).addOrderItem(1L, 1L);
        mockMvc.perform(put(URL + "/1/orderItem")
                        .param("orderItemId", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteOrderItemFromOrder() throws Exception {
        doNothing().when(orderService).deleteOrderItemFromOrder(1L, 1L);
        mockMvc.perform(put(URL + "/1/delete/orderItem")
                        .param("orderItemId", "1"))
                .andExpect(status().isOk());
    }
}
