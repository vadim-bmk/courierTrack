package com.dvo.courier_service.mapper;

import com.dvo.courier_service.entity.Courier;
import com.dvo.courier_service.web.model.request.UpdateCourierRequest;
import com.dvo.courier_service.web.model.request.UpsertCourierRequest;
import com.dvo.courier_service.web.model.response.CourierResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE, uses = {DeliveryAssignmentMapper.class})
public interface CourierMapper {
    CourierResponse courierToResponse(Courier courier);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "assignments", ignore = true)
    Courier requestToCourier(UpsertCourierRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "assignments", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateRequestToCourier(UpdateCourierRequest request, @MappingTarget Courier courier);
}
