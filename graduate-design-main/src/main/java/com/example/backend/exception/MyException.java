package com.example.backend.exception;

import com.example.backend.common.ErrorCode;
import lombok.Data;

/**
 * 自定义异常类
 */
@Data
public class MyException extends RuntimeException {
    private final int code;
    private String message;

    public MyException(ErrorCode errorCode, String message) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.message = message;
    }

}
