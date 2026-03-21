package com.api.app.getdonapi.meetingmember.service.command;

import com.api.app.getdonapi.global.enums.ErrorCode;
import com.api.app.getdonapi.global.exception.CustomException;
import com.api.app.getdonapi.meeting.domain.Meeting;
import com.api.app.getdonapi.meeting.repository.MeetingRepository;
import com.api.app.getdonapi.meetingmember.controller.request.JoinMeetingRequest;
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

class MeetingMemberCommandServiceImplTest {

    private final MeetingMemberRepository meetingMemberRepository = mock(MeetingMemberRepository.class);
    private final MeetingRepository meetingRepository = mock(MeetingRepository.class);
    private final UserRepository userRepository = mock(UserRepository.class);
    private final MeetingMemberCommandServiceImpl meetingMemberCommandService =
            new MeetingMemberCommandServiceImpl(meetingMemberRepository, meetingRepository, userRepository);

    @Test
    @DisplayName("모임 참여 성공 - MEMBER 역할로 MeetingMember 저장")
    void joinMeeting_success() {
        // given
        Long userId = 1L;
        JoinMeetingRequest request = mock(JoinMeetingRequest.class);
        when(request.getInviteCode()).thenReturn("ABCD1234");

        User user = User.builder()
                .userName("테스터")
                .email("test@test.com")
                .password("encodedPassword")
                .provider(LoginType.NORMAL)
                .build();

        Meeting meeting = Meeting.builder()
                .user(user)
                .title("테스트 모임")
                .bankName("카카오뱅크")
                .bankAccount(12345678)
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(meetingRepository.findByInviteCode("ABCD1234")).thenReturn(Optional.of(meeting));

        // when
        meetingMemberCommandService.joinMeeting(request, userId);

        // then
        ArgumentCaptor<MeetingMember> captor = ArgumentCaptor.forClass(MeetingMember.class);
        verify(meetingMemberRepository).save(captor.capture());
        MeetingMember saved = captor.getValue();
        assertThat(saved.getRole()).isEqualTo(MeetingRole.MEMBER);
        assertThat(saved.getWithdrawalYn()).isEqualTo("N");
    }

    @Test
    @DisplayName("모임 참여 실패 - 존재하지 않는 회원")
    void joinMeeting_memberNotFound() {
        // given
        Long userId = 999L;
        JoinMeetingRequest request = mock(JoinMeetingRequest.class);

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> meetingMemberCommandService.joinMeeting(request, userId))
                .isInstanceOf(CustomException.class)
                .satisfies(e -> assertThat(((CustomException) e).getErrorCode()).isEqualTo(ErrorCode.MEMBER_NOT_FOUND));

        verify(meetingRepository, never()).findByInviteCode(any());
        verify(meetingMemberRepository, never()).save(any());
    }

    @Test
    @DisplayName("모임 참여 실패 - 존재하지 않는 초대코드")
    void joinMeeting_meetingNotFound() {
        // given
        Long userId = 1L;
        JoinMeetingRequest request = mock(JoinMeetingRequest.class);
        when(request.getInviteCode()).thenReturn("INVALID1");

        User user = User.builder()
                .userName("테스터")
                .email("test@test.com")
                .password("encodedPassword")
                .provider(LoginType.NORMAL)
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(meetingRepository.findByInviteCode("INVALID1")).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> meetingMemberCommandService.joinMeeting(request, userId))
                .isInstanceOf(CustomException.class)
                .satisfies(e -> assertThat(((CustomException) e).getErrorCode()).isEqualTo(ErrorCode.MEETING_NOT_FOUND));

        verify(meetingMemberRepository, never()).save(any());
    }

    @Test
    @DisplayName("모임 멤버 내보내기 성공 - LEADER가 요청 시 withdraw() 호출")
    void withdrawalMember_success() {
        // given
        Long meetingMemberId = 10L;
        Long requesterId = 1L;
        Long meetingId = 5L;

        Meeting meeting = mock(Meeting.class);
        when(meeting.getId()).thenReturn(meetingId);

        MeetingMember target = mock(MeetingMember.class);
        when(target.getMeeting()).thenReturn(meeting);

        MeetingMember requester = mock(MeetingMember.class);
        when(requester.getRole()).thenReturn(MeetingRole.LEADER);

        when(meetingMemberRepository.findById(meetingMemberId)).thenReturn(Optional.of(target));
        when(meetingMemberRepository.findByUserIdAndMeetingId(requesterId, meetingId)).thenReturn(Optional.of(requester));

        // when
        meetingMemberCommandService.withdrawalMember(meetingMemberId, requesterId);

        // then
        verify(target).withdraw();
    }

    @Test
    @DisplayName("모임 멤버 내보내기 실패 - 대상 멤버가 존재하지 않음")
    void withdrawalMember_targetNotFound() {
        // given
        Long meetingMemberId = 999L;
        Long requesterId = 1L;

        when(meetingMemberRepository.findById(meetingMemberId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> meetingMemberCommandService.withdrawalMember(meetingMemberId, requesterId))
                .isInstanceOf(CustomException.class)
                .satisfies(e -> assertThat(((CustomException) e).getErrorCode()).isEqualTo(ErrorCode.MEETING_MEMBER_NOT_FOUND));

        verify(meetingMemberRepository, never()).findByUserIdAndMeetingId(any(), any());
    }

    @Test
    @DisplayName("모임 멤버 내보내기 실패 - 요청자가 해당 모임 멤버가 아님")
    void withdrawalMember_requesterNotFound() {
        // given
        Long meetingMemberId = 10L;
        Long requesterId = 999L;
        Long meetingId = 5L;

        Meeting meeting = mock(Meeting.class);
        when(meeting.getId()).thenReturn(meetingId);

        MeetingMember target = mock(MeetingMember.class);
        when(target.getMeeting()).thenReturn(meeting);

        when(meetingMemberRepository.findById(meetingMemberId)).thenReturn(Optional.of(target));
        when(meetingMemberRepository.findByUserIdAndMeetingId(requesterId, meetingId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> meetingMemberCommandService.withdrawalMember(meetingMemberId, requesterId))
                .isInstanceOf(CustomException.class)
                .satisfies(e -> assertThat(((CustomException) e).getErrorCode()).isEqualTo(ErrorCode.MEETING_MEMBER_NOT_FOUND));

        verify(target, never()).withdraw();
    }

    @Test
    @DisplayName("모임 멤버 내보내기 실패 - 요청자가 LEADER가 아님")
    void withdrawalMember_notLeader() {
        // given
        Long meetingMemberId = 10L;
        Long requesterId = 2L;
        Long meetingId = 5L;

        Meeting meeting = mock(Meeting.class);
        when(meeting.getId()).thenReturn(meetingId);

        MeetingMember target = mock(MeetingMember.class);
        when(target.getMeeting()).thenReturn(meeting);

        MeetingMember requester = mock(MeetingMember.class);
        when(requester.getRole()).thenReturn(MeetingRole.MEMBER);

        when(meetingMemberRepository.findById(meetingMemberId)).thenReturn(Optional.of(target));
        when(meetingMemberRepository.findByUserIdAndMeetingId(requesterId, meetingId)).thenReturn(Optional.of(requester));

        // when & then
        assertThatThrownBy(() -> meetingMemberCommandService.withdrawalMember(meetingMemberId, requesterId))
                .isInstanceOf(CustomException.class)
                .satisfies(e -> assertThat(((CustomException) e).getErrorCode()).isEqualTo(ErrorCode.NOT_LEADER));

        verify(target, never()).withdraw();
    }
}