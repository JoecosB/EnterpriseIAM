package com.joecos.iam.modules.role.model.request;

import lombok.Data;

import java.util.List;

@Data
public class AssignRolePermissionRequest {
    List<String> permissionCodes;
}
