package com.joecos.iam.modules.role.service;

import com.joecos.iam.common.constant.ErrorCode;
import com.joecos.iam.common.exception.BusinessException;
import com.joecos.iam.infrastructure.persistence.entity.*;
import com.joecos.iam.infrastructure.persistence.mapper.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.joecos.iam.modules.permission.model.respond.PermissionDTO;
import com.joecos.iam.modules.permission.service.PermissionService;
import com.joecos.iam.modules.role.model.RoleDTO;
import com.joecos.iam.modules.role.model.request.AssignRolePermissionRequest;
import com.joecos.iam.modules.role.model.request.CreateRoleRequest;
import com.joecos.iam.modules.role.model.request.UpdateRoleInfoRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        if (roleId == null) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST, "Role id cannot be null");
        }
        return roleMapper.selectById(roleId);
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
        if (roleName == null || roleName.isBlank()) {
            return null;
        }

        LambdaQueryWrapper<RoleEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RoleEntity::getRoleName, roleName);
        return roleMapper.selectOne(wrapper);
    }

    /**
     * 查询单个代码对应的身份组
     *
     * @param roleCode 身份组代码
     * */
    @Override
    public RoleEntity findByCode(Integer roleCode) {
        if (roleCode == null) {
            return null;
        }

        LambdaQueryWrapper<RoleEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RoleEntity::getRoleCode, roleCode);
        return roleMapper.selectOne(wrapper);
    }

    /**
     * 查询角色对应的权限
     *
     * @param roleId 身份 ID
     * */
    @Override
    public List<PermissionEntity> findRolePermissions(Integer roleId) {
        assertRoleExists(roleId);

        LambdaQueryWrapper<RolePermissionEntity> rolePermissionWrapper = new LambdaQueryWrapper<>();
        rolePermissionWrapper.eq(RolePermissionEntity::getRoleId, roleId);

        List<RolePermissionEntity> rolePermissions = rolePermissionMapper.selectList(rolePermissionWrapper);
        if (rolePermissions.isEmpty()) {
            return new ArrayList<>();
        }

        List<Integer> permissionIds = rolePermissions.stream()
                .map(RolePermissionEntity::getPermissionId)
                .toList();

        LambdaQueryWrapper<PermissionEntity> permissionWrapper = new LambdaQueryWrapper<>();
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
        return findRolePermissions(roleId).stream()
                .map(PermissionEntity::getPermissionCode)
                .toList();
    }

    /**
     * 更新身份组权限
     *
     * @param roleId 身份组 ID
     * @param newPermissionCodes 新的权限列表
     * */
    @Override
    public void updateRolePermissions(Integer roleId, List<String> newPermissionCodes) {
        assertRoleExists(roleId);

        LambdaQueryWrapper<RolePermissionEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RolePermissionEntity::getRoleId, roleId);
        rolePermissionMapper.delete(wrapper);

        if (newPermissionCodes == null || newPermissionCodes.isEmpty()) {
            return;
        }

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

    /** 根据 ID 删除身份组
     *
     * @param roleId 身份组 ID
     * */
    @Override
    public void deleteRoleById(Integer roleId) {
        assertRoleExists(roleId);

        LambdaQueryWrapper<RolePermissionEntity> rolePermissionWrapper = new LambdaQueryWrapper<>();
        rolePermissionWrapper.eq(RolePermissionEntity::getRoleId, roleId);
        rolePermissionMapper.delete(rolePermissionWrapper);

        roleMapper.deleteById(roleId);
    }

    /**
     * 校验身份组是否存在。
     *
     * @param roleId 身份组 ID
     */
    private void assertRoleExists(Integer roleId) {
        if (findById(roleId) == null) {
            throw new BusinessException(ErrorCode.ROLE_NOT_FOUND);
        }
    }

    /**
     * 查询并返回指定身份组；若身份组不存在则抛出异常。
     *
     * @param roleId 身份组 ID
     */
    private RoleEntity requireRole(Integer roleId) {
        RoleEntity role = findById(roleId);
        if (role == null) {
            throw new BusinessException(ErrorCode.ROLE_NOT_FOUND);
        }
        return role;
    }

    /**
     * API-赋予身份组权限
     *
     * @param roleId 身份组 ID
     * @param request AssignRolePermissionRequest
     * */
    @Override
    public void assignRolePermission(Integer roleId, AssignRolePermissionRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST, "Request cannot be null");
        }

        updateRolePermissions(roleId, request.getPermissionCodes());
    }

    /**
     * API-查询完整身份组列表
     * */
    @Override
    public List<RoleDTO> getAllRoles() {
        return findAllRoles().stream()
                .map(role -> new RoleDTO(
                        role.getId(),
                        role.getRoleName(),
                        role.getDescription()
                ))
                .toList();
    }

    /**
     * API-查询单个身份组信息
     *
     * @param roleId 身份组 ID
     * */
    @Override
    public RoleDTO getRoleById(Integer roleId) {
        RoleEntity role = requireRole(roleId);
        return new RoleDTO(role.getId(), role.getRoleName(), role.getDescription());
    }

    /**
     * API-查询身份组权限
     *
     * @param roleId 身份组 ID
     * */
    @Override
    public List<PermissionDTO> getRolePermissions(Integer roleId) {
        return findRolePermissions(roleId).stream()
                .map(permission -> new PermissionDTO(
                        permission.getId(),
                        permission.getPermissionName()
                ))
                .toList();
    }

    /**
     * API-更新身份组信息
     *
     * @param roleId 身份组 ID
     * @param request UpdateRoleRequest
     * */
    @Override
    public void updateRoleInfo(Integer roleId, UpdateRoleInfoRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST, "Request cannot be null");
        }

        RoleEntity role = requireRole(roleId);

        String newRoleName = request.getRoleName();
        String newRoleDesc = request.getRoleDesc();

        if (newRoleName != null && !newRoleName.isBlank()) {
            RoleEntity existedRole = findByName(newRoleName);
            if (existedRole != null && !existedRole.getId().equals(roleId)) {
                throw new BusinessException(ErrorCode.ROLE_NAME_OCCUPIED);
            }
            role.setRoleName(newRoleName);
        }

        if (newRoleDesc != null) {
            role.setDescription(newRoleDesc);
        }

        roleMapper.updateById(role);
    }

    /**
     * API-创建新身份组
     *
     * @param request CreateRoleRequest
     * */
    @Override
    public Integer createRole(CreateRoleRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST, "Request cannot be null");
        }

        if (findByName(request.getRoleName()) != null) {
            throw new BusinessException(ErrorCode.ROLE_NAME_OCCUPIED);
        }

        if (findByCode(request.getRoleCode()) != null) {
            throw new BusinessException(ErrorCode.ROLE_CODE_OCCUPIED);
        }

        RoleEntity newRole = new RoleEntity();
        newRole.setRoleName(request.getRoleName());
        newRole.setRoleCode(request.getRoleCode());
        newRole.setDescription(request.getRoleDesc());

        roleMapper.insert(newRole);
        return newRole.getId();
    }

    /**
     * API-删除身份组
     *
     * @param roleId 身份组 ID
     * */
    @Override
    public void deleteRole(Integer roleId) {
        deleteRoleById(roleId);
    }
}