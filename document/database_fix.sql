-- Gán quyền cho ADMIN (tất cả quyền)
INSERT INTO role_permissions (roleid, permissionid, created_at)
SELECT 1, permission_id, '2025-05-01 08:00:00'
FROM permissions;

-- Gán quyền cho USER (quyền cơ bản)
INSERT INTO role_permissions (roleid, permissionid, created_at)
SELECT 2, permission_id, '2025-05-01 08:00:00'
FROM permissions
WHERE permission_name IN (
                          'CREATE_POST', 'VIEW_POST', 'UPDATE_POST', 'DELETE_POST',
                          'CREATE_VIDEO', 'VIEW_VIDEO', 'UPDATE_VIDEO', 'DELETE_VIDEO',
                          'CREATE_COMMENT', 'UPDATE_COMMENT', 'DELETE_COMMENT'
    );

-- Gán quyền cho CREATOR (quyền USER + CREATE_ADS)
INSERT INTO role_permissions (roleid, permissionid, created_at)
SELECT 3, permission_id, '2025-05-01 08:00:00'
FROM permissions
WHERE permission_name IN (
                          'CREATE_POST', 'VIEW_POST', 'UPDATE_POST', 'DELETE_POST',
                          'CREATE_VIDEO', 'VIEW_VIDEO', 'UPDATE_VIDEO', 'DELETE_VIDEO',
                          'CREATE_COMMENT', 'UPDATE_COMMENT', 'DELETE_COMMENT',
                          'CREATE_ADS'
    );

DROP TABLE role_permissions;

-- Tạo bảng role_permissions
CREATE TABLE role_permissions (
                                  roleid INT NOT NULL,
                                  permissionid INT NOT NULL,
                                  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                  PRIMARY KEY (roleid, permissionid),
                                  FOREIGN KEY (roleid) REFERENCES roles(role_id),
                                  FOREIGN KEY (permissionid) REFERENCES permissions(permission_id)
);