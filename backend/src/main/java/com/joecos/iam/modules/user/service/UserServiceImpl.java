package com.joecos.iam.modules.user.service;

import com.joecos.iam.common.exception.BusinessException;
import com.joecos.iam.infrastructure.persistence.entity.*;
import com.joecos.iam.infrastructure.persistence.mapper.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.joecos.iam.modules.user.model.CreateUserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final UserRoleMapper userRoleMapper;
    private final RoleMapper roleMapper;
    private final RolePermissionMapper rolePermissionMapper;
    private final PermissionMapper permissionMapper;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * 通过用户名查询用户
     *
     * @param username 用户名
     * */
    @Override
    public UserEntity findByUsername(String username) {

        LambdaQueryWrapper<UserEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserEntity::getUsername, username)
                .eq(UserEntity::getDeleted, 0);

        return userMapper.selectOne(wrapper);
    }

    /**
     * 根据 ID 查询用户
     *
     * @param userId 用户 ID
     * */
    @Override
    public UserEntity findById(Long userId) {
        return userMapper.selectById(userId);
    }

    /**
     * 查询用户角色
     *
     * @param userId 用户 ID
     * */
    @Override
    public List<RoleEntity> getUserRoles(Long userId) {

        // 查询 user_role 关系
        LambdaQueryWrapper<UserRoleEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRoleEntity::getUserId, userId);

        List<UserRoleEntity> userRoles = userRoleMapper.selectList(wrapper);

        if (userRoles.isEmpty()) {
            return new ArrayList<>();
        }

        // 提取 roleId
        List<Integer> roleIds = userRoles.stream()
                .map(UserRoleEntity::getRoleId)
                .collect(Collectors.toList());

        // 批量查询 role
        LambdaQueryWrapper<RoleEntity> roleWrapper = new LambdaQueryWrapper<>();
        roleWrapper.in(RoleEntity::getId, roleIds);

        return roleMapper.selectList(roleWrapper);
    }

    /**
     * 查询用户权限
     *
     * @param userId 用户 ID
     * */
    @Override
    public List<PermissionEntity> getUserPermissions(Long userId) {

        // 获取用户角色
        List<RoleEntity> roles = getUserRoles(userId);

        if (roles.isEmpty()) {
            return new ArrayList<>();
        }

        // 提取 roleId
        List<Integer> roleIds = roles.stream()
                .map(RoleEntity::getId)
                .collect(Collectors.toList());

        // 查询 role_permission
        LambdaQueryWrapper<RolePermissionEntity> rolePermissionWrapper =
                new LambdaQueryWrapper<>();

        rolePermissionWrapper.in(RolePermissionEntity::getRoleId, roleIds);

        List<RolePermissionEntity> rolePermissions =
                rolePermissionMapper.selectList(rolePermissionWrapper);

        if (rolePermissions.isEmpty()) {
            return new ArrayList<>();
        }

        // 提取 permissionId
        List<Integer> permissionIds = rolePermissions.stream()
                .map(RolePermissionEntity::getPermissionId)
                .collect(Collectors.toList());

        // 查询 permission
        LambdaQueryWrapper<PermissionEntity> permissionWrapper =
                new LambdaQueryWrapper<>();

        permissionWrapper.in(PermissionEntity::getId, permissionIds);

        return permissionMapper.selectList(permissionWrapper);
    }

    /**
     * 获取用户权限字符串
     *
     * @param userId 用户 ID
     * */
    @Override
    public List<String> getUserPermissionString(Long userId) {

        List<PermissionEntity> permissions = getUserPermissions(userId);

        return permissions.stream()
                .map(PermissionEntity::getPermissionCode)
                .collect(Collectors.toList());
    }

    /**
     * 通过用户名查询用户 ID
     *
     * @param username 用户名
     * */
    @Override
    public Long getUserIdByUsername(String username) {
        UserEntity user = findByUsername(username);
        return user.getId();
    }

    /**
     * 通过用户 ID 查询用户名
     *
     * @param userId 用户 ID
     * */
    @Override
    public String getUsernameByUserId(Long userId) {
        UserEntity user = findById(userId);
        return user.getUsername();
    }

    /**
     * 创建用户
     *
     * @param request CreateUserRequest DTO
     * */
    @Override
    public Long createUser(CreateUserRequest request) {
        String username = request.getUsername();
        String password = request.getPassword();

        if(!checkUsernameAvailable(username)) {
            throw new RuntimeException("Username occupied!");
        }

        UserEntity newUser = new UserEntity();
        newUser.setUsername(username);
        newUser.setPassword(passwordEncoder.encode(password));
        userMapper.insert(newUser);

        return newUser.getId();
    }

    /**
     * 检查用户名是否被占用
     *
     * @param username 用户名
     * */
    @Override
    public boolean checkUsernameAvailable(String username) {
        return findByUsername(username) == null;
    }
}