package com.joecos.iam.modules.permission.controller;

import com.joecos.iam.common.api.ApiResponse;
import com.joecos.iam.infrastructure.persistence.entity.PermissionEntity;
import com.joecos.iam.modules.permission.model.PermissionTree;
import com.joecos.iam.modules.permission.model.respond.PermissionDTO;
import com.joecos.iam.modules.permission.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/permissions")
public class PermissionController {
    private final PermissionService permissionService;

    /** 查询完整权限列表 */
    @PreAuthorize("hasAuthority('permission:list')")
    @GetMapping
    public ApiResponse<List<PermissionDTO>> getAllPermissions() {
        return ApiResponse.success(
                permissionService.getAllPermissions()
        );
    }

    /** 通过 ID 查询单个权限 */
    @PreAuthorize("hasAuthority('permission:list')")
    @GetMapping("/{id}")
    public ApiResponse<PermissionDTO> getPermissionById(@PathVariable Integer id) {
        return ApiResponse.success(
                permissionService.getPermissionById(id)
        );
    }

    /** 查询系统权限树 */
    @PreAuthorize("hasAuthority('permission:list')")
    @GetMapping("/tree")
    public ApiResponse<List<PermissionTree>> getFullPermissionTree() {
        return ApiResponse.success(
                permissionService.getFullPermissionTree()
        );
    }
}
