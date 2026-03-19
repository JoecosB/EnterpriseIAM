package com.joecos.iam.modules.user.service;

import com.joecos.iam.infrastructure.persistence.entity.*;
import com.joecos.iam.modules.user.model.UserDTO;
import com.joecos.iam.modules.user.model.requests.CreateUserRequest;

import java.util.List;

public interface UserService {
    /**
     * 通过用户名查询用户
     *
     * @param username 用户名
     * */
    UserEntity findByUsername(String username);

    /**
     * 根据 ID 查询用户
     *
     * @param userId 用户 ID
     * */
    UserEntity findById(Long userId);

    /**
     * 查询用户角色
     *
     * @param userId 用户 ID
     * */
    List<RoleEntity> getUserRoles(Long userId);

    /**
     * 查询用户权限
     *
     * @param userId 用户 ID
     * */
    List<PermissionEntity> getUserPermissions(Long userId);

    /**
     * 获取用户权限字符串
     *
     * @param userId 用户 ID
     * */
    List<String> getUserPermissionString(Long userId);

    /**
     * 通过用户名查询用户 ID
     *
     * @param username 用户名
     * */
    Long getUserIdByUsername(String username);

    /**
     * 通过用户 ID 查询用户名
     *
     * @param userId 用户 ID
     * */
    String getUsernameByUserId(Long userId);

    /**
     * 创建用户
     *
     * @param request CreateUserRequest DTO
     * */
    Long createUser(CreateUserRequest request);

    /**
     * 查询用户列表
     * */
    List<UserDTO> getUserList();

    /**
     * 检查用户名是否被占用
     *
     * @param username 用户名
     * */
    boolean checkUsernameExistence(String username);
}
