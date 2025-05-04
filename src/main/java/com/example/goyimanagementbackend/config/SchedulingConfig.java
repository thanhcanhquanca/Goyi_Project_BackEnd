package com.example.goyimanagementbackend.config;

import com.example.goyimanagementbackend.service.TokenBlacklistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class SchedulingConfig {

    @Autowired
    private TokenBlacklistService tokenBlacklistService;

    // Chạy mỗi giờ để dọn dẹp token hết hạn
    @Scheduled(fixedRate = 3600000)
    public void cleanupExpiredTokens() {
        tokenBlacklistService.cleanupExpiredTokens();
    }
}
