package com.joecos.iam.modules.user.service;

import com.joecos.iam.infrastructure.persistence.entity.*;
import java.util.List;

public interface UserService {
    UserEntity findByUsername(String userName);
    UserEntity findById(Long id);
    List<RoleEntity> getUserRoles(Long userId);
    List<PermissionEntity> getUserPermissions(Long userId);
    List<String> getUserPermissionString(Long userId);
}
