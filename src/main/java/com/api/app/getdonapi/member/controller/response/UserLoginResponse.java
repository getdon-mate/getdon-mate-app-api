package com.api.app.getdonapi.member.controller.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserLoginResponse {
    private String accessToken;
    private String refreshToken;
}