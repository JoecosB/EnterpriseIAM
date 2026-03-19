package com.joecos.iam.modules.auth.service;

import com.joecos.iam.infrastructure.persistence.entity.PermissionEntity;
import com.joecos.iam.infrastructure.persistence.entity.UserEntity;
import com.joecos.iam.modules.auth.model.AuthResult;
import com.joecos.iam.modules.permission.model.PermissionTree;
import com.joecos.iam.modules.permission.model.PermissionTreeBuilder;
import com.joecos.iam.modules.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {
    private final UserService userService;
    private final PermissionTreeBuilder builder;

    /** 通过用户名登陆 */
    @Override
    public Boolean loginByUsername(String username, String password) {
        // 通过用户名获取用户
        UserEntity user = userService.findByUsername(username);
        if(user == null) {return false;}

        // 校验密码
        return Objects.equals(password, user.getPassword());
    }

    /** 通过 ID 加载用户信息 */
    @Override
    public AuthResult loadUserById(Long userId) {
        AuthResult result = new AuthResult();
        UserEntity user = userService.findById(userId);
        List<PermissionEntity> permissions = userService.getUserPermissions(userId);
        List<PermissionTree> permissionTree = builder.buildPermissionTree(permissions);

        result.setUserId(userId);
        result.setUsername(user.getUsername());
        result.setPermissionTree(permissionTree);

        return result;
    }

    /** 通过用户名加载用户信息 */
    @Override
    public AuthResult loadUserByUsername(String Username) {
        Long userId = userService.getUserIdByUsername(Username);
        return loadUserById(userId);
    }
}
