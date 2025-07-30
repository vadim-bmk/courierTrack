package com.dvo.user_service.web.model.request;

import com.dvo.user_service.validation.ValidRoleType;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateUserRequest {
    @Email
    private String email;
    private String phone;

    @ValidRoleType
    private String roleType;
}
