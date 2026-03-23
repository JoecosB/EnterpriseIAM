package com.joecos.iam.modules.role.controller;

import com.joecos.iam.infrastructure.persistence.entity.RoleEntity;
import com.joecos.iam.modules.role.model.RoleDTO;
import com.joecos.iam.modules.role.model.request.AssignRolePermissionRequest;
import com.joecos.iam.modules.role.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/roles")
public class RoleController {
    private final RoleService roleService;

    /** 获取身份组信息 */
    @GetMapping("/{id}")
    public RoleDTO getRoleById(@PathVariable Integer id) {
        return roleService.getRoleById(id);
    }

    /** 获取身份组权限 */
    @GetMapping("/{id}/permissions")
    public List<String> getRolePermissions(@PathVariable Integer id) {
        return roleService.findPermissionCodes(id);
    }

    /** 更新身份组权限 */
    @PostMapping("/{id}/permissions")
    public void assignRolePermission(@PathVariable Integer id,
                                     @RequestBody AssignRolePermissionRequest request
    ) {
        roleService.assignRolePermission(id, request);
    }

    /** 查询完整身份列表 */
    @GetMapping
    public List<RoleDTO> getAllRoles() {
        return roleService.getAllRoles();
    }
}
