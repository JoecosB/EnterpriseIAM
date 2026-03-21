package com.joecos.iam.modules.user.service;

import com.joecos.iam.infrastructure.persistence.entity.*;
import com.joecos.iam.modules.user.model.UserDTO;
import com.joecos.iam.modules.user.model.requests.CreateUserRequest;
import com.joecos.iam.modules.user.model.requests.UpdateUserRequest;
import com.joecos.iam.modules.user.model.requests.UpdateUserStatusRequest;

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
    List<RoleEntity> findRolesById(Long userId);

    /**
     * 查询用户角色字符串
     *
     * @param userId 用户 ID
     * */
    List<String> findRoleNamesById(Long userId);

    /**
     * 查询用户权限
     *
     * @param userId 用户 ID
     * */
    List<PermissionEntity> findPermissionsById(Long userId);

    /**
     * 获取用户权限字符串
     *
     * @param userId 用户 ID
     * */
    List<String> findPermissionCodesById(Long userId);

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

    /**
     * 根据 ID 删除用户
     *
     * @param userId 用户 ID
      */
    void deleteUserById(Long userId);


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

    /**
     * API-修改用户信息
     *
     * @param userId 用户 ID
     * @param request 请求体
     * */
    void updateUserInfo(Long userId, UpdateUserRequest request);

    /**
     * API-删除用户
     *
     * @param userId 用户 ID
     * */
    void deleteUser(Long userId);

    /**
     * API-更新用户状态
     *
     * @param userId 用户 ID
     * @param request UpdateUserStatusRequest
     * */
    void updateUserStatus(Long userId, UpdateUserStatusRequest request);

}
