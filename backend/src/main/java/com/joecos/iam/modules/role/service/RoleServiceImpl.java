package com.joecos.iam.modules.role.service;

import com.joecos.iam.infrastructure.persistence.entity.*;
import com.joecos.iam.infrastructure.persistence.mapper.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.joecos.iam.modules.permission.service.PermissionService;
import com.joecos.iam.modules.role.model.RoleDTO;
import com.joecos.iam.modules.role.model.request.AssignRolePermissionRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.management.relation.Role;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class RoleServiceImpl implements RoleService {
    private final RoleMapper roleMapper;
    private final RolePermissionMapper rolePermissionMapper;
    private final PermissionMapper permissionMapper;
    private final PermissionService permissionService;


    /**
     * 查询单个 ID 对应的角色
     *
     * @param roleId 身份 ID
     * */
    @Override
    public RoleEntity findById(Integer roleId) {
        LambdaQueryWrapper<RoleEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RoleEntity::getId, roleId);

        return roleMapper.selectOne(wrapper);
    }

    /**
     * 查询多个 ID 对应的角色
     *
     * @param roleIds 身份 ID 列表
     * */
    @Override
    public List<RoleEntity> findByIds(List<Integer> roleIds) {

        if (roleIds == null || roleIds.isEmpty()) {
            return new ArrayList<>();
        }

        LambdaQueryWrapper<RoleEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(RoleEntity::getId, roleIds);

        return roleMapper.selectList(wrapper);
    }

    /**
     * 查询单个角色名称对应的角色
     *
     * @param roleName 身份名称
     * */
    @Override
    public RoleEntity findByName(String roleName) {
        LambdaQueryWrapper<RoleEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RoleEntity::getRoleName, roleName);

        return roleMapper.selectOne(wrapper);
    }

    /**
     * 查询角色对应的权限
     *
     * @param roleId 身份 ID
     * */
    @Override
    public List<PermissionEntity> findRolePermissions(Integer roleId) {

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

    /**
     * 查询角色对应的权限代码
     *
     * @param roleId 身份 ID
     * */
    @Override
    public List<String> findPermissionCodes(Integer roleId) {

        List<PermissionEntity> permissions = findRolePermissions(roleId);

        return permissions.stream()
                .map(PermissionEntity::getPermissionCode)
                .toList();
    }

    /**
     * 更新身份组权限
     *
     * @param roleId             身份组 ID
     * @param newPermissionCodes 新的权限列表
     *
     */
    @Override
    public void updateRolePermissions(Integer roleId, List<String> newPermissionCodes) {
        LambdaQueryWrapper<RolePermissionEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RolePermissionEntity::getRoleId, roleId);
        rolePermissionMapper.delete(wrapper);

        newPermissionCodes.stream()
                .map(permissionService::findPermissionIdByCode)
                .forEach(permissionId ->
                        rolePermissionMapper.insert(new RolePermissionEntity(roleId, permissionId))
                );
    }

    /**
     * 查询所有身份组
     *
     *
     */
    @Override
    public List<RoleEntity> findAllRoles() {
        return roleMapper.selectList(null);
    }

    /**
     * API-赋予身份组权限
     *
     * @param roleId  身份组 ID
     * @param request AssignRolePermissionRequest
     *
     */
    @Override
    public void assignRolePermission(Integer roleId, AssignRolePermissionRequest request) {
        List<String> newPermissionCodes = request.getPermissionCodes();

        updateRolePermissions(roleId, newPermissionCodes);

    }

    /**
     * API-查询完整角色列表
     *
     *
     */
    @Override
    public List<RoleDTO> getAllRoles() {
        List<RoleEntity> roles = findAllRoles();

        return roles.stream()
                .map(role ->
                        new RoleDTO(role.getId(), role.getRoleName(), role.getDescription())
                ).toList();
    }

    /**
     * API-查询单个身份组信息
     *
     * @param roleId 身份组 ID
     *
     */
    @Override
    public RoleDTO getRoleById(Integer roleId) {
        RoleEntity role = findById(roleId);
        return new RoleDTO(role.getId(), role.getRoleName(), role.getDescription());
    }
}