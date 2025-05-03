package com.example.goyimanagementbackend.service;

import com.example.goyimanagementbackend.dto.JwtResponse;
import com.example.goyimanagementbackend.dto.LoginRequest;
import com.example.goyimanagementbackend.dto.SignupRequest;
import com.example.goyimanagementbackend.dto.UserDTO;
import com.example.goyimanagementbackend.entity.Users;
import com.example.goyimanagementbackend.entity.Roles;
import com.example.goyimanagementbackend.repository.UserRepository;
import com.example.goyimanagementbackend.repository.RoleRepository;
import com.example.goyimanagementbackend.security.JwtUtil;
import com.example.goyimanagementbackend.exception.DuplicateResourceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements IUserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public JwtResponse loginUser(LoginRequest loginRequest) {
        if (loginRequest == null || loginRequest.getPhoneNumber() == null || loginRequest.getPassword() == null) {
            throw new IllegalArgumentException("Login request or required fields cannot be null");
        }
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getPhoneNumber(), loginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Users user = userRepository.findByPhoneNumber(loginRequest.getPhoneNumber())
                .orElseThrow(() -> new RuntimeException("User not found"));
        String token = jwtUtil.generateToken(user);
        String redirectUrl = getRedirectUrl(user.getRole().getRoleName());
        return new JwtResponse(token, "Bearer", user.getUserId(), user.getUserName(), user.getPhoneNumber(),
                user.getRole().getRoleName(), redirectUrl);
    }

    @Override
    public UserDTO registerUser(SignupRequest signupRequest) {
        if (signupRequest == null || signupRequest.getPhoneNumber() == null || signupRequest.getPassword() == null ||
                signupRequest.getUserName() == null) {
            throw new IllegalArgumentException("Signup request or required fields cannot be null");
        }
        if (userRepository.findByPhoneNumber(signupRequest.getPhoneNumber()).isPresent()) {
            throw new DuplicateResourceException("Số điện thoại " + signupRequest.getPhoneNumber() + " đã tồn tại");
        }
        if (userRepository.findByUserName(signupRequest.getUserName()).isPresent()) {
            throw new DuplicateResourceException("Tên người dùng " + signupRequest.getUserName() + " đã tồn tại");
        }
        Users user = new Users();
        user.setPhoneNumber(signupRequest.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        user.setFullName(signupRequest.getFullName());
        user.setUserName(signupRequest.getUserName());
        user.setEmail(signupRequest.getEmail());
        Roles role = roleRepository.findByRoleName("USER")
                .orElseThrow(() -> new RuntimeException("Role USER không tồn tại"));
        user.setRole(role);
        userRepository.save(user);
        return toDto(user);
    }

    @Override
    public Users create(Users user) {
        if (user == null || user.getPhoneNumber() == null || user.getPassword() == null || user.getUserName() == null) {
            throw new IllegalArgumentException("User or required fields cannot be null");
        }
        if (userRepository.findByPhoneNumber(user.getPhoneNumber()).isPresent()) {
            throw new DuplicateResourceException("Số điện thoại " + user.getPhoneNumber() + " đã tồn tại");
        }
        if (userRepository.findByUserName(user.getUserName()).isPresent()) {
            throw new DuplicateResourceException("Tên người dùng " + user.getUserName() + " đã tồn tại");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getRole() == null) {
            Roles role = roleRepository.findByRoleName("USER")
                    .orElseThrow(() -> new RuntimeException("Role USER không tồn tại"));
            user.setRole(role);
        }
        return userRepository.save(user);
    }

    @Override
    public Users update(Users user) {
        if (user == null || user.getUserId() == null) {
            throw new IllegalArgumentException("User or ID cannot be null");
        }
        Users existingUser = userRepository.findById(user.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        existingUser.setFullName(user.getFullName());
        existingUser.setEmail(user.getEmail());
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        if (user.getRole() != null) {
            existingUser.setRole(user.getRole());
        }
        return userRepository.save(existingUser);
    }

    @Override
    public void delete(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(id);
    }

    @Override
    public Users findById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public List<Users> findAll() {
        return userRepository.findAll();
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public UserDTO getUserById(Integer userId) {
        Users user = findById(userId);
        return toDto(user);
    }

    @Override
    public void lockUser(Integer userId, boolean locked) {
        Users user = findById(userId);
        user.setStatus(locked ? com.example.goyimanagementbackend.eNum.UserStatus.LOCKED : com.example.goyimanagementbackend.eNum.UserStatus.ACTIVE);
        userRepository.save(user);
    }

    private UserDTO toDto(Users user) {
        UserDTO dto = new UserDTO();
        dto.setUserId(user.getUserId());
        dto.setUserName(user.getUserName());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setRoleName(user.getRole().getRoleName());
        return dto;
    }

    private String getRedirectUrl(String role) {
        switch (role) {
            case "ADMIN":
                return "http://localhost:8080/admin/dashboard";
            case "USER":
                return "http://localhost:8080/user/dashboard";
            case "MANAGER":
                return "http://localhost:8080/manager/dashboard";
            default:
                return "http://localhost:8080/";
        }
    }
}