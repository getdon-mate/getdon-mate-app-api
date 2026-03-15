package com.api.app.getdonapi.global.enums;

public enum ErrorCode {
    UNAUTHORIZED(401, "E010", "권한이 없습니다. 로그인이 필요합니다."),
    INVALID_INPUT(400, "E001", "잘못된 입력값입니다."),
    DUPLICATE_EMAIL(409, "M001", "이미 사용 중인 이메일입니다."),
    MEMBER_NOT_FOUND(404, "M002", "존재하지 않는 회원입니다."),
    INVALID_PASSWORD(401, "M003", "비밀번호가 올바르지 않습니다."),
    INTERNAL_SERVER_ERROR(500, "E999", "서버 내부 오류가 발생했습니다."),
    ;

    private int status;
    private final String code;
    private final String message;

    ErrorCode(final int status, final String code, final String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return this.message;
    }

    public static String getCode(String code) {
        for (ErrorCode errorCode : ErrorCode.values()) {
            if (code.equals(errorCode.code)) {
                return errorCode.getMessage();
            }
        }
        return null;
    }
}
