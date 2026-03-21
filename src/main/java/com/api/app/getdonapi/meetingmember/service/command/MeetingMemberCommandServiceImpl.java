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

        addMember(user, meeting, MeetingRole.MEMBER);
    }

    @Override
    public void addMember(User user, Meeting meeting, MeetingRole role) {
        MeetingMember meetingMember = MeetingMember.builder()
                .user(user)
                .meeting(meeting)
                .role(role)
                .build();

        meetingMemberRepository.save(meetingMember);
    }

    @Override
    public void withdrawalMember(Long meetingMemberId, Long requesterId) {
        MeetingMember target = meetingMemberRepository.findById(meetingMemberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEETING_MEMBER_NOT_FOUND));

        MeetingMember requester = meetingMemberRepository.findByUserIdAndMeetingId(requesterId, target.getMeeting().getId())
                .orElseThrow(() -> new CustomException(ErrorCode.MEETING_MEMBER_NOT_FOUND));

        if (requester.getRole() != MeetingRole.LEADER) {
            throw new CustomException(ErrorCode.NOT_LEADER);
        }

        target.withdraw();
    }
}
