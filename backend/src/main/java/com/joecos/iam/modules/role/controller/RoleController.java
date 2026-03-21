package com.joecos.iam.modules.role.controller;

import com.joecos.iam.infrastructure.persistence.entity.RoleEntity;
import com.joecos.iam.modules.role.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/roles")
public class RoleController {
    private final RoleService roleService;

    /** 获取身份信息 */
    @GetMapping("/{id}")
    public RoleEntity getRoleById(@PathVariable Integer id) {
        return roleService.findById(id);
    }

    /** 获取身份权限 */
    @GetMapping("/{id}/permissions")
    public List<String> getRolePermissions(@PathVariable Integer id) {
        return roleService.findPermissionCodes(id);
    }
}
