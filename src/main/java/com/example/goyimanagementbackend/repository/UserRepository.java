package com.example.goyimanagementbackend.repository;

import com.example.goyimanagementbackend.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Integer> {
    Optional<Users> findByPhoneNumber(String phoneNumber);
    Optional<Users> findByUserName(String userName);
}
