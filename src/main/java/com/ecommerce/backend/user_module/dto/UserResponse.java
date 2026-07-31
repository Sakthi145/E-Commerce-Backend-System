package com.ecommerce.backend.user_module.dto;

import com.ecommerce.backend.user_module.models.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class UserResponse {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private Role role;

    private LocalDateTime createdAt;

}