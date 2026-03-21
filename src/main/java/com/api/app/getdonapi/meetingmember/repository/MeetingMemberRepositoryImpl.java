package com.api.app.getdonapi.meetingmember.repository;

import com.api.app.getdonapi.meetingmember.controller.response.MeetingMemberListResponse;
import com.api.app.getdonapi.meetingmember.domain.QMeetingMember;
import com.api.app.getdonapi.member.domain.QUser;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MeetingMemberRepositoryImpl implements MeetingMemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    private static final QMeetingMember meetingMember = QMeetingMember.meetingMember;
    private static final QUser user = QUser.user;

    @Override
    public List<MeetingMemberListResponse> findMemberList(Long meetingId) {
        return queryFactory
                .select(Projections.constructor(MeetingMemberListResponse.class,
                        meetingMember.id,
                        user.id,
                        user.userName,
                        user.profileUrl,
                        meetingMember.role
                ))
                .from(meetingMember)
                .join(meetingMember.user, user)
                .where(
                        meetingMember.meeting.id.eq(meetingId),
                        meetingMember.withdrawalYn.eq("N")
                )
                .fetch();
    }
}
