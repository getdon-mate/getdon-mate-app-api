package com.api.app.getdonapi.meetingmember.service.command;

import com.api.app.getdonapi.meeting.domain.Meeting;
import com.api.app.getdonapi.meetingmember.controller.request.JoinMeetingRequest;
import com.api.app.getdonapi.meetingmember.enums.MeetingRole;
import com.api.app.getdonapi.member.domain.User;
import jakarta.validation.Valid;

public interface MeetingMemberCommandService {
    void joinMeeting(@Valid JoinMeetingRequest request, Long userId);
    void addMember(User user, Meeting meeting, MeetingRole role);
    void withdrawalMember(Long meetingMemberId, Long requesterId);
}
