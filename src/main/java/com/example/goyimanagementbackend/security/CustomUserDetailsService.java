package com.example.goyimanagementbackend.security;

import com.example.goyimanagementbackend.entity.Users;
import com.example.goyimanagementbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        Users user = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with phoneNumber: " + phoneNumber));

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        // Thêm ROLE_ prefix cho vai trò để Spring Security nhận diện
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().getRoleName()));

        // Thêm các quyền từ role (nếu có)
        if (user.getRole() != null && user.getRole().getPermissions() != null) {
            authorities.addAll(user.getRole().getPermissions().stream()
                    .map(permission -> new SimpleGrantedAuthority(permission.getPermissionName()))
                    .collect(Collectors.toList()));
        }
        return new User(
                user.getPhoneNumber(),
                user.getPassword(),
                authorities
        );
    }
}