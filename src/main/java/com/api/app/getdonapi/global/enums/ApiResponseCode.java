package com.api.app.getdonapi.global.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ApiResponseCode {
    SIGN_UP(201, "S001", "회원가입이 정상적으로 완료됐습니다."),
    ;

    private int status;
    private final String code;
    private final String message;

    ApiResponseCode(final int status, final String code, final String message) {
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
        for (ApiResponseCode responseCode : ApiResponseCode.values()) {
            if (code.equals(responseCode.code)) {
                return responseCode.getMessage();
            }
        }
        return null;
    }
}
