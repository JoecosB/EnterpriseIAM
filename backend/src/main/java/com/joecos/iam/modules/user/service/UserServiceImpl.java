package com.joecos.iam.modules.user.service;

import com.joecos.iam.common.constant.ErrorCode;
import com.joecos.iam.common.exception.BusinessException;
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
    private final RoleService roleService;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * 通过用户名查询用户
     *
     * @param username 用户名
     * */
    @Override
    public UserEntity findUserByUsername(String username) {
        if (username == null || username.isBlank()) {
            return null;
        }

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
        if (userId == null) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST, "User id cannot be null");
        }
        return userMapper.selectById(userId);
    }

    /**
     * 查询用户角色
     *
     * @param userId 用户 ID
     * */
    @Override
    public List<RoleEntity> findRolesById(Long userId) {
        requireUser(userId);

        LambdaQueryWrapper<UserRoleEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRoleEntity::getUserId, userId);

        List<UserRoleEntity> userRoles = userRoleMapper.selectList(wrapper);

        if (userRoles.isEmpty()) {
            return new ArrayList<>();
        }

        List<Integer> roleIds = userRoles.stream()
                .map(UserRoleEntity::getRoleId)
                .collect(Collectors.toList());

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
        requireUser(userId);

        List<RoleEntity> roles = findRolesById(userId);

        if (roles.isEmpty()) {
            return new ArrayList<>();
        }

        List<Integer> roleIds = roles.stream()
                .map(RoleEntity::getId)
                .collect(Collectors.toList());

        LambdaQueryWrapper<RolePermissionEntity> rolePermissionWrapper =
                new LambdaQueryWrapper<>();

        rolePermissionWrapper.in(RolePermissionEntity::getRoleId, roleIds);

        List<RolePermissionEntity> rolePermissions =
                rolePermissionMapper.selectList(rolePermissionWrapper);

        if (rolePermissions.isEmpty()) {
            return new ArrayList<>();
        }

        List<Integer> permissionIds = rolePermissions.stream()
                .map(RolePermissionEntity::getPermissionId)
                .collect(Collectors.toList());

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
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        return user.getId();
    }

    /**
     * 通过用户 ID 查询用户名
     *
     * @param userId 用户 ID
     * */
    @Override
    public String findUsernameByUserId(Long userId) {
        UserEntity user = requireUser(userId);
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
        UserEntity user = requireUser(userId);

        if (user.getDeleted() != null && user.getDeleted() == 1) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST, "User already deleted");
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
        requireUser(userId);

        LambdaQueryWrapper<UserRoleEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRoleEntity::getUserId, userId);
        userRoleMapper.delete(wrapper);

        if (roles == null || roles.isEmpty()) {
            return;
        }

        for (Integer roleId : roles) {
            if (roleService.findById(roleId) == null) {
                throw new BusinessException(ErrorCode.ROLE_NOT_FOUND);
            }

            UserRoleEntity userRole = new UserRoleEntity();
            userRole.setUserId(userId);
            userRole.setRoleId(roleId);
            userRoleMapper.insert(userRole);
        }
    }

    /**
     * 校验用户是否存在。
     *
     * @param userId 用户 ID
     */
    private void assertUserExists(Long userId) {
        if (findUserById(userId) == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
    }

    /**
     * 查询并返回指定用户；若用户不存在则抛出异常。
     *
     * @param userId 用户 ID
     */
    private UserEntity requireUser(Long userId) {
        UserEntity user = findUserById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        return user;
    }

    /**
     * API-创建用户
     *
     * @param request CreateUserRequest DTO
     * */
    @Override
    public Long createUser(CreateUserRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST, "Request cannot be null");
        }

        String username = request.getUsername();
        String password = request.getPassword();

        if (username == null || username.isBlank()) {
            throw new BusinessException(ErrorCode.INVALID_USERNAME);
        }

        if (password == null || password.isBlank()) {
            throw new BusinessException(ErrorCode.INVALID_PASSWORD);
        }

        if (!checkUsernameExistence(username)) {
            throw new BusinessException(ErrorCode.USERNAME_OCCUPIED);
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
        for (UserEntity userEntity : userEntities) {
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
        UserEntity userEntity = requireUser(userId);

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
        if (request == null) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST, "Request cannot be null");
        }

        UserEntity userEntity = requireUser(userId);
        String newUsername = request.getUsername();
        String newEmail = request.getEmail();
        String newPassword = request.getPassword();

        if (newUsername != null && !newUsername.isBlank()) {
            UserEntity existedUser = findUserByUsername(newUsername);
            if (existedUser != null && !Objects.equals(existedUser.getId(), userId)) {
                throw new BusinessException(ErrorCode.USERNAME_OCCUPIED);
            }
            userEntity.setUsername(newUsername);
        }

        if (newPassword != null && !newPassword.isBlank()) {
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
    @Override
    public void deleteUser(Long userId) {
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        Long currentUserId = principal.getUserId();

        if (Objects.equals(userId, currentUserId)
                || findPermissionCodesById(currentUserId).contains("user:delete")) {
            deleteUserById(userId);
        } else {
            throw new BusinessException(ErrorCode.PERMISSION_DENIED);
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
        if (request == null) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST, "Request cannot be null");
        }

        UserEntity user = requireUser(userId);
        Integer newStatus = request.getStatus();

        if (newStatus == null || (newStatus != 0 && newStatus != 1)) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST, "New status invalid");
        }

        Integer oldStatus = user.getStatus();

        if (Objects.equals(oldStatus, newStatus)) {
            if (oldStatus == 1) {
                throw new BusinessException(ErrorCode.USER_ALREADY_ENABLED);
            } else {
                throw new BusinessException(ErrorCode.USER_ALREADY_DISABLED);
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
        if (request == null) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST, "Request cannot be null");
        }

        requireUser(userId);

        List<String> newRoles = request.getRoleNames();
        List<Integer> newRoleIds = new ArrayList<>();

        if (newRoles == null || newRoles.isEmpty()) {
            updateUserRoles(userId, new ArrayList<>());
            return;
        }

        for (String roleName : newRoles) {
            RoleEntity role = roleService.findByName(roleName);
            if (role == null) {
                throw new BusinessException(ErrorCode.ROLE_NOT_FOUND);
            }
            newRoleIds.add(role.getId());
        }

        updateUserRoles(userId, newRoleIds);
    }
}