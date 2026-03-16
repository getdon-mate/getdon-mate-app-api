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
import com.api.app.getdonapi.member.domain.enums.LoginType;
import com.api.app.getdonapi.member.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MeetingCommandServiceImplTest {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final MeetingRepository meetingRepository = mock(MeetingRepository.class);
    private final MeetingMemberRepository meetingMemberRepository = mock(MeetingMemberRepository.class);
    private final MeetingCommandServiceImpl meetingCommandService =
            new MeetingCommandServiceImpl(userRepository, meetingRepository, meetingMemberRepository);

    @Test
    @DisplayName("모임 생성 성공 - Meeting 저장 및 LEADER로 MeetingMember 저장")
    void createMeeting_success() {
        // given
        Long userId = 1L;
        CreateMeetingRequest request = mock(CreateMeetingRequest.class);
        when(request.getTitle()).thenReturn("테스트 모임");
        when(request.getBankName()).thenReturn("카카오뱅크");
        when(request.getBankAccount()).thenReturn(12345678);

        User user = User.builder()
                .userName("테스터")
                .email("test@test.com")
                .password("encodedPassword")
                .provider(LoginType.NORMAL)
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // when
        meetingCommandService.createMeeting(userId, request);

        // then
        ArgumentCaptor<Meeting> meetingCaptor = ArgumentCaptor.forClass(Meeting.class);
        verify(meetingRepository).save(meetingCaptor.capture());
        Meeting savedMeeting = meetingCaptor.getValue();
        assertThat(savedMeeting.getTitle()).isEqualTo("테스트 모임");
        assertThat(savedMeeting.getBankName()).isEqualTo("카카오뱅크");
        assertThat(savedMeeting.getBankAccount()).isEqualTo(12345678);

        ArgumentCaptor<MeetingMember> memberCaptor = ArgumentCaptor.forClass(MeetingMember.class);
        verify(meetingMemberRepository).save(memberCaptor.capture());
        MeetingMember savedMember = memberCaptor.getValue();
        assertThat(savedMember.getRole()).isEqualTo(MeetingRole.LEADER);
        assertThat(savedMember.getWithdrawalYn()).isEqualTo("N");
    }

    @Test
    @DisplayName("모임 생성 실패 - 존재하지 않는 회원")
    void createMeeting_memberNotFound() {
        // given
        Long userId = 999L;
        CreateMeetingRequest request = mock(CreateMeetingRequest.class);

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> meetingCommandService.createMeeting(userId, request))
                .isInstanceOf(CustomException.class)
                .satisfies(e -> assertThat(((CustomException) e).getErrorCode()).isEqualTo(ErrorCode.MEMBER_NOT_FOUND));

        verify(meetingRepository, never()).save(any());
        verify(meetingMemberRepository, never()).save(any());
    }
}