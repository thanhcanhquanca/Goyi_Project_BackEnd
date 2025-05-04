package com.example.goyimanagementbackend.dto;

import lombok.Data;

import java.util.Set;

@Data
public class UserDTO {
    private Integer userId;
    private String userName;
    private String phoneNumber;
    private String fullName;
    private String email;
    private String roleName;
    private Set<String> permissions;
}
