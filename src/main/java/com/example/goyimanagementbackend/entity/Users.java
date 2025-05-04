package com.example.goyimanagementbackend.entity;

import com.example.goyimanagementbackend.eNum.UserStatus; // Import enum UserStatus
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "Users")
@Data
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;                          // ID của người dùng

    @Column(nullable = false, unique = true, length = 50)
    private String userName;                         // Tên đăng nhập, không được trùng

    @Column(nullable = false, length = 100)
    private String fullName;                         // Họ và tên đầy đủ

    @Column(nullable = false, unique = true, length = 20)
    private String phoneNumber;                      // Số điện thoại, không được trùng

    @Column(length = 100)
    private String email;                            // Địa chỉ email

    @Column(nullable = false, length = 255)
    private String password;                         // Mật khẩu đã được mã hóa

    @ManyToOne
    @JoinColumn(name = "RoleID", nullable = false)
    private Roles role;                             // Vai trò của người dùng

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status = UserStatus.ACTIVE;   // Trạng thái người dùng, mặc định là ACTIVE

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;                 // Thời điểm tạo tài khoản

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;                 // Thời điểm cập nhật gần nhất

    private LocalDateTime lastLogin;                 // Thời điểm đăng nhập gần nhất

    @Column(columnDefinition = "TEXT")
    private String address;                          // Địa chỉ của người dùng

    @Column(length = 255)
    private String profilePicture;                   // Đường dẫn ảnh đại diện

    @Column(length = 255)
    private String coverPhoto;                       // Đường dẫn ảnh bìa

    @Column(length = 255)
    private String qrCode;                           // Mã QR của người dùng

    @Column(unique = true, length = 50)
    private String userCode;                         // Mã người dùng, không được trùng

    private Boolean is2faEnabled = false;            // Trạng thái bảo mật 2 lớp

    @Column(length = 100)
    private String twoFASecret;                      // Mã bí mật cho xác thực 2 lớp


    private Integer copyrightViolations = 0;         // Số lần vi phạm bản quyền

    // Constructor mặc định
    public Users() {
        this.createdAt = LocalDateTime.now();        // Khởi tạo thời gian tạo
        this.updatedAt = LocalDateTime.now();        // Khởi tạo thời gian cập nhật
        this.copyrightViolations = 0;                // Khởi tạo số vi phạm bản quyền
        this.is2faEnabled = false;                   // Mặc định tắt xác thực 2 lớp
    }

    // Phương thức cập nhật thời gian
    @PreUpdate
    public void updateTimestamp() {
        this.updatedAt = LocalDateTime.now();        // Cập nhật thời gian khi có thay đổi
    }
}