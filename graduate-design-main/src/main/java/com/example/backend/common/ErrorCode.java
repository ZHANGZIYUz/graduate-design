package com.example.backend.common;

/**
 * 错误码
 */
public enum ErrorCode {

    PARAMS_ERROR(400, "Invalid request parameters"),
    NULL_ERROR(401, "Empty request data"),
    INSERT_ERROR(402, "Invalid insert request"),
    ALLOCATE_ERROR(403, "Not all TimeTableEntries have been allocated"),
    ALLOCATE_WORKING_HOUR_ERROR(404, "TA's working hours exceed the allowed limit"),
    ALLOCATE_EMPLOYMENT_APPROVAL_ERROR(405, "This TA is not authorized to teach this module"),
    ALLOCATE_TIME_CONFLICT_ERROR(406, "Time Conflict"),
    ALLOCATE_SPACE_CONFLICT_ERROR(407, "Space Conflict"),
    ALLOCATE_TA_NUMBER_EACH_SESSION_ERROR(408,"Assigned TA count for this session is wrong");

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
