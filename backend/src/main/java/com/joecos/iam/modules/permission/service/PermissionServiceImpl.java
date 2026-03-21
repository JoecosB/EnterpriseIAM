package com.joecos.iam.modules.permission.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.joecos.iam.infrastructure.persistence.entity.PermissionEntity;
import com.joecos.iam.infrastructure.persistence.mapper.PermissionMapper;
import com.joecos.iam.modules.permission.model.PermissionTree;
import com.joecos.iam.modules.permission.model.PermissionTreeBuilder;
import com.joecos.iam.modules.permission.model.respond.PermissionDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PermissionServiceImpl implements PermissionService {
    private final PermissionMapper permissionMapper;

    /**
     * 根据 ID 查询权限
     *
     * @param permissionId 权限 ID
     * */
    @Override
    public PermissionEntity findById(Integer permissionId) {

        return permissionMapper.selectById(permissionId);
    }

    /**
     * 根据多个 ID 查询权限
     *
     * @param permissionIds 权限 ID 列表
     * */
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
     * */
    @Override
    public List<PermissionEntity> findAllPermissions() {

        return permissionMapper.selectList(null);
    }

    /**
     * 根据单个权限 ID 查询权限代码
     *
     * @param permissionId 权限 ID
     * */
    @Override
    public String findPermissionCodeById(Integer permissionId) {
        LambdaQueryWrapper<PermissionEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PermissionEntity::getId, permissionId);
        PermissionEntity permission = permissionMapper.selectById(wrapper);

        return permission.getPermissionCode();
    }

    /**
     * 根据多个权限 ID 查询权限代码
     *
     * @param permissionIds 权限 ID 列表
     * */
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
     * 返回当前系统权限树
     * */
    @Override
    public List<PermissionTree> findFullPermissionTree() {
        List<PermissionEntity> permissions = findAllPermissions();

        return PermissionTreeBuilder.build(permissions);
    }

    /**
     * API-查询完整权限列表
     *
     */
    @Override
    public List<PermissionDTO> getAllPermissions() {

        return findAllPermissions().stream()
                .map(permission ->
                        new PermissionDTO(permission.getId(), permission.getPermissionName())
                ).toList();
    }
}