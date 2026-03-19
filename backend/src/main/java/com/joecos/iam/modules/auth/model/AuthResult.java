package com.joecos.iam.modules.auth.model;

import com.joecos.iam.modules.permission.model.PermissionTree;
import lombok.Data;

import java.util.List;

@ Data
public class AuthResult {
    private Long userId;
    private String username;
    private List<PermissionTree> permissionTree;
}
