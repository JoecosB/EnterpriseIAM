package com.joecos.iam.modules.user.service;

import com.joecos.iam.infrastructure.persistence.entity.*;
import com.joecos.iam.infrastructure.persistence.mapper.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.joecos.iam.modules.role.service.RoleService;
import com.joecos.iam.modules.user.model.UserDTO;
import com.joecos.iam.modules.user.model.requests.AssignUserRolesRequest;
import com.joecos.iam.modules.user.model.requests.CreateUserRequest;
import com.joecos.iam.modules.user.model.requests.UpdateUserRequest;
import com.joecos.iam.modules.user.model.requests.UpdateUserStatusRequest;
import com.joecos.iam.security.model.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
    private final RoleService roleService;

    /**
     * 通过用户名查询用户
     *
     * @param username 用户名
     * */
    @Override
    public UserEntity findUserByUsername(String username) {

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
    public UserEntity findUserById(Long userId) {
        return userMapper.selectById(userId);
    }

    /**
     * 查询用户角色
     *
     * @param userId 用户 ID
     * */
    @Override
    public List<RoleEntity> findRolesById(Long userId) {

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
     * 查询用户角色字符串
     *
     * @param userId 用户 ID
     *
     */
    @Override
    public List<String> findRoleNamesById(Long userId) {
        List<RoleEntity> roleEntities = findRolesById(userId);

        return roleEntities.stream()
                .map(RoleEntity::getRoleName)
                .toList();
    }

    /**
     * 查询用户权限
     *
     * @param userId 用户 ID
     * */
    @Override
    public List<PermissionEntity> findPermissionsById(Long userId) {

        // 获取用户角色
        List<RoleEntity> roles = findRolesById(userId);

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
    public List<String> findPermissionCodesById(Long userId) {

        List<PermissionEntity> permissions = findPermissionsById(userId);

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
    public Long findUserIdByUsername(String username) {
        UserEntity user = findUserByUsername(username);
        return user.getId();
    }

    /**
     * 通过用户 ID 查询用户名
     *
     * @param userId 用户 ID
     * */
    @Override
    public String findUsernameByUserId(Long userId) {
        UserEntity user = findUserById(userId);
        return user.getUsername();
    }

    /**
     * 检查用户名是否被占用
     *
     * @param username 用户名
     * */
    @Override
    public boolean checkUsernameExistence(String username) {
        return findUserByUsername(username) == null;
    }

    /**
     * 根据 ID 删除用户
     *
     * @param userId 用户 ID
     */
    @Override
    public void deleteUserById(Long userId) {
        UserEntity user = findUserById(userId);
        if (user == null) {
            throw new RuntimeException("User doesn't exist!");
        }

        if (user.getDeleted() == 1) {
            throw new RuntimeException("User already deleted!");
        }

        user.setDeleted(1);
        user.setUsername("deletedUser_" + userId);
        user.setEmail(null);
        userMapper.updateById(user);

    }

    /**
     * 更新用户身份组
     *
     * @param userId 用户 ID
     * @param roles  用户的新身份组列表
     *
     */
    @Override
    public void updateUserRoles(Long userId, List<Integer> roles) {
        LambdaQueryWrapper<UserRoleEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRoleEntity::getUserId, userId);
        userRoleMapper.delete(wrapper);

        for(Integer roleId : roles) {
            UserRoleEntity userRole = new UserRoleEntity();
            userRole.setUserId(userId);
            userRole.setRoleId(roleId);
            userRoleMapper.insert(userRole);
        }
    }

    /**
     * API-创建用户
     *
     * @param request CreateUserRequest DTO
     * */
    @Override
    public Long createUser(CreateUserRequest request) {
        String username = request.getUsername();
        String password = request.getPassword();

        if(!checkUsernameExistence(username)) {
            throw new RuntimeException("Username occupied!");
        }

        UserEntity newUser = new UserEntity();
        newUser.setUsername(username);
        newUser.setPassword(passwordEncoder.encode(password));
        userMapper.insert(newUser);

        return newUser.getId();
    }

    /**
     * API-查询用户列表
     *
     */
    @Override
    public List<UserDTO> getUserList() {
        List<UserEntity> userEntities = userMapper.selectList(null);

        List<UserDTO> userList = new ArrayList<>();
        for(UserEntity userEntity : userEntities) {
            UserDTO user = new UserDTO();
            user.setUserId(userEntity.getId());
            user.setUsername(userEntity.getUsername());
            user.setUserRole(findRoleNamesById(userEntity.getId()));

            userList.add(user);
        }

        return userList;
    }

    /**
     * API-通过 ID 查询单个用户
     *
     * @param userId 用户 ID
     *
     */
    @Override
    public UserDTO getUserById(Long userId) {
        UserEntity userEntity = findUserById(userId);
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(userEntity.getId());
        userDTO.setUsername(userEntity.getUsername());
        userDTO.setUserRole(findRoleNamesById(userId));

        return userDTO;
    }

    /**
     * API-修改用户信息
     *
     * @param userId  用户 ID
     * @param request 请求体
     *
     */
    @Override
    public void updateUserInfo(Long userId, UpdateUserRequest request) {
        UserEntity userEntity = findUserById(userId);
        String newUsername = request.getUsername();
        String newEmail = request.getEmail();
        String newPassword = request.getPassword();

        if (newUsername != null) {
            if (checkUsernameExistence(newUsername)) {
                throw new RuntimeException("Username Occupied");
            } else {
                userEntity.setUsername(newUsername);
            }
        }
        if (newPassword != null) {
            userEntity.setPassword(passwordEncoder.encode(newPassword));
        }
        if (newEmail != null) {
            userEntity.setEmail(newEmail);
        }

        userMapper.updateById(userEntity);
    }

    /**
     * API-删除用户
     *
     * @param userId 用户 ID
     * */
    public void deleteUser(Long userId) {
        UserPrincipal principal =(UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long currentUserId = principal.getUserId();

        if (Objects.equals(userId, currentUserId) || findPermissionCodesById(currentUserId).contains("user:delete")) {
            deleteUserById(userId);

        } else {
                 throw new RuntimeException("Permission not granted!");
        }
    }

    /**
     * API-更新用户状态
     *
     * @param userId 用户 ID
     * @param request UpdateUserStatusRequest
     *
     */
    @Override
    public void updateUserStatus(Long userId, UpdateUserStatusRequest request) {
        UserEntity user = findUserById(userId);
        Integer newStatus = request.getStatus();

        if (newStatus != 0 && newStatus != 1) {
            throw new RuntimeException("New status invalid!");
        }

        Integer oldStatus = user.getStatus();

        if (Objects.equals(oldStatus, newStatus)) {
            if (oldStatus == 1) {
                throw new RuntimeException("User already enabled!");
            } else {
                throw new RuntimeException("User already disabled!");
            }
        }

        user.setStatus(newStatus);
        userMapper.updateById(user);
    }

    /**
     * API-更新用户角色表
     *
     * @param userId  用户 ID
     * @param request AssignUserRolesRequest
     *
     */
    @Override
    public void assignUserRoles(Long userId, AssignUserRolesRequest request) {
        List<String> newRoles = request.getRoleNames();
        List<Integer> newRoleIds = new ArrayList<>();

        for(String roleName : newRoles) {
            Integer roleId = roleService.findByName(roleName).getId();
            newRoleIds.add(roleId);
        }

        updateUserRoles(userId, newRoleIds);
    }

}