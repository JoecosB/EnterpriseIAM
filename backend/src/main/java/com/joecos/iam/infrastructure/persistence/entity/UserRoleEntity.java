package com.joecos.iam.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("sys_user_role")
public class UserRoleEntity {
    @TableId
    private Long userId;
    private Integer roleId;
}
