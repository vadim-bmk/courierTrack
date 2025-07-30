package com.dvo.courier_service.service;

import com.dvo.courier_service.entity.Courier;
import com.dvo.courier_service.web.model.request.UpdateCourierRequest;
import com.dvo.courier_service.web.model.request.UpsertCourierRequest;

import java.util.List;

public interface CourierService {
    List<Courier> findAll();

    Courier findById(Long id);

    Courier create(UpsertCourierRequest request);

    Courier update(UpdateCourierRequest request, Long id);

    void deleteById(Long id);

    void setCurrentStatus(Long id, String status);
}
