package com.example.goyimanagementbackend.service;

import org.springframework.stereotype.Service;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Date;

@Service
public class TokenBlacklistService {
    // Sử dụng ConcurrentHashMap để lưu trữ token đã bị blacklist
    private ConcurrentHashMap<String, Date> blacklistedTokens = new ConcurrentHashMap<>();

    /**
     * Thêm token vào blacklist
     * @param token JWT token
     * @param expiryDate Ngày hết hạn của token
     */
    public void blacklistToken(String token, Date expiryDate) {
        blacklistedTokens.put(token, expiryDate);
    }

    /**
     * Kiểm tra xem token có trong blacklist hay không
     * @param token JWT token
     * @return true nếu token đã bị blacklist
     */
    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokens.containsKey(token);
    }

    /**
     * Xóa các token đã hết hạn khỏi blacklist để tối ưu bộ nhớ
     * Nên gọi phương thức này định kỳ bằng scheduled task
     */
    public void cleanupExpiredTokens() {
        Date now = new Date();
        blacklistedTokens.entrySet().removeIf(entry -> entry.getValue().before(now));
    }
}