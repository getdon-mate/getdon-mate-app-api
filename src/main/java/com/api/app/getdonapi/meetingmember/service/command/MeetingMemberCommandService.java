package com.api.app.getdonapi.meetingmember.service.command;

import com.api.app.getdonapi.meetingmember.controller.request.JoinMeetingRequest;
import jakarta.validation.Valid;

public interface MeetingMemberCommandService {
    void joinMeeting(@Valid JoinMeetingRequest request, Long userId);
}
