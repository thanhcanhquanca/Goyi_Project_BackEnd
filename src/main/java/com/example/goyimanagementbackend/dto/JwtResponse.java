package com.example.goyimanagementbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Integer userId;
    private String userName;
    private String phoneNumber;
    private String role;
    private String redirectUrl;
}