package com.api.app.getdonapi.meeting.repository;

import com.api.app.getdonapi.meeting.controller.response.MyMeetingListResponse;
import com.api.app.getdonapi.meeting.domain.QMeeting;
import com.api.app.getdonapi.meeting.domain.enums.DeleteYn;
import com.api.app.getdonapi.meetingmember.domain.QMeetingMember;
import com.api.app.getdonapi.paymenthistory.domain.QPaymentHistory;
import com.api.app.getdonapi.paymenthistory.enums.PaymentStatus;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MeetingRepositoryImpl implements MeetingRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    private static final QMeeting meeting = QMeeting.meeting;
    private static final QMeetingMember meetingMember = QMeetingMember.meetingMember;
    private static final QMeetingMember subMeetingMember = new QMeetingMember("subMeetingMember");
    private static final QPaymentHistory paymentHistory = QPaymentHistory.paymentHistory;

    @Override
    public List<MyMeetingListResponse> findMyMeetingList(Long userId) {
        return queryFactory
                .select(Projections.constructor(MyMeetingListResponse.class,
                        meeting.id,
                        meeting.title,
                        meeting.bankName,
                        meeting.amount,
                        JPAExpressions
                                .select(paymentHistory.count())
                                .from(paymentHistory)
                                .join(paymentHistory.meetingMember, subMeetingMember)
                                .where(
                                        subMeetingMember.meeting.eq(meeting),
                                        paymentHistory.paymentStatus.eq(PaymentStatus.SUCCESS)
                                )
                ))
                .from(meeting)
                .join(meeting.meetingMembers, meetingMember)
                .where(
                        meetingMember.user.id.eq(userId),
                        meetingMember.withdrawalYn.eq("N"),
                        meeting.deleteYn.eq(DeleteYn.N)
                )
                .fetch();
    }
}