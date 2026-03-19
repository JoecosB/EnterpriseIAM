package com.joecos.iam.modules.permission.model;

import lombok.Getter;

@Getter
public enum PermissionType {

    MENU("MENU"),
    BUTTON("BUTTON"),
    API("API");

    private final String code;

    PermissionType(String code) {
        this.code = code;
    }

    /**
     * 根据 code 获取对应的 PermissionType
     *
     * @param code 权限类型
     *
     */
    public static PermissionType fromCode(String code) {
        if (code == null) {
            throw new IllegalArgumentException("PermissionType code cannot be null");
        }

        for (PermissionType type : PermissionType.values()) {
            if (type.getCode().equalsIgnoreCase(code)) {
                return type;
            }
        }

        throw new IllegalArgumentException("Unknown PermissionType code: " + code);
    }
}