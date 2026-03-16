package com.api.app.getdonapi.meeting.service.query;

import com.api.app.getdonapi.meeting.controller.response.MyMeetingListResponse;
import com.api.app.getdonapi.meeting.domain.Meeting;
import com.api.app.getdonapi.meeting.repository.MeetingRepositoryImpl;
import com.api.app.getdonapi.meetingmember.domain.MeetingMember;
import com.api.app.getdonapi.meetingmember.enums.MeetingRole;
import com.api.app.getdonapi.member.domain.User;
import com.api.app.getdonapi.member.domain.enums.LoginType;
import com.api.app.getdonapi.paymenthistory.domain.PaymentHistory;
import com.api.app.getdonapi.paymenthistory.enums.PaymentStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({MeetingRepositoryImpl.class, MeetingQueryServiceImpl.class, MeetingQueryServiceImplTest.QueryDslTestConfig.class})
class MeetingQueryServiceImplTest {

    @TestConfiguration
    @EnableJpaAuditing
    static class QueryDslTestConfig {
        @Bean
        JPAQueryFactory jpaQueryFactory(EntityManager em) {
            return new JPAQueryFactory(em);
        }
    }

    @Autowired
    private MeetingQueryServiceImpl meetingQueryService;

    @Autowired
    private EntityManager em;

    @Test
    @DisplayName("내 모임 목록 조회 - SUCCESS 상태 PaymentHistory만 paidCount에 포함")
    void getMyList_paidCountOnlyCountsSuccess() {
        // given
        User user = saveUser("test@test.com");
        Meeting meeting = saveMeeting(user, "테스트 모임", "카카오뱅크", 12345678);
        MeetingMember member = saveMeetingMember(user, meeting);

        savePaymentHistory(member, PaymentStatus.SUCCESS);
        savePaymentHistory(member, PaymentStatus.SUCCESS);
        savePaymentHistory(member, PaymentStatus.FAILED);

        em.flush();
        em.clear();

        // when
        List<MyMeetingListResponse> result = meetingQueryService.getMyList(user.getId());

        // then
        assertThat(result).hasSize(1);
        MyMeetingListResponse response = result.get(0);
        assertThat(response.getTitle()).isEqualTo("테스트 모임");
        assertThat(response.getBankName()).isEqualTo("카카오뱅크");
        assertThat(response.getPaidCount()).isEqualTo(2);
    }

    @Test
    @DisplayName("내 모임 목록 조회 - 탈퇴한 모임(withdrawalYn=Y)은 제외")
    void getMyList_excludesWithdrawnMember() {
        // given
        User user = saveUser("withdrawn@test.com");
        Meeting meeting = saveMeeting(user, "탈퇴 모임", "신한은행", 99999999);

        MeetingMember member = MeetingMember.builder()
                .user(user)
                .meeting(meeting)
                .role(MeetingRole.LEADER)
                .build();
        member.withdraw();
        em.persist(member);

        em.flush();
        em.clear();

        // when
        List<MyMeetingListResponse> result = meetingQueryService.getMyList(user.getId());

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("내 모임 목록 조회 - 삭제된 모임(deleteYn=Y)은 제외")
    void getMyList_excludesDeletedMeeting() {
        // given
        User user = saveUser("deleted@test.com");

        Meeting meeting = Meeting.builder()
                .user(user)
                .title("삭제 모임")
                .bankName("국민은행")
                .bankAccount(11111111)
                .build();
        meeting.delete();
        em.persist(meeting);

        saveMeetingMember(user, meeting);

        em.flush();
        em.clear();

        // when
        List<MyMeetingListResponse> result = meetingQueryService.getMyList(user.getId());

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("내 모임 목록 조회 - 모임이 없으면 빈 리스트 반환")
    void getMyList_returnsEmptyList() {
        // given
        User user = saveUser("empty@test.com");
        em.flush();
        em.clear();

        // when
        List<MyMeetingListResponse> result = meetingQueryService.getMyList(user.getId());

        // then
        assertThat(result).isEmpty();
    }

    // ─── helpers ──────────────────────────────────────────────────────────────

    private User saveUser(String email) {
        User user = User.builder()
                .userName("테스터")
                .email(email)
                .password("password")
                .provider(LoginType.NORMAL)
                .build();
        em.persist(user);
        return user;
    }

    private Meeting saveMeeting(User user, String title, String bankName, Integer bankAccount) {
        Meeting meeting = Meeting.builder()
                .user(user)
                .title(title)
                .bankName(bankName)
                .bankAccount(bankAccount)
                .build();
        em.persist(meeting);
        return meeting;
    }

    private MeetingMember saveMeetingMember(User user, Meeting meeting) {
        MeetingMember member = MeetingMember.builder()
                .user(user)
                .meeting(meeting)
                .role(MeetingRole.LEADER)
                .build();
        em.persist(member);
        return member;
    }

    private void savePaymentHistory(MeetingMember member, PaymentStatus status) {
        PaymentHistory ph = PaymentHistory.builder()
                .meetingMember(member)
                .amount(10000)
                .paymentStatus(status)
                .build();
        em.persist(ph);
    }
}