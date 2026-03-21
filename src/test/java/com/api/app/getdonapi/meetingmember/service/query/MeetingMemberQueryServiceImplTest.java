package com.api.app.getdonapi.meetingmember.service.query;

import com.api.app.getdonapi.meeting.domain.Meeting;
import com.api.app.getdonapi.meetingmember.controller.response.MeetingMemberListResponse;
import com.api.app.getdonapi.meetingmember.domain.MeetingMember;
import com.api.app.getdonapi.meetingmember.enums.MeetingRole;
import com.api.app.getdonapi.meetingmember.repository.MeetingMemberRepositoryImpl;
import com.api.app.getdonapi.member.domain.User;
import com.api.app.getdonapi.member.domain.enums.LoginType;
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
@Import({MeetingMemberRepositoryImpl.class, MeetingMemberQueryServiceImpl.class, MeetingMemberQueryServiceImplTest.QueryDslTestConfig.class})
class MeetingMemberQueryServiceImplTest {

    @TestConfiguration
    @EnableJpaAuditing
    static class QueryDslTestConfig {
        @Bean
        JPAQueryFactory jpaQueryFactory(EntityManager em) {
            return new JPAQueryFactory(em);
        }
    }

    @Autowired
    private MeetingMemberQueryServiceImpl meetingMemberQueryService;

    @Autowired
    private EntityManager em;

    @Test
    @DisplayName("모임 멤버 목록 조회 - 활성 멤버만 반환")
    void getMemberList_success() {
        // given
        User user1 = saveUser("leader@test.com", "홍길동");
        User user2 = saveUser("member@test.com", "김영희");
        Meeting meeting = saveMeeting(user1);
        saveMeetingMember(user1, meeting, MeetingRole.LEADER);
        saveMeetingMember(user2, meeting, MeetingRole.MEMBER);

        em.flush();
        em.clear();

        // when
        List<MeetingMemberListResponse> result = meetingMemberQueryService.getMemberList(meeting.getId());

        // then
        assertThat(result).hasSize(2);
        assertThat(result).extracting("userName").containsExactlyInAnyOrder("홍길동", "김영희");
    }

    @Test
    @DisplayName("모임 멤버 목록 조회 - 탈퇴한 멤버(withdrawalYn=Y)는 제외")
    void getMemberList_excludesWithdrawnMember() {
        // given
        User user1 = saveUser("leader2@test.com", "리더");
        User user2 = saveUser("withdrawn@test.com", "탈퇴자");
        Meeting meeting = saveMeeting(user1);
        saveMeetingMember(user1, meeting, MeetingRole.LEADER);

        MeetingMember withdrawn = MeetingMember.builder()
                .user(user2)
                .meeting(meeting)
                .role(MeetingRole.MEMBER)
                .build();
        withdrawn.withdraw();
        em.persist(withdrawn);

        em.flush();
        em.clear();

        // when
        List<MeetingMemberListResponse> result = meetingMemberQueryService.getMemberList(meeting.getId());

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getUserName()).isEqualTo("리더");
    }

    @Test
    @DisplayName("모임 멤버 목록 조회 - 멤버가 없으면 빈 리스트 반환")
    void getMemberList_returnsEmptyList() {
        // given
        User user = saveUser("empty@test.com", "빈유저");
        Meeting meeting = saveMeeting(user);

        em.flush();
        em.clear();

        // when
        List<MeetingMemberListResponse> result = meetingMemberQueryService.getMemberList(meeting.getId());

        // then
        assertThat(result).isEmpty();
    }

    // ─── helpers ──────────────────────────────────────────────────────────────

    private User saveUser(String email, String userName) {
        User user = User.builder()
                .userName(userName)
                .email(email)
                .password("password")
                .provider(LoginType.NORMAL)
                .build();
        em.persist(user);
        return user;
    }

    private Meeting saveMeeting(User user) {
        Meeting meeting = Meeting.builder()
                .user(user)
                .title("테스트 모임")
                .bankName("카카오뱅크")
                .bankAccount(12345678)
                .build();
        em.persist(meeting);
        return meeting;
    }

    private void saveMeetingMember(User user, Meeting meeting, MeetingRole role) {
        MeetingMember member = MeetingMember.builder()
                .user(user)
                .meeting(meeting)
                .role(role)
                .build();
        em.persist(member);
    }
}