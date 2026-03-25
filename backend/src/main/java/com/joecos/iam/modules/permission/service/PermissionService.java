package com.joecos.iam.modules.permission.service;

import com.joecos.iam.infrastructure.persistence.entity.*;
import com.joecos.iam.modules.permission.model.PermissionTree;
import com.joecos.iam.modules.permission.model.respond.PermissionDTO;

import java.util.List;

public interface PermissionService {
    // 以下为后端使用方法
    /**
     * 根据 ID 查询权限
     *
     * @param permissionId 权限 ID
     * */
    PermissionEntity findById(Integer permissionId);

    /**
     * 根据多个 ID 查询权限
     *
     * @param permissionIds 权限 ID 列表
     * */
    List<PermissionEntity> findByIds(List<Integer> permissionIds);

    /**
     * 查询全部权限
     * */
    List<PermissionEntity> findAllPermissions();

    /**
     * 根据单个权限 ID 查询权限代码
     *
     * @param permissionId 权限 ID
     * */
    String findPermissionCodeById(Integer permissionId);

    /**
     * 根据多个权限 ID 查询权限代码
     *
     * @param permissionIds 权限 ID 列表
     * */
    List<String> findPermissionCodeByIds(List<Integer> permissionIds);

    /**
     * 通过权限代码查询权限 ID
     *
     * @param permissionCode 权限代码
     * */
    Integer findPermissionIdByCode(String permissionCode);

    /**
     * 返回当前系统权限树
     * */
    List<PermissionTree> findFullPermissionTree();


    // 以下为 API 使用方法
    /**
     * API-查询完整权限列表
     * */
    List<PermissionDTO> getAllPermissions();

    /**
     * API-通过 ID 查询单个权限
     *
     * @param permissionId 权限 ID
     * */
    PermissionDTO getPermissionById(Integer permissionId);

    /**
     * API-查询系统完整权限树
     * */
    List<PermissionTree> getFullPermissionTree();

}
