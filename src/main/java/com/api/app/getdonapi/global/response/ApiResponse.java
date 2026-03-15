package com.api.app.getdonapi.global.response;

import com.api.app.getdonapi.global.enums.ApiResponseCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiResponse<T> {
    private final int status;
    private final String message;
    private final T data;

    private ApiResponse(HttpStatus httpStatus, String message, T data) {
        this.status = httpStatus.value();
        this.message = message;
        this.data = data;
    }

    // ApiResponseCode로 생성
    public static <T> ApiResponse<T> of(ApiResponseCode code, T data) {
        return new ApiResponse<>(code.getHttpStatus(), code.getMessage(), data);
    }

    public static <T> ApiResponse<T> of(ApiResponseCode code) {
        return new ApiResponse<>(code.getHttpStatus(), code.getMessage(), null);
    }

    // 단순 조회용 편의 메서드
    public static <T> ApiResponse<T> ok(T data) {
        return of(ApiResponseCode.SUCCESS, data);
    }
}