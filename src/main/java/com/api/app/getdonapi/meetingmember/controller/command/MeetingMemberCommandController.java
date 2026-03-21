package com.api.app.getdonapi.meetingmember.controller.command;

import com.api.app.getdonapi.global.ApiPath;
import com.api.app.getdonapi.global.config.jwt.security.UserPrincipal;
import com.api.app.getdonapi.global.enums.ApiResponseCode;
import com.api.app.getdonapi.global.response.ApiResponse;
import com.api.app.getdonapi.meetingmember.controller.request.JoinMeetingRequest;
import com.api.app.getdonapi.meetingmember.service.command.MeetingMemberCommandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiPath.MeetingMember.ROOT)
public class MeetingMemberCommandController {
    private final MeetingMemberCommandService meetingMemberCommandService;

    @PostMapping(ApiPath.MeetingMember.JOIN)
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Void> joinMeeting(@RequestBody @Valid JoinMeetingRequest request, @AuthenticationPrincipal UserPrincipal user) {
        meetingMemberCommandService.joinMeeting(request, user.userId());
        return ApiResponse.of(ApiResponseCode.JOIN_MEETING_MEMBER_CREATED);
    }

    @DeleteMapping(ApiPath.MeetingMember.WITHDRAWAL)
    public ApiResponse<Void> withdrawalMember(@RequestParam @Valid Long meetingMemberId, @AuthenticationPrincipal UserPrincipal user) {
        meetingMemberCommandService.withdrawalMember(meetingMemberId, user.userId());
        return ApiResponse.of(ApiResponseCode.MEETING_MEMBER_WITHDRAWAL);
    }
}
