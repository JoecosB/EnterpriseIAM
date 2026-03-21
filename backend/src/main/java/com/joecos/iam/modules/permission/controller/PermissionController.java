package com.joecos.iam.modules.permission.controller;

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
    public List<PermissionDTO> getAllPermissions() {
        return permissionService.getAllPermissions();
    }

    /** 通过 ID 查询单个权限 */
    @GetMapping("/{id}")
    public PermissionDTO getPermissionById(@PathVariable Integer id) {
        return permissionService.getPermissionById(id);
    }

    /** 查询系统权限树 */
    @PreAuthorize("hasAuthority('permission:list')")
    @GetMapping("/tree")
    public List<PermissionTree> getFullPermissionTree() {
        return permissionService.getFullPermissionTree();
    }
}
