package com.joecos.iam.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@TableName("sys_role_permission")
public class RolePermissionEntity {
    @TableId
    private final Integer roleId;
    private final Integer permissionId;
}
