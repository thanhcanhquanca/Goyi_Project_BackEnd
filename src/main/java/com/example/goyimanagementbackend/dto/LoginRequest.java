package com.example.goyimanagementbackend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LoginRequest {
    private String phoneNumber;
    private String password;
}
