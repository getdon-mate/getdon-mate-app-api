package com.api.app.getdonapi.meeting.repository;

import com.api.app.getdonapi.meeting.domain.Meeting;
import com.api.app.getdonapi.meeting.domain.QMeeting;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MeetingRepositoryImpl implements MeetingRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Meeting> findAllActiveByUserId(Long userId) {
        QMeeting meeting = QMeeting.meeting;

        return queryFactory
                .selectFrom(meeting)
                .where(
                        meeting.user.id.eq(userId),
                        meeting.deleteYn.eq("N")
                )
                .orderBy(meeting.id.desc())
                .fetch();
    }
}