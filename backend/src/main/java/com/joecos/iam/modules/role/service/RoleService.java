package com.joecos.iam.modules.role.service;

import com.joecos.iam.infrastructure.persistence.entity.*;
import java.util.List;

public interface RoleService {
    List<RoleEntity> findByIds(List<Integer> roleIds);
    List<PermissionEntity> getRolePermissions(Integer roleId);
    List<String> getPermissionCode(Integer roleId);
}
