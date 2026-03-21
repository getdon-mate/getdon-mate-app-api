package com.api.app.getdonapi.meetingmember.repository;

import com.api.app.getdonapi.meetingmember.controller.response.MeetingMemberListResponse;

import java.util.List;

public interface MeetingMemberRepositoryCustom {
    List<MeetingMemberListResponse> findMemberList(Long meetingId);
}
