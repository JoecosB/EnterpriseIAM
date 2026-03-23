package com.joecos.iam.modules.role.model.request;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CreateRoleRequest {
    private final String roleName;
    private final Integer roleCode;
    private final String roleDesc;
}
