package com.joecos.iam.modules.auth.service;

import com.joecos.iam.common.constant.ErrorCode;
import com.joecos.iam.common.exception.BusinessException;
import com.joecos.iam.infrastructure.persistence.entity.PermissionEntity;
import com.joecos.iam.infrastructure.persistence.entity.UserEntity;
import com.joecos.iam.modules.auth.model.AuthResult;
import com.joecos.iam.modules.permission.model.PermissionTree;
import com.joecos.iam.modules.permission.model.PermissionTreeBuilder;
import com.joecos.iam.modules.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {
    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;

    /** 通过用户名登陆 */
    @Override
    public Boolean loginByUsername(String username, String password) {
        if (username == null || username.isBlank()) {
            throw new BusinessException(ErrorCode.INVALID_USERNAME);
        }

        if (password == null || password.isBlank()) {
            throw new BusinessException(ErrorCode.INVALID_PASSWORD);
        }

        UserEntity user = userService.findUserByUsername(username);
        if (user == null) {
            throw new BusinessException(ErrorCode.LOGIN_FAILED);
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BusinessException(ErrorCode.LOGIN_FAILED);
        }

        return true;
    }

    /** 通过 ID 加载用户信息 */
    @Override
    public AuthResult loadUserById(Long userId) {
        UserEntity user = userService.findUserById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }

        List<PermissionEntity> permissions = userService.findPermissionsById(userId);
        List<PermissionTree> permissionTree = PermissionTreeBuilder.build(permissions);

        AuthResult result = new AuthResult();
        result.setUserId(userId);
        result.setUsername(user.getUsername());
        result.setPermissionTree(permissionTree);

        return result;
    }

    /** 通过用户名加载用户信息 */
    @Override
    public AuthResult loadUserByUsername(String username) {
        UserEntity user = userService.findUserByUsername(username);
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }

        return loadUserById(user.getId());
    }
}
