package com.joecos.iam.modules.role.service;

import com.joecos.iam.infrastructure.persistence.entity.*;
import com.joecos.iam.modules.permission.model.respond.PermissionDTO;
import com.joecos.iam.modules.role.model.RoleDTO;
import com.joecos.iam.modules.role.model.request.AssignRolePermissionRequest;

import java.util.List;

public interface RoleService {
    // 以下为后端使用方法
    /**
     * 查询单个 ID 对应的角色
     *
     * @param roleId 身份 ID
     * */
    RoleEntity findById(Integer roleId);

    /**
     * 查询多个 ID 对应的角色
     *
     * @param roleIds 身份 ID 列表
     * */
    List<RoleEntity> findByIds(List<Integer> roleIds);

    /**
     * 查询单个角色名称对应的角色
     *
     * @param roleName 身份名称
     * */
    RoleEntity findByName(String roleName);

    /**
     * 查询角色对应的权限
     *
     * @param roleId 身份 ID
     * */
    List<PermissionEntity> findRolePermissions(Integer roleId);

    /**
     * 查询角色对应的权限代码
     *
     * @param roleId 身份 ID
     * */
    List<String> findPermissionCodes(Integer roleId);

    /**
     * 更新身份组权限
     *
     * @param roleId 身份组 ID
     * @param newPermissionCodes 新的权限列表
     * */
    void updateRolePermissions(Integer roleId, List<String> newPermissionCodes);

    /**
     * 查询所有身份组
     *
     *
     */
    List<RoleEntity> findAllRoles();


    // 以下为 API 使用方法
    /**
     * API-赋予身份组权限
     *
     * @param roleId 身份组 ID
     * @param request AssignRolePermissionRequest
     * */
    void assignRolePermission(Integer roleId, AssignRolePermissionRequest request);

    /**
     * API-查询完整身份组列表
     * */
    List<RoleDTO> getAllRoles();

    /**
     * API-查询单个身份组信息
     *
     * @param roleId 身份组 ID
     * */
    RoleDTO getRoleById(Integer roleId);

    /**
     * API-查询身份组权限
     *
     * @param roleId 身份组 ID
     * */
    List<PermissionDTO> getRolePermissions(Integer roleId);
}
