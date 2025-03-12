package com.authorization.spring_auth.dtos;

import com.authorization.spring_auth.models.Role;
import lombok.Data;

@Data
public class AuthRequest {
    private String username;
    private String email;
    private String password;
    private Role role;
}
