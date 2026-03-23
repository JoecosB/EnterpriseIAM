package com.joecos.iam.modules.role.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class RoleDTO {
    private final Integer roleId;
    private final String roleName;
    private final String roleDesc;
}
