package com.joecos.iam.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("sys_role")
public class RoleEntity {
    @TableId
    private Integer id;
    private Integer roleCode;
    private String roleName;
    private String description;
}
