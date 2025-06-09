package com.example.backend.common;

/**
 * 错误码
 */
public enum ErrorCode {

    PARAMS_ERROR(400, "Invalid request parameters"),
    NULL_ERROR(401, "Empty request data"),
    INSERT_ERROR(402, "Invalid insert request"),
    ALLOCATE_ERROR(403, "Not all TimeTableEntries have been allocated");

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
