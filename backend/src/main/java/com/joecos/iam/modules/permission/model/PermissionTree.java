package com.joecos.iam.modules.permission.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PermissionTree {
    Integer id;
    String permissionCode;
    String permissionName;
    String permissionType;
    List<PermissionTree> children = new ArrayList<>();
}
