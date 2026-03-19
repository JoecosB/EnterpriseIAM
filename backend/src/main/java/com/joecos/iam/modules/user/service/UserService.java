package com.joecos.iam.modules.user.service;

import com.joecos.iam.infrastructure.persistence.entity.*;
import com.joecos.iam.modules.user.model.UserDTO;
import com.joecos.iam.modules.user.model.requests.CreateUserRequest;

import java.util.List;

public interface UserService {
    // 以下为后端使用方法
    /**
     * 通过用户名查询用户
     *
     * @param username 用户名
     * */
    UserEntity findUserByUsername(String username);

    /**
     * 根据 ID 查询用户
     *
     * @param userId 用户 ID
     * */
    UserEntity findUserById(Long userId);

    /**
     * 查询用户角色
     *
     * @param userId 用户 ID
     * */
    List<RoleEntity> findUserRoles(Long userId);

    /**
     * 查询用户角色字符串
     *
     * @param userId 用户 ID
     * */
    List<String> findUserRoleString(Long userId);

    /**
     * 查询用户权限
     *
     * @param userId 用户 ID
     * */
    List<PermissionEntity> findUserPermissions(Long userId);

    /**
     * 获取用户权限字符串
     *
     * @param userId 用户 ID
     * */
    List<String> findUserPermissionString(Long userId);

    /**
     * 通过用户名查询用户 ID
     *
     * @param username 用户名
     * */
    Long findUserIdByUsername(String username);

    /**
     * 通过用户 ID 查询用户名
     *
     * @param userId 用户 ID
     * */
    String findUsernameByUserId(Long userId);

    /**
     * 检查用户名是否被占用
     *
     * @param username 用户名
     * */
    boolean checkUsernameExistence(String username);


    // 以下为 API 使用方法
    /**
     * API-创建用户
     *
     * @param request CreateUserRequest DTO
     * */
    Long createUser(CreateUserRequest request);

    /**
     * API-查询用户列表
     * */
    List<UserDTO> getUserList();

    /**
     * API-通过 ID 查询单个用户
     *
     * @param userId 用户 ID
     * */
    UserDTO getUserById(Long userId);

}
