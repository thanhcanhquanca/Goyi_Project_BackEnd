package com.example.goyimanagementbackend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SignupRequest {
    private String phoneNumber;
    private String password;
    private String userName;
    private String email;
}
