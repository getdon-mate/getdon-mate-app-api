package com.api.app.getdonapi.meetingmember.controller.response;

import com.api.app.getdonapi.meetingmember.enums.MeetingRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MeetingMemberListResponse {
    private Long meetingMemberId;
    private Long userId;
    private String userName;
    private String profileUrl;
    private MeetingRole role;
}
