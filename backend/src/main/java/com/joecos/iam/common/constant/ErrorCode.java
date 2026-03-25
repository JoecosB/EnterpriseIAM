package com.joecos.iam.common.constant;

public enum ErrorCode {
    // 通用错误
    SYSTEM_ERROR(5000, "System error"),
    INVALID_REQUEST(4000, "Invalid request"),

    // user 模块错误
    USER_NOT_FOUND(1001, "User not found"),
    USERNAME_OCCUPIED(1002, "Username occupied"),
    USER_ALREADY_ENABLED(1003, "User already enabled"),
    USER_ALREADY_DISABLED(1004, "User already disabled"),

    // role 模块错误
    ROLE_NOT_FOUND(2001, "Role not found"),
    ROLE_NAME_OCCUPIED(2002, "Role name occupied"),
    ROLE_CODE_OCCUPIED(2003, "Role code occupied"),

    // permission 模块错误
    PERMISSION_NOT_FOUND(3001, "Permission not found"),
    PERMISSION_DENIED(3002, "Permission denied"),

    // auth 模块错误
    LOGIN_FAILED(4001, "Invalid username or password"),
    UNAUTHORIZED(4002, "Unauthorized"),
    INVALID_USERNAME(4003, "Invalid username"),
    INVALID_EMAIL(4004, "Invalid email"),
    INVALID_PASSWORD(4005, "Invalid password");

    private final Integer code;
    private final String message;

    ErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}