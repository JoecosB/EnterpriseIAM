package com.joecos.iam.modules.role.service;

import com.joecos.iam.infrastructure.persistence.entity.*;
import java.util.List;

public interface RoleService {
    RoleEntity findById(Integer roleId);
    List<RoleEntity> findByIds(List<Integer> roleIds);
    RoleEntity findByName(String roleName);
    List<PermissionEntity> findRolePermissions(Integer roleId);
    List<String> findPermissionCodes(Integer roleId);
}
