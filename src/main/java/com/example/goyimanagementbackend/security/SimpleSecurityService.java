package com.example.goyimanagementbackend.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Service đơn giản để kiểm tra quyền người dùng.
 * Sử dụng với @PreAuthorize("@securityService.hasPermission('PERMISSION_NAME')")
 */
@Component("securityService")
public class SimpleSecurityService {

    /**
     * Kiểm tra xem người dùng hiện tại có quyền được chỉ định không
     * @param permission Quyền cần kiểm tra
     * @return true nếu người dùng có quyền
     */
    public boolean hasPermission(String permission) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return false;
        }

        // 1. Kiểm tra quyền trực tiếp
        if (auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(authority -> authority.equals(permission))) {
            return true;
        }

        // 2. Kiểm tra quyền SYSTEM_ADMIN (luôn có mọi quyền)
        if (auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(authority -> authority.equals(PermissionConstants.SYSTEM_ADMIN))) {
            return true;
        }

        // 3. Kiểm tra quyền quản lý (parent)
        String parentPermission = getParentPermission(permission);
        if (parentPermission != null) {
            return auth.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .anyMatch(authority -> authority.equals(parentPermission));
        }

        return false;
    }

    /**
     * Phương thức đơn giản để xác định quyền cha
     */
    private String getParentPermission(String permission) {
        if (permission.endsWith("_POST")) {
            return PermissionConstants.POST_MANAGEMENT;
        } else if (permission.endsWith("_VIDEO")) {
            return PermissionConstants.VIDEO_MANAGEMENT;
        } else if (permission.endsWith("_COMMENT")) {
            return PermissionConstants.COMMENT_MANAGEMENT;
        } else if (permission.endsWith("_USER")) {
            return PermissionConstants.USER_MANAGEMENT;
        } else if (permission.endsWith("_ROLE")) {
            return PermissionConstants.ROLE_MANAGEMENT;
        }
        return null;
    }
}