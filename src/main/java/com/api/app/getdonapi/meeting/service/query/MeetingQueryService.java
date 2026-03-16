package com.api.app.getdonapi.meeting.service.query;

import com.api.app.getdonapi.meeting.controller.response.MyMeetingListResponse;

import java.util.List;

public interface MeetingQueryService {
    List<MyMeetingListResponse> getMyList(Long userId);
}
