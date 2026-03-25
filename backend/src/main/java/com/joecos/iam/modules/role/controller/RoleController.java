package com.joecos.iam.modules.role.controller;

import com.joecos.iam.common.api.ApiResponse;
import com.joecos.iam.modules.permission.model.respond.PermissionDTO;
import com.joecos.iam.modules.role.model.RoleDTO;
import com.joecos.iam.modules.role.model.request.AssignRolePermissionRequest;
import com.joecos.iam.modules.role.model.request.CreateRoleRequest;
import com.joecos.iam.modules.role.model.request.UpdateRoleInfoRequest;
import com.joecos.iam.modules.role.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService roleService;

    /** 获取身份组信息 */
    @PreAuthorize("hasAuthority('role:list')")
    @GetMapping("/{id}")
    public ApiResponse<RoleDTO> getRoleById(@PathVariable Integer id) {
        return ApiResponse.success(roleService.getRoleById(id));
    }

    /** 获取身份组权限 */
    @PreAuthorize("hasAuthority('role:list')")
    @GetMapping("/{id}/permissions")
    public ApiResponse<List<PermissionDTO>> getRolePermissions(@PathVariable Integer id) {
        return ApiResponse.success(roleService.getRolePermissions(id));
    }

    /** 更新身份组权限 */
    @PreAuthorize("hasAuthority('role:update')")
    @PostMapping("/{id}/permissions")
    public ApiResponse<Void> assignRolePermission(@PathVariable Integer id,
                                                  @RequestBody AssignRolePermissionRequest request) {
        roleService.assignRolePermission(id, request);
        return ApiResponse.success(null);
    }

    /** 查询完整身份列表 */
    @PreAuthorize("hasAuthority('role:list')")
    @GetMapping
    public ApiResponse<List<RoleDTO>> getAllRoles() {
        return ApiResponse.success(roleService.getAllRoles());
    }

    /** 更新身份组信息 */
    @PreAuthorize("hasAuthority('role:update')")
    @PutMapping("/{id}")
    public ApiResponse<Void> updateRoleInfo(@PathVariable Integer id,
                                            @RequestBody UpdateRoleInfoRequest request) {
        roleService.updateRoleInfo(id, request);
        return ApiResponse.success(null);
    }

    /** 创建新身份组 */
    @PreAuthorize("hasAuthority('role:create')")
    @PostMapping
    public ApiResponse<Integer> createRole(@RequestBody CreateRoleRequest request) {
        return ApiResponse.success(roleService.createRole(request));
    }

    /** 删除身份组 */
    @PreAuthorize("hasAuthority('role:delete')")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteRole(@PathVariable Integer id) {
        roleService.deleteRole(id);
        return ApiResponse.success(null);
    }
}