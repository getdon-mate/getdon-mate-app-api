package com.api.app.getdonapi.meeting.repository;

import com.api.app.getdonapi.meeting.controller.response.MyMeetingListResponse;

import java.util.List;

public interface MeetingRepositoryCustom {
    List<MyMeetingListResponse> findMyMeetingList(Long userId);
}