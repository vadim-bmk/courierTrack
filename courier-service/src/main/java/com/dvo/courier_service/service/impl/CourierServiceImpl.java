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
import com.dvo.courier_service.service.CourierService;
import com.dvo.courier_service.web.model.request.UpdateCourierRequest;
import com.dvo.courier_service.web.model.request.UpsertCourierRequest;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourierServiceImpl implements CourierService {
    private final CourierRepository courierRepository;
    private final DeliveryAssignmentRepository deliveryAssignmentRepository;
    private final CourierMapper courierMapper;
    private final UserClient userClient;

    @Override
    public List<Courier> findAll() {
        log.info("Call findAll in CourierServiceImpl");

        return courierRepository.findAll();
    }

    @Override
    public Courier findById(Long id) {
        log.info("Call findById in CourierServiceImpl with ID: {}", id);

        return courierRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Courier with ID: {0} not found", id)));
    }

    @Override
    @Transactional
    public Courier create(UpsertCourierRequest request) {
        log.info("Call create in CourierServiceImpl with request: {}", request);

        try {
            UserDto user = userClient.getUserById(request.getUserId());
        } catch (FeignException.NotFound e) {
            throw new EntityNotFoundException(MessageFormat.format("User with ID: {0} not found", request.getUserId()));
        }

        if (courierRepository.existsByUserId(request.getUserId())) {
            throw new EntityExistsException(MessageFormat.format("Courier with User ID: {0} already exists", request.getUserId()));
        }

        Courier courier = courierMapper.requestToCourier(request);

        return courierRepository.save(courier);
    }

    @Override
    @Transactional
    public Courier update(UpdateCourierRequest request, Long id) {
        log.info("Call update in CourierServiceImpl for ID: {}, with request: {}", id, request);

        try {
            UserDto user = userClient.getUserById(request.getUserId());
        } catch (FeignException.NotFound e) {
            throw new EntityNotFoundException(MessageFormat.format("User with ID: {0} not found", request.getUserId()));
        }

        Courier existedCourier = courierRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Courier with ID: {0} not found", id)));

        if (courierRepository.existsByUserId(request.getUserId()) && !Objects.equals(existedCourier.getUserId(), request.getUserId())) {
            throw new EntityExistsException(MessageFormat.format("Courier with User ID: {0} already exists", request.getUserId()));
        }

        courierMapper.updateRequestToCourier(request, existedCourier);

        return courierRepository.save(existedCourier);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        log.info("Call deleteById in CourierServiceImpl with ID: {}", id);

        courierRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void setCurrentStatus(Long id, String status) {
        log.info("Call setCurrentStatus in CourierServiceImpl for ID: {}, with status: {}", id, status);

        Courier courier = courierRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Courier with ID: {0} not found", id)));

        courier.setCurrentStatus(CourierStatus.valueOf(status));

        courierRepository.save(courier);
    }
}
