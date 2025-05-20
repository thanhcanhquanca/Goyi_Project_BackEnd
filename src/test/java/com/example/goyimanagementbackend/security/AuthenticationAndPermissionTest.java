package com.example.goyimanagementbackend.security;

import com.example.goyimanagementbackend.dto.JwtResponse;
import com.example.goyimanagementbackend.dto.LoginRequest;
import com.example.goyimanagementbackend.entity.Permissions;
import com.example.goyimanagementbackend.entity.Roles;
import com.example.goyimanagementbackend.entity.Users;
import com.example.goyimanagementbackend.repository.RoleRepository;
import com.example.goyimanagementbackend.repository.UserRepository;
import com.example.goyimanagementbackend.service.IUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AuthenticationAndPermissionTest {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private AuthenticationManager authenticationManager;

    @Autowired
    private IUserService userService;

    @Autowired
    private SimpleSecurityService securityService;

    @Mock
    private Authentication authentication;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLoginSuccessAndPermissions() {
        // Tạo role với permissions
        Roles adminRole = new Roles();
        adminRole.setRoleId(1);
        adminRole.setRoleName("ADMIN");

        // Tạo permissions
        Set<Permissions> permissions = new HashSet<>();
        Permissions createUserPermission = new Permissions();
        createUserPermission.setPermissionId(1);
        createUserPermission.setPermissionName(PermissionConstants.CREATE_USER);
        
        Permissions userManagementPermission = new Permissions();
        userManagementPermission.setPermissionId(2);
        userManagementPermission.setPermissionName(PermissionConstants.USER_MANAGEMENT);
        
        permissions.add(createUserPermission);
        permissions.add(userManagementPermission);
        adminRole.setPermissions(permissions);

        // Tạo user
        Users user = new Users();
        user.setUserId(1);
        user.setUserName("testadmin");
        user.setFullName("Test Admin");
        user.setPhoneNumber("0123456789");
        user.setPassword("$2a$10$abcdefghijklmnopqrstuvwxyz123456789"); // Giả lập mật khẩu đã được mã hóa
        user.setRole(adminRole);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        // Mock repository
        when(userRepository.findByPhoneNumber("0123456789")).thenReturn(Optional.of(user));
        when(roleRepository.findPermissionNamesByRoleId(1)).thenReturn(
                Arrays.asList(PermissionConstants.CREATE_USER, PermissionConstants.USER_MANAGEMENT)
        );

        // Mock authentication
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        authorities.add(new SimpleGrantedAuthority(PermissionConstants.CREATE_USER));
        authorities.add(new SimpleGrantedAuthority(PermissionConstants.USER_MANAGEMENT));
        
        UserDetails userDetails = new User("0123456789", "password", authorities);
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, authorities);
        
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authToken);

        // Thực hiện login
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setPhoneNumber("0123456789");
        loginRequest.setPassword("password");
        
        JwtResponse response = userService.loginUser(loginRequest);
        
        // Kiểm tra kết quả
        assertNotNull(response);
        assertEquals("testadmin", response.getUserName());
        assertEquals("0123456789", response.getPhoneNumber());
        assertEquals("ADMIN", response.getRole());
        assertTrue(response.getPermissions().contains(PermissionConstants.CREATE_USER));
        assertTrue(response.getPermissions().contains(PermissionConstants.USER_MANAGEMENT));
        
        // Kiểm tra quyền với SimpleSecurityService
        SecurityContextHolder.getContext().setAuthentication(authToken);
        
        // Kiểm tra quyền trực tiếp
        assertTrue(securityService.hasPermission(PermissionConstants.CREATE_USER));
        
        // Kiểm tra quyền cha
        assertTrue(securityService.hasPermission(PermissionConstants.UPDATE_USER)); // Nên trả về true vì có USER_MANAGEMENT
        
        // Kiểm tra quyền không có
        assertFalse(securityService.hasPermission(PermissionConstants.CREATE_VIDEO));
    }
    
    @Test
    public void testSystemAdminHasAllPermissions() {
        // Tạo role với permission SYSTEM_ADMIN
        Roles systemAdminRole = new Roles();
        systemAdminRole.setRoleId(2);
        systemAdminRole.setRoleName("SYSTEM_ADMIN");

        Set<Permissions> permissions = new HashSet<>();
        Permissions systemAdminPermission = new Permissions();
        systemAdminPermission.setPermissionId(3);
        systemAdminPermission.setPermissionName(PermissionConstants.SYSTEM_ADMIN);
        permissions.add(systemAdminPermission);
        systemAdminRole.setPermissions(permissions);

        // Mock authentication với quyền SYSTEM_ADMIN
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_SYSTEM_ADMIN"));
        authorities.add(new SimpleGrantedAuthority(PermissionConstants.SYSTEM_ADMIN));
        
        UserDetails userDetails = new User("admin", "password", authorities);
        Authentication authToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, authorities);
        
        SecurityContextHolder.getContext().setAuthentication(authToken);
        
        // Kiểm tra các quyền khác nhau
        assertTrue(securityService.hasPermission(PermissionConstants.CREATE_USER));
        assertTrue(securityService.hasPermission(PermissionConstants.UPDATE_VIDEO));
        assertTrue(securityService.hasPermission(PermissionConstants.DELETE_POST));
        assertTrue(securityService.hasPermission(PermissionConstants.VIEW_ACTIVITY_LOG));
        
        // SYSTEM_ADMIN nên có tất cả các quyền
    }
} 