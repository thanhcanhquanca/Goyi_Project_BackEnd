package com.example.goyimanagementbackend.security;

public final class PermissionConstants {
    // Prevent instantiation
    private PermissionConstants() {}

    // System admin - has all permissions
    public static final String SYSTEM_ADMIN = "SYSTEM_ADMIN";

    // Post permissions
    public static final String CREATE_POST = "CREATE_POST";
    public static final String VIEW_POST = "VIEW_POST";
    public static final String UPDATE_POST = "UPDATE_POST";
    public static final String DELETE_POST = "DELETE_POST";
    public static final String POST_MANAGEMENT = "POST_MANAGEMENT"; // Super permission for all post operations

    // Video permissions
    public static final String CREATE_VIDEO = "CREATE_VIDEO";
    public static final String VIEW_VIDEO = "VIEW_VIDEO";
    public static final String UPDATE_VIDEO = "UPDATE_VIDEO";
    public static final String DELETE_VIDEO = "DELETE_VIDEO";
    public static final String VIDEO_MANAGEMENT = "VIDEO_MANAGEMENT"; // Super permission for all video operations

    // Comment permissions
    public static final String CREATE_COMMENT = "CREATE_COMMENT";
    public static final String VIEW_COMMENT = "VIEW_COMMENT";
    public static final String UPDATE_COMMENT = "UPDATE_COMMENT";
    public static final String DELETE_COMMENT = "DELETE_COMMENT";
    public static final String COMMENT_MANAGEMENT = "COMMENT_MANAGEMENT"; // Super permission for all comment operations

    // User permissions
    public static final String CREATE_USER = "CREATE_USER";
    public static final String VIEW_USER = "VIEW_USER";
    public static final String UPDATE_USER = "UPDATE_USER";
    public static final String DELETE_USER = "DELETE_USER";
    public static final String USER_MANAGEMENT = "USER_MANAGEMENT";

    // Role and Permission management
    public static final String CREATE_ROLE = "CREATE_ROLE";
    public static final String VIEW_ROLE = "VIEW_ROLE";
    public static final String UPDATE_ROLE = "UPDATE_ROLE";
    public static final String DELETE_ROLE = "DELETE_ROLE";
    public static final String ROLE_MANAGEMENT = "ROLE_MANAGEMENT";

    // System admin permissions
    public static final String VIEW_ACTIVITY_LOG = "VIEW_ACTIVITY_LOG";
}
