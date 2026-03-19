package com.api.app.getdonapi.meetingmember.service.command;

import com.api.app.getdonapi.global.enums.ErrorCode;
import com.api.app.getdonapi.global.exception.CustomException;
import com.api.app.getdonapi.meeting.domain.Meeting;
import com.api.app.getdonapi.meeting.repository.MeetingRepository;
import com.api.app.getdonapi.meetingmember.controller.request.JoinMeetingRequest;
import com.api.app.getdonapi.meetingmember.domain.MeetingMember;
import com.api.app.getdonapi.meetingmember.enums.MeetingRole;
import com.api.app.getdonapi.member.domain.User;
import com.api.app.getdonapi.member.repository.UserRepository;
import com.api.app.getdonapi.meetingmember.repository.MeetingMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MeetingMemberCommandServiceImpl implements MeetingMemberCommandService {
    private final MeetingMemberRepository meetingMemberRepository;
    private final MeetingRepository meetingRepository;
    private final UserRepository userRepository;

    @Override
    public void joinMeeting(JoinMeetingRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        Meeting meeting = meetingRepository.findByInviteCode(request.getInviteCode())
                .orElseThrow(() -> new CustomException(ErrorCode.MEETING_NOT_FOUND));

        MeetingMember meetingMember = MeetingMember.builder()
                .user(user)
                .meeting(meeting)
                .role(MeetingRole.MEMBER)
                .build();

        meetingMemberRepository.save(meetingMember);
    }
}
