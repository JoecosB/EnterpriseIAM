package com.joecos.iam.modules.permission.controller;

import com.joecos.iam.infrastructure.persistence.entity.PermissionEntity;
import com.joecos.iam.modules.permission.model.PermissionTree;
import com.joecos.iam.modules.permission.service.PermissionService;
import lombok.RequiredArgsConstructor;
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

    /** 获取单个权限信息 */
    @GetMapping("/{id}")
    public PermissionEntity getPermission(@PathVariable Integer id) {
        return permissionService.findById(id);
    }

    /** 查询单个权限代码 */
    @GetMapping("/{id}/code")
    public String getPermissionCode(@PathVariable Integer id) {
        return permissionService.getPermissionCode(id);
    }

    /** 获取全部权限 */
    @GetMapping
    public List<PermissionEntity> getAllPermissions() {
        return permissionService.getAllPermissions();
    }

    /** 获取完整权限树 */
    @GetMapping("/tree")
    public List<PermissionTree> getFullPermissionTree() {
        return permissionService.getFullPermissionTree();
    }
}
