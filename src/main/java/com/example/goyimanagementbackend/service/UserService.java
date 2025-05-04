package com.example.goyimanagementbackend.service;

import com.example.goyimanagementbackend.dto.JwtResponse;
import com.example.goyimanagementbackend.dto.LoginRequest;
import com.example.goyimanagementbackend.dto.SignupRequest;
import com.example.goyimanagementbackend.dto.UserDTO;
import com.example.goyimanagementbackend.entity.Permissions;
import com.example.goyimanagementbackend.entity.Roles;
import com.example.goyimanagementbackend.entity.Users;
import com.example.goyimanagementbackend.repository.UserRepository;
import com.example.goyimanagementbackend.repository.RoleRepository;
import com.example.goyimanagementbackend.security.JwtUtil;
import com.example.goyimanagementbackend.exception.DuplicateResourceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService implements IUserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

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
        logger.info("Attempting to log in user with phone number: {}", loginRequest.getPhoneNumber());
        if (loginRequest == null || loginRequest.getPhoneNumber() == null || loginRequest.getPassword() == null) {
            logger.error("Invalid login request: phoneNumber or password is null");
            throw new IllegalArgumentException("Login request or required fields cannot be null");
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getPhoneNumber(), loginRequest.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            Users user = userRepository.findByPhoneNumber(loginRequest.getPhoneNumber())
                    .orElseThrow(() -> {
                        logger.error("User not found with phone number: {}", loginRequest.getPhoneNumber());
                        return new RuntimeException("User not found with phone number: " + loginRequest.getPhoneNumber());
                    });

            String token = jwtUtil.generateToken(user);
            String redirectUrl = getRedirectUrl(user.getRole().getRoleName());

            // Lấy danh sách quyền sử dụng phương thức repository
            Set<String> permissions = new HashSet<>();
            try {
                if (user.getRole() != null) {
                    List<String> permissionNames = roleRepository.findPermissionNamesByRoleId(user.getRole().getRoleId());
                    permissions.addAll(permissionNames);
                    logger.info("Found {} permissions for role {}", permissions.size(), user.getRole().getRoleName());
                }
            } catch (Exception e) {
                logger.error("Error fetching permissions: {}", e.getMessage(), e);
                // Tiếp tục xử lý mà không ném ngoại lệ
            }

            logger.info("User {} logged in successfully with role: {}", user.getUserName(), user.getRole().getRoleName());
            return new JwtResponse(token, "Bearer", user.getUserId(), user.getUserName(), user.getPhoneNumber(),
                    user.getRole().getRoleName(), redirectUrl, permissions);
        } catch (Exception e) {
            logger.error("Login failed: {}", e.getMessage(), e);
            throw new RuntimeException("Login failed: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public UserDTO registerUser(SignupRequest signupRequest) {
        logger.info("Attempting to register user with phone number: {} and username: {}",
                signupRequest.getPhoneNumber(), signupRequest.getUserName());

        try {
            // Kiểm tra dữ liệu đầu vào
            if (signupRequest == null ||
                    signupRequest.getPhoneNumber() == null ||
                    signupRequest.getPassword() == null ||
                    signupRequest.getUserName() == null) {
                logger.error("Invalid signup request: required fields are null");
                throw new IllegalArgumentException("Signup request or required fields cannot be null");
            }

            // Kiểm tra trùng số điện thoại
            if (userRepository.findByPhoneNumber(signupRequest.getPhoneNumber()).isPresent()) {
                logger.warn("Phone number {} already exists", signupRequest.getPhoneNumber());
                throw new DuplicateResourceException("Phone number " + signupRequest.getPhoneNumber() + " already exists");
            }

            // Kiểm tra trùng tên người dùng
            if (userRepository.findByUserName(signupRequest.getUserName()).isPresent()) {
                logger.warn("Username {} already exists", signupRequest.getUserName());
                throw new DuplicateResourceException("Username " + signupRequest.getUserName() + " already exists");
            }

            // Tìm vai trò "USER" bằng ID hoặc vai trò đầu tiên nếu không tìm thấy
            Roles role;
            try {
                // Lấy vai trò USER (giả định ID của vai trò USER là 2)
                role = roleRepository.findById(2).orElse(null);

                // Nếu không tìm thấy, thử findByRoleName
                if (role == null) {
                    role = roleRepository.findByRoleName("USER").orElse(null);
                }

                // Nếu vẫn không tìm thấy, lấy bất kỳ vai trò nào
                if (role == null) {
                    List<Roles> roles = roleRepository.findAll();
                    if (!roles.isEmpty()) {
                        role = roles.get(0);
                    } else {
                        throw new RuntimeException("No roles found in the database");
                    }
                }
            } catch (Exception e) {
                logger.error("Error finding a role: {}", e.getMessage());
                throw new RuntimeException("Failed to find a suitable role");
            }

            // Tạo người dùng mới
            Users user = new Users();
            user.setPhoneNumber(signupRequest.getPhoneNumber());
            user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
            user.setFullName(signupRequest.getFullName());
            user.setUserName(signupRequest.getUserName());
            user.setEmail(signupRequest.getEmail());
            user.setStatus(com.example.goyimanagementbackend.eNum.UserStatus.ACTIVE);
            user.setRole(role);

            // Lưu người dùng vào database
            Users savedUser = userRepository.save(user);
            logger.info("User {} registered successfully with role: {}",
                    savedUser.getUserName(), role.getRoleName());

            // Tạo DTO với thông tin cơ bản
            UserDTO userDTO = new UserDTO();
            userDTO.setUserId(savedUser.getUserId());
            userDTO.setUserName(savedUser.getUserName());
            userDTO.setPhoneNumber(savedUser.getPhoneNumber());
            userDTO.setFullName(savedUser.getFullName());
            userDTO.setEmail(savedUser.getEmail());
            userDTO.setRoleName(role.getRoleName());

            // Lấy danh sách quyền từ database
            try {
                List<String> permissionNames = roleRepository.findPermissionNamesByRoleId(role.getRoleId());
                Set<String> permissions = new HashSet<>(permissionNames);
                userDTO.setPermissions(permissions);
                logger.info("Loaded {} permissions for new user with role {}",
                        permissions.size(), role.getRoleName());
            } catch (Exception e) {
                logger.error("Error loading permissions: {}", e.getMessage(), e);
                userDTO.setPermissions(Collections.emptySet());
            }

            return userDTO;
        } catch (Exception e) {
            logger.error("Đăng ký thất bại: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to register user: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Users create(Users user) {
        logger.info("Creating user with phone number: {}", user.getPhoneNumber());
        if (user == null || user.getPhoneNumber() == null || user.getPassword() == null || user.getUserName() == null) {
            logger.error("Invalid user creation request: required fields are null");
            throw new IllegalArgumentException("User or required fields cannot be null");
        }
        if (userRepository.findByPhoneNumber(user.getPhoneNumber()).isPresent()) {
            logger.warn("Phone number {} already exists", user.getPhoneNumber());
            throw new DuplicateResourceException("Phone number " + user.getPhoneNumber() + " already exists");
        }
        if (userRepository.findByUserName(user.getUserName()).isPresent()) {
            logger.warn("Username {} already exists", user.getUserName());
            throw new DuplicateResourceException("Username " + user.getUserName() + " already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getRole() == null) {
            logger.debug("Assigning default USER role for new user");
            Roles role = roleRepository.findByRoleName("USER")
                    .orElseThrow(() -> {
                        logger.error("Role USER not found in the database");
                        return new RuntimeException("Role USER not found. Please ensure the database is properly initialized.");
                    });
            user.setRole(role);
        }
        user.setStatus(com.example.goyimanagementbackend.eNum.UserStatus.ACTIVE);
        try {
            Users savedUser = userRepository.save(user);
            logger.info("User {} created successfully", savedUser.getUserName());
            return savedUser;
        } catch (Exception e) {
            logger.error("Failed to create user: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create user: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public Users update(Users user) {
        logger.info("Updating user with ID: {}", user.getUserId());
        if (user == null || user.getUserId() == null) {
            logger.error("Invalid user update request: user or ID is null");
            throw new IllegalArgumentException("User or ID cannot be null");
        }
        Users existingUser = userRepository.findById(user.getUserId())
                .orElseThrow(() -> {
                    logger.error("User not found with ID: {}", user.getUserId());
                    return new RuntimeException("User not found with ID: " + user.getUserId());
                });
        existingUser.setFullName(user.getFullName());
        existingUser.setEmail(user.getEmail());
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        if (user.getRole() != null) {
            existingUser.setRole(user.getRole());
        }
        try {
            Users updatedUser = userRepository.save(existingUser);
            logger.info("User {} updated successfully", updatedUser.getUserName());
            return updatedUser;
        } catch (Exception e) {
            logger.error("Failed to update user: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to update user: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        logger.info("Deleting user with ID: {}", id);
        if (!userRepository.existsById(id)) {
            logger.error("User not found with ID: {}", id);
            throw new RuntimeException("User not found with ID: " + id);
        }
        try {
            userRepository.deleteById(id);
            logger.info("User with ID {} deleted successfully", id);
        } catch (Exception e) {
            logger.error("Failed to delete user: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to delete user: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public Users findById(Integer id) {
        logger.info("Fetching user with ID: {}", id);
        return userRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("User not found with ID: {}", id);
                    return new RuntimeException("User not found with ID: " + id);
                });
    }

    @Override
    @Transactional
    public List<Users> findAll() {
        logger.info("Fetching all users");
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public List<UserDTO> getAllUsers() {
        logger.info("Fetching all users as DTOs");
        return findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserDTO getUserById(Integer userId) {
        logger.info("Fetching user DTO with ID: {}", userId);
        Users user = findById(userId);
        return toDto(user);
    }

    @Override
    @Transactional
    public void lockUser(Integer userId, boolean locked) {
        logger.info("Locking/unlocking user with ID: {}, locked: {}", userId, locked);
        Users user = findById(userId);
        user.setStatus(locked ? com.example.goyimanagementbackend.eNum.UserStatus.LOCKED : com.example.goyimanagementbackend.eNum.UserStatus.ACTIVE);
        try {
            userRepository.save(user);
            logger.info("User {} lock status updated to {}", user.getUserName(), user.getStatus());
        } catch (Exception e) {
            logger.error("Failed to update user lock status: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to update user lock status: " + e.getMessage(), e);
        }
    }

    private UserDTO toDto(Users user) {
        logger.debug("Converting user to DTO: {}", user.getUserName());
        UserDTO dto = new UserDTO();
        dto.setUserId(user.getUserId());
        dto.setUserName(user.getUserName());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setRoleName(user.getRole() != null ? user.getRole().getRoleName() : null);
        if (user.getRole() != null && user.getRole().getPermissions() != null) {
            Set<String> permissionNames = user.getRole().getPermissions()
                    .stream()
                    .map(Permissions::getPermissionName)
                    .collect(Collectors.toSet());
            dto.setPermissions(permissionNames);
        } else {
            dto.setPermissions(Collections.emptySet());
        }
        return dto;
    }

    private String getRedirectUrl(String role) {
        logger.debug("Determining redirect URL for role: {}", role);
        switch (role != null ? role : "USER") {
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