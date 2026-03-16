package com.api.app.getdonapi.meeting.service.command;

import com.api.app.getdonapi.global.enums.ErrorCode;
import com.api.app.getdonapi.global.exception.CustomException;
import com.api.app.getdonapi.meeting.controller.request.CreateMeetingRequest;
import com.api.app.getdonapi.meeting.domain.Meeting;
import com.api.app.getdonapi.meeting.repository.MeetingRepository;
import com.api.app.getdonapi.meetingmember.domain.MeetingMember;
import com.api.app.getdonapi.meetingmember.enums.MeetingRole;
import com.api.app.getdonapi.meetingmember.repository.MeetingMemberRepository;
import com.api.app.getdonapi.member.domain.User;
import com.api.app.getdonapi.member.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MeetingCommandServiceImpl implements MeetingCommandService {

    private final UserRepository userRepository;
    private final MeetingRepository meetingRepository;
    private final MeetingMemberRepository meetingMemberRepository;

    @Override
    public void createMeeting(Long userId, CreateMeetingRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        Meeting meeting = Meeting.builder()
                .user(user)
                .title(request.getTitle())
                .bankName(request.getBankName())
                .bankAccount(request.getBankAccount())
                .build();

        meetingRepository.save(meeting);

        MeetingMember meetingMember = MeetingMember.builder()
                .user(user)
                .meeting(meeting)
                .role(MeetingRole.LEADER)
                .build();

        meetingMemberRepository.save(meetingMember);
    }
}
