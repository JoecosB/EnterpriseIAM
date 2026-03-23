package com.joecos.iam.modules.permission.model.respond;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class PermissionDTO {
    private final Integer permissionId;
    private final String permissionName;
}
