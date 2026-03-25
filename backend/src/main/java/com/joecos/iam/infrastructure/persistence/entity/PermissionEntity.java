package com.joecos.iam.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("sys_permission")
public class PermissionEntity {
    @TableId
    private Integer id;
    private Integer parentId;
    private String permissionName;
    private String permissionCode;
    private String type;
    private String path;
    private String component;
    private LocalDateTime createTime;
}
