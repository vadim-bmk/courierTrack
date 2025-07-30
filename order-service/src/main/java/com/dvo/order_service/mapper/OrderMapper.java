package com.dvo.order_service.mapper;

import com.dvo.order_service.entity.Order;
import com.dvo.order_service.web.model.request.UpsertOrderRequest;
import com.dvo.order_service.web.model.response.OrderResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE, uses = OrderItemMapper.class  )
public interface OrderMapper {
    OrderResponse orderToResponse(Order order);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orderItems", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    Order requestToOrder(UpsertOrderRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orderItems", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    void updateRequestToOrder(UpsertOrderRequest request, @MappingTarget Order order);
}
