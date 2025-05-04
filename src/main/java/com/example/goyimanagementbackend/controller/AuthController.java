package com.example.goyimanagementbackend.controller;

import com.example.goyimanagementbackend.dto.JwtResponse;
import com.example.goyimanagementbackend.dto.LoginRequest;
import com.example.goyimanagementbackend.dto.SignupRequest;
import com.example.goyimanagementbackend.dto.UserDTO;
import com.example.goyimanagementbackend.entity.Users;
import com.example.goyimanagementbackend.repository.UserRepository;
import com.example.goyimanagementbackend.service.IUserService;
import com.example.goyimanagementbackend.service.TokenBlacklistService;
import com.example.goyimanagementbackend.security.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private IUserService userService;

    @Autowired
    private TokenBlacklistService tokenBlacklistService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest loginRequest) {
        JwtResponse response = userService.loginUser(loginRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/signup")
    public ResponseEntity<UserDTO> signup(@RequestBody SignupRequest signupRequest) {
        UserDTO userDTO = userService.registerUser(signupRequest);
        return ResponseEntity.ok(userDTO);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        logger.info("Xử lý yêu cầu đăng xuất");

        Map<String, String> response = new HashMap<>();
        String userName = "Không xác định";
        String phoneNumber = "Không xác định";

        // Lấy thông tin người dùng hiện tại đang đăng nhập
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            phoneNumber = userDetails.getUsername(); // Trong hệ thống của bạn, username là số điện thoại

            // Tìm thêm thông tin về người dùng từ database
            Optional<Users> userOpt = userRepository.findByPhoneNumber(phoneNumber);
            if (userOpt.isPresent()) {
                Users user = userOpt.get();
                userName = user.getUserName();
                logger.info("Đang đăng xuất người dùng: {} ({})", userName, phoneNumber);
            }
        }

        // Lấy token từ header
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            try {
                // Thêm token vào blacklist với thời gian hết hạn
                Date expiryDate = jwtUtil.getExpirationDateFromToken(token);
                tokenBlacklistService.blacklistToken(token, expiryDate);
                logger.info("Token đã được thêm vào blacklist");
            } catch (Exception e) {
                logger.warn("Không thể thêm token vào blacklist: {}", e.getMessage());
            }
        }

        // Xóa Authentication khỏi SecurityContext
        SecurityContextHolder.clearContext();

        response.put("message", "Đăng xuất thành công");
        response.put("userName", userName);
        response.put("phoneNumber", phoneNumber);

        return ResponseEntity.ok(response);
    }
}