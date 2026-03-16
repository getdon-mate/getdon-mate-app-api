package com.api.app.getdonapi.meeting.controller.command;

import com.api.app.getdonapi.global.ApiPath;
import com.api.app.getdonapi.global.config.jwt.security.UserPrincipal;
import com.api.app.getdonapi.global.enums.ApiResponseCode;
import com.api.app.getdonapi.global.response.ApiResponse;
import com.api.app.getdonapi.meeting.controller.request.CreateMeetingRequest;
import com.api.app.getdonapi.meeting.service.command.MeetingCommandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiPath.Meeting.ROOT)
public class MeetingCommandController {
    private final MeetingCommandService meetingCommandService;

    @PostMapping(ApiPath.Meeting.CREATE)
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Void> createMeeting(@AuthenticationPrincipal UserPrincipal user, @Valid @RequestBody CreateMeetingRequest request) {
        meetingCommandService.createMeeting(user.userId(), request);
        return ApiResponse.of(ApiResponseCode.MEETING_CREATED);
    }
}
