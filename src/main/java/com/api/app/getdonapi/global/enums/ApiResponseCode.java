package com.api.app.getdonapi.global.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
@RequiredArgsConstructor
public enum ApiResponseCode {
    SUCCESS(HttpStatus.OK, "정상 처리됐습니다."),

    MEMBER_CREATED(HttpStatus.CREATED, "회원가입이 정상적으로 완료됐습니다."),
    MEMBER_LOGIN(HttpStatus.OK, "로그인이 정상적으로 완료됐습니다."),

    MEETING_CREATED(HttpStatus.CREATED, "모임 통장 개설 완료되었습니다."),

    JOIN_MEETING_MEMBER_CREATED(HttpStatus.CREATED, "모임 통장에 초대되었습니다."),
    MEETING_MEMBER_WITHDRAWAL(HttpStatus.OK, "모임 멤버가 탈퇴 처리되었습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
