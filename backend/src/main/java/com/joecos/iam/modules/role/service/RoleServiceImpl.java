package com.joecos.iam.modules.role.service;

import com.joecos.iam.infrastructure.persistence.entity.*;
import com.joecos.iam.infrastructure.persistence.mapper.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    @Autowired
    private PermissionMapper permissionMapper;


    /** 查询多个 ID 对应的角色 */
    @Override
    public List<RoleEntity> findByIds(List<Integer> roleIds) {

        if (roleIds == null || roleIds.isEmpty()) {
            return new ArrayList<>();
        }

        LambdaQueryWrapper<RoleEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(RoleEntity::getId, roleIds);

        return roleMapper.selectList(wrapper);
    }


    /** 查询角色对应的权限 */
    @Override
    public List<PermissionEntity> getRolePermissions(Integer roleId) {

        // 查询角色权限记录
        LambdaQueryWrapper<RolePermissionEntity> rolePermissionWrapper =
                new LambdaQueryWrapper<>();

        rolePermissionWrapper.eq(RolePermissionEntity::getRoleId, roleId);

        List<RolePermissionEntity> rolePermissions =
                rolePermissionMapper.selectList(rolePermissionWrapper);

        if (rolePermissions.isEmpty()) {
            return new ArrayList<>();
        }

        // 提取 permissionId
        List<Integer> permissionIds = rolePermissions.stream()
                .map(RolePermissionEntity::getPermissionId)
                .toList();

        // 查询权限
        LambdaQueryWrapper<PermissionEntity> permissionWrapper =
                new LambdaQueryWrapper<>();

        permissionWrapper.in(PermissionEntity::getId, permissionIds);

        return permissionMapper.selectList(permissionWrapper);
    }


    /** 查询角色对应的权限代码 */
    @Override
    public List<String> getPermissionCode(Integer roleId) {

        List<PermissionEntity> permissions = getRolePermissions(roleId);

        return permissions.stream()
                .map(PermissionEntity::getPermissionCode)
                .toList();
    }
}