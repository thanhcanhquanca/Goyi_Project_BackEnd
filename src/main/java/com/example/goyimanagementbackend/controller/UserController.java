package com.example.goyimanagementbackend.controller;

import com.example.goyimanagementbackend.entity.Users;
import com.example.goyimanagementbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
@PreAuthorize("hasAuthority('USER')")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/dashboard")
    public ResponseEntity<String> getUserDashboard() {
        return ResponseEntity.ok("Welcome to User Dashboard");
    }


    @GetMapping("/profile/qrcode")
    public ResponseEntity<?> getCurrentUserQRCode() {
        // Lấy thông tin người dùng hiện tại
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String phoneNumber = authentication.getName();

        try {
            Optional<Users> userOpt = userRepository.findByPhoneNumber(phoneNumber);
            if (userOpt.isPresent()) {
                Users user = userOpt.get();

                Map<String, String> response = new HashMap<>();
                response.put("userCode", user.getUserCode());
                response.put("qrCode", user.getQrCode());
                response.put("userName", user.getUserName());

                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Không tìm thấy thông tin người dùng");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi khi lấy thông tin QR code: " + e.getMessage());
        }
    }

    @GetMapping("/user/{userCode}")
    public ResponseEntity<?> getUserByUserCode(@PathVariable String userCode) {
        try {
            // Tìm người dùng theo user_code
            Optional<Users> userOpt = userRepository.findByUserCode(userCode);
            if (userOpt.isPresent()) {
                Users user = userOpt.get();

                // Tạo đối tượng chứa thông tin công khai của người dùng
                Map<String, Object> publicUserInfo = new HashMap<>();
                publicUserInfo.put("userName", user.getUserName());
                publicUserInfo.put("userCode", user.getUserCode());
                // Thêm các thông tin công khai khác nếu cần

                return ResponseEntity.ok(publicUserInfo);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Không tìm thấy người dùng với mã: " + userCode);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi khi tìm người dùng: " + e.getMessage());
        }
    }

}