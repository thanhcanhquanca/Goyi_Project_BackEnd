package com.example.goyimanagementbackend.repository;

import com.example.goyimanagementbackend.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Integer> {
    Optional<Users> findByPhoneNumber(String phoneNumber);
    Optional<Users> findByUserName(String userName);

    // Tìm mã người dùng lớn nhất hiện tại
    @Query(value = "SELECT MAX(user_code) FROM Users", nativeQuery = true)
    String findMaxUserCode();

    // Kiểm tra user_code đã tồn tại chưa
    boolean existsByUserCode(String userCode);

    // Thêm phương thức này
    Optional<Users> findByUserCode(String userCode);
}
