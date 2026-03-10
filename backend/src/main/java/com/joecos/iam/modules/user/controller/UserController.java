package com.joecos.iam.modules.user.controller;

import com.joecos.iam.modules.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.joecos.iam.infrastructure.persistence.entity.UserEntity;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    /** 获取用户信息 */
    @GetMapping("/{id}")
    public UserEntity getUserById(@PathVariable Long id) {
        return userService.findById(id);
    }

    /** 获取用户权限 */
    @GetMapping("/{id}/permissions")
    public List<String> getUserPermissions(@PathVariable Long id) {
        return userService.getUserPermissionString(id);
    }
}
