package com.joecos.iam.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("sys_role")
public class RoleEntity {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer roleCode;
    private String roleName;
    private String description;
}
