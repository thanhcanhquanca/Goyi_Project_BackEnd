package com.example.goyimanagementbackend.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PermissionHierarchyTest {

    private SimpleSecurityService securityService;

    @Mock
    private Authentication authentication;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        securityService = new SimpleSecurityService();
    }

    @Test
    public void testParentPermissionGivesAccessToChildPermissions() {
        // Tạo danh sách quyền chỉ với quyền cha POST_MANAGEMENT
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(PermissionConstants.POST_MANAGEMENT));
        
        UserDetails userDetails = new User("manager", "password", authorities);
        Authentication authToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, authorities);
        
        SecurityContextHolder.getContext().setAuthentication(authToken);
        
        // Kiểm tra các quyền con của POST_MANAGEMENT
        assertTrue(securityService.hasPermission(PermissionConstants.CREATE_POST));
        assertTrue(securityService.hasPermission(PermissionConstants.VIEW_POST));
        assertTrue(securityService.hasPermission(PermissionConstants.UPDATE_POST));
        assertTrue(securityService.hasPermission(PermissionConstants.DELETE_POST));
        
        // Kiểm tra các quyền không liên quan
        assertFalse(securityService.hasPermission(PermissionConstants.CREATE_VIDEO));
        assertFalse(securityService.hasPermission(PermissionConstants.VIEW_ACTIVITY_LOG));
    }

    @Test
    public void testVideoManagementPermissionHierarchy() {
        // Tạo danh sách quyền chỉ với quyền cha VIDEO_MANAGEMENT
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(PermissionConstants.VIDEO_MANAGEMENT));
        
        UserDetails userDetails = new User("video_manager", "password", authorities);
        Authentication authToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, authorities);
        
        SecurityContextHolder.getContext().setAuthentication(authToken);
        
        // Kiểm tra các quyền con của VIDEO_MANAGEMENT
        assertTrue(securityService.hasPermission(PermissionConstants.CREATE_VIDEO));
        assertTrue(securityService.hasPermission(PermissionConstants.VIEW_VIDEO));
        assertTrue(securityService.hasPermission(PermissionConstants.UPDATE_VIDEO));
        assertTrue(securityService.hasPermission(PermissionConstants.DELETE_VIDEO));
        
        // Kiểm tra các quyền không liên quan
        assertFalse(securityService.hasPermission(PermissionConstants.CREATE_POST));
        assertFalse(securityService.hasPermission(PermissionConstants.VIEW_ACTIVITY_LOG));
    }

    @Test
    public void testCommentManagementPermissionHierarchy() {
        // Tạo danh sách quyền chỉ với quyền cha COMMENT_MANAGEMENT
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(PermissionConstants.COMMENT_MANAGEMENT));
        
        UserDetails userDetails = new User("comment_manager", "password", authorities);
        Authentication authToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, authorities);
        
        SecurityContextHolder.getContext().setAuthentication(authToken);
        
        // Kiểm tra các quyền con của COMMENT_MANAGEMENT
        assertTrue(securityService.hasPermission(PermissionConstants.CREATE_COMMENT));
        assertTrue(securityService.hasPermission(PermissionConstants.VIEW_COMMENT));
        assertTrue(securityService.hasPermission(PermissionConstants.UPDATE_COMMENT));
        assertTrue(securityService.hasPermission(PermissionConstants.DELETE_COMMENT));
        
        // Kiểm tra các quyền không liên quan
        assertFalse(securityService.hasPermission(PermissionConstants.CREATE_POST));
        assertFalse(securityService.hasPermission(PermissionConstants.VIEW_ACTIVITY_LOG));
    }

    @Test
    public void testUserManagementPermissionHierarchy() {
        // Tạo danh sách quyền chỉ với quyền cha USER_MANAGEMENT
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(PermissionConstants.USER_MANAGEMENT));
        
        UserDetails userDetails = new User("user_manager", "password", authorities);
        Authentication authToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, authorities);
        
        SecurityContextHolder.getContext().setAuthentication(authToken);
        
        // Kiểm tra các quyền con của USER_MANAGEMENT
        assertTrue(securityService.hasPermission(PermissionConstants.CREATE_USER));
        assertTrue(securityService.hasPermission(PermissionConstants.VIEW_USER));
        assertTrue(securityService.hasPermission(PermissionConstants.UPDATE_USER));
        assertTrue(securityService.hasPermission(PermissionConstants.DELETE_USER));
        
        // Kiểm tra các quyền không liên quan
        assertFalse(securityService.hasPermission(PermissionConstants.CREATE_POST));
        assertFalse(securityService.hasPermission(PermissionConstants.VIEW_ACTIVITY_LOG));
    }

    @Test
    public void testRoleManagementPermissionHierarchy() {
        // Tạo danh sách quyền chỉ với quyền cha ROLE_MANAGEMENT
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(PermissionConstants.ROLE_MANAGEMENT));
        
        UserDetails userDetails = new User("role_manager", "password", authorities);
        Authentication authToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, authorities);
        
        SecurityContextHolder.getContext().setAuthentication(authToken);
        
        // Kiểm tra các quyền con của ROLE_MANAGEMENT
        assertTrue(securityService.hasPermission(PermissionConstants.CREATE_ROLE));
        assertTrue(securityService.hasPermission(PermissionConstants.VIEW_ROLE));
        assertTrue(securityService.hasPermission(PermissionConstants.UPDATE_ROLE));
        assertTrue(securityService.hasPermission(PermissionConstants.DELETE_ROLE));
        
        // Kiểm tra các quyền không liên quan
        assertFalse(securityService.hasPermission(PermissionConstants.CREATE_POST));
        assertFalse(securityService.hasPermission(PermissionConstants.VIEW_ACTIVITY_LOG));
    }
} 