package com.dvo.courier_service.service.impl;

import com.dvo.courier_service.client.UserClient;
import com.dvo.courier_service.client.dto.UserDto;
import com.dvo.courier_service.entity.Courier;
import com.dvo.courier_service.entity.CourierStatus;
import com.dvo.courier_service.exception.EntityExistsException;
import com.dvo.courier_service.exception.EntityNotFoundException;
import com.dvo.courier_service.mapper.CourierMapper;
import com.dvo.courier_service.repository.CourierRepository;
import com.dvo.courier_service.repository.DeliveryAssignmentRepository;
import com.dvo.courier_service.web.model.request.UpdateCourierRequest;
import com.dvo.courier_service.web.model.request.UpsertCourierRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CourierServiceImplTest {
    @InjectMocks
    private CourierServiceImpl courierService;

    @Mock
    private CourierRepository courierRepository;

    @Mock
    private DeliveryAssignmentRepository deliveryAssignmentRepository;

    @Mock
    private CourierMapper courierMapper;

    @Mock
    private UserClient userClient;

    private Courier courier;

    @BeforeEach
    void setUp() {
        courier = Courier.builder()
                .id(1L)
                .currentStatus(CourierStatus.AVAILABLE)
                .build();
    }

    @Test
    void testFindAll() {
        when(courierRepository.findAll()).thenReturn(List.of(courier));
        List<Courier> result = courierService.findAll();

        assertEquals(1, result.size());
        assertEquals(1L, result.getFirst().getId());
        verify(courierRepository).findAll();
    }

    @Test
    void testFindById() {
        when(courierRepository.findById(1L)).thenReturn(Optional.of(courier));

        Courier result = courierService.findById(1L);

        assertEquals(courier, result);
        verify(courierRepository).findById(1L);
    }

    @Test
    void testFindById_whenNotFound() {
        when(courierRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> courierService.findById(1L));
    }

    @Test
    void testCreate() {
        UpsertCourierRequest request = UpsertCourierRequest.builder()
                .userId(1L)
                .build();

        when(userClient.getUserById(1L)).thenReturn(new UserDto());
        when(courierRepository.existsByUserId(1L)).thenReturn(false);
        when(courierMapper.requestToCourier(request)).thenReturn(courier);
        when(courierRepository.save(courier)).thenReturn(courier);

        Courier result = courierService.create(request);

        assertEquals(courier, result);
        verify(userClient).getUserById(1L);
        verify(courierRepository).existsByUserId(1L);
        verify(courierMapper).requestToCourier(request);
        verify(courierRepository).save(courier);
    }

    @Test
    void testCreate_whenUserClientNotFound() {
        UpsertCourierRequest request = UpsertCourierRequest.builder()
                .userId(1L)
                .build();

        when(userClient.getUserById(1L)).thenThrow(EntityNotFoundException.class);
        assertThrows(EntityNotFoundException.class, () -> courierService.create(request));
        verify(courierRepository, never()).save(any(Courier.class));
    }

    @Test
    void testCreate_whenUserAlreadyExists() {
        UpsertCourierRequest request = UpsertCourierRequest.builder()
                .userId(1L)
                .build();
        when(userClient.getUserById(1L)).thenReturn(new UserDto());
        when(courierRepository.existsByUserId(1L)).thenReturn(true);

        assertThrows(EntityExistsException.class, () -> courierService.create(request));
        verify(courierRepository, never()).save(any(Courier.class));
    }

    @Test
    void testUpdate() {
        UpdateCourierRequest request = UpdateCourierRequest.builder()
                .userId(1L)
                .build();
        when(userClient.getUserById(1L)).thenReturn(new UserDto());
        when(courierRepository.findById(1L)).thenReturn(Optional.of(courier));
        when(courierRepository.existsByUserId(1L)).thenReturn(false);
        doNothing().when(courierMapper).updateRequestToCourier(request, courier);
        when(courierRepository.save(courier)).thenReturn(courier);

        Courier result = courierService.update(request, 1L);

        assertEquals(courier, result);
        verify(userClient).getUserById(1L);
        verify(courierRepository).findById(1L);
        verify(courierRepository).existsByUserId(1L);
        verify(courierRepository).save(courier);
    }

    @Test
    void testUpdate_whenUserClientNotFound() {
        UpdateCourierRequest request = UpdateCourierRequest.builder()
                .userId(1L)
                .build();

        when(userClient.getUserById(1L)).thenThrow(EntityNotFoundException.class);
        assertThrows(EntityNotFoundException.class, () -> courierService.update(request, 1L));
        verify(courierRepository, never()).save(any(Courier.class));
    }

    @Test
    void testUpdate_whenCourierNotFound() {
        UpdateCourierRequest request = UpdateCourierRequest.builder()
                .userId(1L)
                .build();

        when(userClient.getUserById(1L)).thenReturn(new UserDto());
        when(courierRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> courierService.update(request, 1L));
        verify(courierRepository, never()).save(any(Courier.class));
    }

    @Test
    void testUpdate_whenUserAlreadyExists() {
        UpdateCourierRequest request = UpdateCourierRequest.builder()
                .userId(1L)
                .build();

        when(userClient.getUserById(1L)).thenReturn(new UserDto());
        when(courierRepository.findById(1L)).thenReturn(Optional.of(courier));
        when(courierRepository.existsByUserId(1L)).thenReturn(true);

        assertThrows(EntityExistsException.class, () -> courierService.update(request, 1L));
    }

    @Test
    void testDeleteById() {
        courierService.deleteById(1L);

        verify(courierRepository).deleteById(1L);
    }

    @Test
    void testSetCurrentStatus() {
        when(courierRepository.findById(1L)).thenReturn(Optional.of(courier));
        when(courierRepository.save(courier)).thenReturn(courier);

        courierService.setCurrentStatus(1L, "UNAVAILABLE");

        assertEquals(CourierStatus.UNAVAILABLE, courier.getCurrentStatus());
        verify(courierRepository).findById(1L);
        verify(courierRepository).save(courier);
    }

    @Test
    void testSetCurrentStatus_whenCourierNotFound() {
        when(courierRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> courierService.setCurrentStatus(1L, "UNAVAILABLE"));
        verify(courierRepository, never()).save(any(Courier.class));
    }
}
