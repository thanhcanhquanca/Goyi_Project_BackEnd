package com.example.goyimanagementbackend.security;

import com.example.goyimanagementbackend.entity.Users;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {
    @Value("${app.jwt.secret}")
    private String SECRET_KEY;

    @Value("${app.jwt.expiration}")
    private long EXPIRATION;

    public String generateToken(Users user) {
        if (user == null || user.getPhoneNumber() == null || user.getRole() == null || user.getRole().getRoleName() == null) {
            throw new IllegalArgumentException("User or required fields cannot be null");
        }
        return Jwts.builder()
                .setSubject(user.getPhoneNumber())
                .claim("role", "ROLE_" + user.getRole().getRoleName())
                .claim("userId", user.getUserId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String getPhoneNumberFromToken(String token) {
        if (!validateToken(token)) {
            throw new JwtException("Invalid or expired token");
        }
        return Jwts.parser().setSigningKey(SECRET_KEY)
                .parseClaimsJws(token).getBody().getSubject();
    }

    public String getRoleFromToken(String token) {
        if (!validateToken(token)) {
            throw new JwtException("Invalid or expired token");
        }
        return (String) Jwts.parser().setSigningKey(SECRET_KEY)
                .parseClaimsJws(token).getBody().get("role");
    }
}