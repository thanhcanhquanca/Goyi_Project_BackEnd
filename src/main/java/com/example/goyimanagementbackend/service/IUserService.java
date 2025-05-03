package com.example.goyimanagementbackend.service;

import com.example.goyimanagementbackend.dto.JwtResponse;
import com.example.goyimanagementbackend.dto.LoginRequest;
import com.example.goyimanagementbackend.dto.SignupRequest;
import com.example.goyimanagementbackend.dto.UserDTO;
import com.example.goyimanagementbackend.entity.Users;

import java.util.List;

public interface IUserService extends IGenerateService<Users> {
    JwtResponse loginUser(LoginRequest loginRequest);
    UserDTO registerUser(SignupRequest signupRequest);
    List<UserDTO> getAllUsers();
    UserDTO getUserById(Integer userId);
    void lockUser(Integer userId, boolean locked);
}