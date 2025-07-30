package com.dvo.order_service.mapper;

import com.dvo.order_service.entity.OrderItem;
import com.dvo.order_service.web.model.request.UpdateOrderItemRequest;
import com.dvo.order_service.web.model.request.UpsertOrderItemRequest;
import com.dvo.order_service.web.model.response.OrderItemResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface OrderItemMapper {
    @Mapping(source = "order.id", target = "orderId")
    OrderItemResponse orderItemToResponse(OrderItem orderItem);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", ignore = true)
    OrderItem requestToOrderItem(UpsertOrderItemRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateRequestToOrderItem(UpdateOrderItemRequest request, @MappingTarget OrderItem orderItem);
}
