package com.dvo.courier_service.mapper;

import com.dvo.courier_service.entity.Courier;
import com.dvo.courier_service.entity.DeliveryAssignment;
import com.dvo.courier_service.web.model.request.UpsertDeliveryAssignmentRequest;
import com.dvo.courier_service.web.model.response.DeliveryAssignmentResponse;
import com.dvo.courier_service.web.model.response.DeliveryAssignmentShortResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface DeliveryAssignmentMapper {

    DeliveryAssignmentShortResponse deliveryAssignmentToShortResponse(DeliveryAssignment deliveryAssignment);

    @Mapping(target = "courierId", source = "courier.id")
    DeliveryAssignmentResponse deliveryAssignmentToResponse(DeliveryAssignment deliveryAssignment);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "assignedAt", ignore = true)
    @Mapping(target = "courier", source = "courier")
    DeliveryAssignment requestToDeliveryAssignment(UpsertDeliveryAssignmentRequest request, Courier courier);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "courier", source = "courier")
    @Mapping(target = "assignedAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    void updateRequestToDeliveryAssignment(UpsertDeliveryAssignmentRequest request, @MappingTarget DeliveryAssignment deliveryAssignment, Courier courier);
}
