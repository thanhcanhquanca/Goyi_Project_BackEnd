package com.example.goyimanagementbackend.exception;

public class TokenExpiredEFxception extends RuntimeException {
    public TokenExpiredEFxception(String message) {
        super(message);
    }
}
