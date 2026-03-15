package com.api.app.getdonapi.global.response;

import com.api.app.getdonapi.global.enums.ApiResponseCode;
import lombok.Getter;

@Getter
public class ApiResponse {
    private int status;
    private Object result;
    private String code;
    private String message;

    public ApiResponse(int httpCode, Object result, String code) {
        this.status = httpCode;
        this.result = result;
        this.code = code;
        this.message = ApiResponseCode.getCode(code);
    }
}
