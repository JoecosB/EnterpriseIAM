package com.joecos.iam.common.exception;


import com.joecos.iam.common.constant.ErrorCode;
import lombok.Getter;

public class BusinessException extends RuntimeException {

    @Getter
    private final Integer code;
    private final String message;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }

    public BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.code = errorCode.getCode();
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}