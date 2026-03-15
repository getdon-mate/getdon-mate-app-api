package com.api.app.getdonapi.member.service.command;

import com.api.app.getdonapi.member.controller.request.UserLoginRequest;
import com.api.app.getdonapi.member.controller.request.UserJoinRequest;
import com.api.app.getdonapi.member.controller.response.UserLoginResponse;
import jakarta.validation.Valid;

public interface MemberCommandService {
    void createMember(@Valid UserJoinRequest request);
    UserLoginResponse login(@Valid UserLoginRequest request);
}