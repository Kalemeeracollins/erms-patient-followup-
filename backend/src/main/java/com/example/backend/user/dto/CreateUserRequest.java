package com.example.backend.user.dto;

import lombok.Data;

@Data
public class CreateUserRequest {
    private String username;
    private String password;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String role;
}
