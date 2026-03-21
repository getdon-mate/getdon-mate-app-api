package com.api.app.getdonapi.meetingmember.service.query;

import com.api.app.getdonapi.meetingmember.controller.response.MeetingMemberListResponse;

import java.util.List;

public interface MeetingMemberQueryService {
    List<MeetingMemberListResponse> getMemberList(Long meetingId);
}
