package com.api.app.getdonapi.member.controller.command;

import com.api.app.getdonapi.global.ApiPath;
import com.api.app.getdonapi.global.enums.ApiResponseCode;
import com.api.app.getdonapi.global.response.ApiResponse;
import com.api.app.getdonapi.member.controller.request.UserLoginRequest;
import com.api.app.getdonapi.member.controller.request.UserJoinRequest;
import com.api.app.getdonapi.member.controller.response.UserLoginResponse;
import com.api.app.getdonapi.member.service.command.MemberCommandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiPath.Member.ROOT)
public class MemberCommandController {
    private final MemberCommandService memberCommandService;

    @PostMapping(ApiPath.Member.JOIN)
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Void> join(@RequestBody @Valid UserJoinRequest request) {
        memberCommandService.createMember(request);
        return ApiResponse.of(ApiResponseCode.MEMBER_CREATED);
    }

    @PostMapping(ApiPath.Member.LOGIN)
    public ApiResponse<UserLoginResponse> login(@RequestBody @Valid UserLoginRequest request) {
        return ApiResponse.of(ApiResponseCode.MEMBER_LOGIN, memberCommandService.login(request));
    }
}