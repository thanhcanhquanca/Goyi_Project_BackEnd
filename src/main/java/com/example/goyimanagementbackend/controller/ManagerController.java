package com.example.goyimanagementbackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/manager")
@PreAuthorize("hasAuthority('MANAGER')")
public class ManagerController {
    @GetMapping("/dashboard")
    public ResponseEntity<String> getManagerDashboard() {
        return ResponseEntity.ok("Welcome to Manager Dashboard");
    }
}