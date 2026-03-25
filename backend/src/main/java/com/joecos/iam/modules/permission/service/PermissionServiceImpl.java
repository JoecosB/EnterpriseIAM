package com.joecos.iam.modules.permission.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.joecos.iam.common.constant.ErrorCode;
import com.joecos.iam.common.exception.BusinessException;
import com.joecos.iam.infrastructure.persistence.entity.PermissionEntity;
import com.joecos.iam.infrastructure.persistence.mapper.PermissionMapper;
import com.joecos.iam.modules.permission.model.PermissionTree;
import com.joecos.iam.modules.permission.model.PermissionTreeBuilder;
import com.joecos.iam.modules.permission.model.respond.PermissionDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PermissionServiceImpl implements PermissionService {
    private final PermissionMapper permissionMapper;

    /**
     * 根据 ID 查询权限
     *
     * @param permissionId 权限 ID
     */
    @Override
    public PermissionEntity findById(Integer permissionId) {
        if (permissionId == null) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST, "Permission id cannot be null");
        }

        return permissionMapper.selectById(permissionId);
    }

    /**
     * 根据多个 ID 查询权限
     *
     * @param permissionIds 权限 ID 列表
     */
    @Override
    public List<PermissionEntity> findByIds(List<Integer> permissionIds) {
        if (permissionIds == null || permissionIds.isEmpty()) {
            return new ArrayList<>();
        }

        LambdaQueryWrapper<PermissionEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(PermissionEntity::getId, permissionIds);

        return permissionMapper.selectList(wrapper);
    }

    /**
     * 查询全部权限
     */
    @Override
    public List<PermissionEntity> findAllPermissions() {
        return permissionMapper.selectList(null);
    }

    /**
     * 根据单个权限 ID 查询权限代码
     *
     * @param permissionId 权限 ID
     */
    @Override
    public String findPermissionCodeById(Integer permissionId) {
        PermissionEntity permission = findById(permissionId);

        if (permission == null) {
            throw new BusinessException(ErrorCode.PERMISSION_NOT_FOUND);
        }

        return permission.getPermissionCode();
    }

    /**
     * 根据多个权限 ID 查询权限代码
     *
     * @param permissionIds 权限 ID 列表
     */
    @Override
    public List<String> findPermissionCodeByIds(List<Integer> permissionIds) {
        if (permissionIds == null || permissionIds.isEmpty()) {
            return new ArrayList<>();
        }

        LambdaQueryWrapper<PermissionEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(PermissionEntity::getId, permissionIds);

        List<PermissionEntity> permissions = permissionMapper.selectList(wrapper);

        List<String> codes = new ArrayList<>();
        for (PermissionEntity permission : permissions) {
            codes.add(permission.getPermissionCode());
        }

        return codes;
    }

    /**
     * 通过权限代码查询权限 ID
     *
     * @param permissionCode 权限代码
     */
    @Override
    public Integer findPermissionIdByCode(String permissionCode) {
        if (permissionCode == null || permissionCode.isBlank()) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST, "Permission code cannot be blank");
        }

        LambdaQueryWrapper<PermissionEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PermissionEntity::getPermissionCode, permissionCode);

        PermissionEntity permission = permissionMapper.selectOne(wrapper);
        if (permission == null) {
            throw new BusinessException(ErrorCode.PERMISSION_NOT_FOUND);
        }

        return permission.getId();
    }

    /**
     * 返回当前系统权限树
     */
    @Override
    public List<PermissionTree> findFullPermissionTree() {
        List<PermissionEntity> permissions = findAllPermissions();
        return PermissionTreeBuilder.build(permissions);
    }

    /**
     * API-查询完整权限列表
     */
    @Override
    public List<PermissionDTO> getAllPermissions() {
        return findAllPermissions().stream()
                .map(permission -> new PermissionDTO(
                        permission.getId(),
                        permission.getPermissionName()
                ))
                .toList();
    }

    /**
     * API-通过 ID 查询单个权限
     *
     * @param permissionId 权限 ID
     */
    @Override
    public PermissionDTO getPermissionById(Integer permissionId) {
        PermissionEntity permission = findById(permissionId);

        if (permission == null) {
            throw new BusinessException(ErrorCode.PERMISSION_NOT_FOUND);
        }

        return new PermissionDTO(permission.getId(), permission.getPermissionName());
    }

    /**
     * API-查询系统完整权限树
     */
    @Override
    public List<PermissionTree> getFullPermissionTree() {
        return findFullPermissionTree();
    }
}