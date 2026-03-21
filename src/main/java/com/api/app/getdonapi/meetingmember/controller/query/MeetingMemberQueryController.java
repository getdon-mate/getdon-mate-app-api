package com.api.app.getdonapi.meetingmember.controller.query;

import com.api.app.getdonapi.global.ApiPath;
import com.api.app.getdonapi.global.enums.ApiResponseCode;
import com.api.app.getdonapi.global.response.ApiResponse;
import com.api.app.getdonapi.meetingmember.controller.response.MeetingMemberListResponse;
import com.api.app.getdonapi.meetingmember.service.query.MeetingMemberQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiPath.MeetingMember.ROOT)
public class MeetingMemberQueryController {
    private final MeetingMemberQueryService meetingMemberQueryService;

    @GetMapping(ApiPath.MeetingMember.MEMBER_LIST)
    public ApiResponse<List<MeetingMemberListResponse>> memberList (@RequestParam Long meetingId) {
        return ApiResponse.of(ApiResponseCode.SUCCESS, meetingMemberQueryService.getMemberList(meetingId));
    }
}
