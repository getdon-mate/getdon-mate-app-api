package com.api.app.getdonapi.meeting.controller.query;

import com.api.app.getdonapi.global.ApiPath;
import com.api.app.getdonapi.global.config.jwt.security.UserPrincipal;
import com.api.app.getdonapi.global.enums.ApiResponseCode;
import com.api.app.getdonapi.global.response.ApiResponse;
import com.api.app.getdonapi.meeting.controller.response.MyMeetingListResponse;
import com.api.app.getdonapi.meeting.service.query.MeetingQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiPath.Meeting.ROOT)
public class MeetingQueryController {
    private final MeetingQueryService meetingQueryService;

    @GetMapping(ApiPath.Meeting.MY_LIST)
    public ApiResponse<List<MyMeetingListResponse>> myList(@AuthenticationPrincipal UserPrincipal user) {
        return ApiResponse.of(ApiResponseCode.SUCCESS, meetingQueryService.getMyList(user.userId()));
    }
}
