package com.dvo.user_service.mapper;

import com.dvo.user_service.entity.User;
import com.dvo.user_service.web.model.request.UpdateUserRequest;
import com.dvo.user_service.web.model.request.UpsertUserRequest;
import com.dvo.user_service.web.model.response.UserResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    UserResponse userToResponse(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    User requestToUser(UpsertUserRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "username", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateRequestToUser(UpdateUserRequest request, @MappingTarget User user);
}
