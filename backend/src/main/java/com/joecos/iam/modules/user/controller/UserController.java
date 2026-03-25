package com.joecos.iam.modules.user.controller;

import com.joecos.iam.common.api.ApiResponse;
import com.joecos.iam.modules.user.model.UserDTO;
import com.joecos.iam.modules.user.model.requests.AssignUserRolesRequest;
import com.joecos.iam.modules.user.model.requests.CreateUserRequest;
import com.joecos.iam.modules.user.model.requests.UpdateUserRequest;
import com.joecos.iam.modules.user.model.requests.UpdateUserStatusRequest;
import com.joecos.iam.modules.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    /** 获取用户权限 */
    @PreAuthorize("hasAuthority('user:list')")
    @GetMapping("/{id}/permissions")
    public ApiResponse<List<String>> getUserPermissions(@PathVariable Long id) {
        return ApiResponse.success(userService.findPermissionCodesById(id));
    }

    /** 创建用户 */
    @PreAuthorize("hasAuthority('user:create')")
    @PostMapping
    public ApiResponse<Long> createUser(@RequestBody CreateUserRequest request) {
        return ApiResponse.success(userService.createUser(request));
    }

    /** 查询用户列表 */
    @PreAuthorize("hasAuthority('user:list')")
    @GetMapping
    public ApiResponse<List<UserDTO>> getUserList() {
        return ApiResponse.success(userService.getUserList());
    }

    /** 获取用户信息 */
    @PreAuthorize("hasAuthority('user:list')")
    @GetMapping("/{id}")
    public ApiResponse<UserDTO> getUserById(@PathVariable Long id) {
        return ApiResponse.success(userService.getUserById(id));
    }

    /** 更新用户信息 */
    @PreAuthorize("hasAuthority('user:update')")
    @PutMapping("/{id}")
    public ApiResponse<Void> updateUserInfo(@PathVariable Long id,
                                            @RequestBody UpdateUserRequest request) {
        userService.updateUserInfo(id, request);
        return ApiResponse.success(null);
    }

    /** 删除用户 */
    @PreAuthorize("hasAuthority('user:delete')")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ApiResponse.success(null);
    }

    /** 更新用户状态 */
    @PreAuthorize("hasAuthority('user:update')")
    @PatchMapping("/{id}/status")
    public ApiResponse<Void> updateUserStatus(@PathVariable Long id,
                                              @RequestBody UpdateUserStatusRequest request) {
        userService.updateUserStatus(id, request);
        return ApiResponse.success(null);
    }

    /** 更新用户身份组 */
    @PreAuthorize("hasAuthority('user:assign')")
    @PostMapping("/{id}/roles")
    public ApiResponse<Void> assignUserRoles(@PathVariable Long id,
                                             @RequestBody AssignUserRolesRequest request) {
        userService.assignUserRoles(id, request);
        return ApiResponse.success(null);
    }
}