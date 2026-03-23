package com.joecos.iam.modules.role.model.request;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class UpdateRoleInfoRequest {
    private final String roleName;
    private final String roleDesc;
}
