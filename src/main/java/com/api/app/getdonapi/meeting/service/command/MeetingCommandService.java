package com.api.app.getdonapi.meeting.service.command;

import com.api.app.getdonapi.meeting.controller.request.CreateMeetingRequest;

public interface MeetingCommandService {
    void createMeeting(Long userId, CreateMeetingRequest request);
}
