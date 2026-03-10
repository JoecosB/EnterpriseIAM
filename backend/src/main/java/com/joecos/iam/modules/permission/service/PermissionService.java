package com.joecos.iam.modules.permission.service;

import com.joecos.iam.infrastructure.persistence.entity.*;
import java.util.List;

public interface PermissionService {
    PermissionEntity findById(Integer permissionId);
    List<PermissionEntity> findByIds(List<Integer> permissionIds);
    List<PermissionEntity> getAllPermissions();
    List<String> getPermissionCodes(List<Integer> permissionIds);
}
