package com.app_api.auth;

import com.app_api.resource.enums.UserRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String email;
    private String password;
    private UserRoleEnum role;
    private String name;
    private String phone;
    private Integer sellerId;
}
