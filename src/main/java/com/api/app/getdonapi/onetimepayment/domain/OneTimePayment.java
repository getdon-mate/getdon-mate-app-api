package com.api.app.getdonapi.onetimepayment.domain;

import com.api.app.getdonapi.global.BaseTimeEntity;
import com.api.app.getdonapi.meeting.domain.Meeting;
import com.api.app.getdonapi.meetingmember.domain.MeetingMember;
import com.api.app.getdonapi.onetimepaymentmember.domain.OneTimePaymentMember;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "one_time_payment")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OneTimePayment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "one_time_payment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_id", nullable = false)
    private Meeting meeting;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_member_id", nullable = false)
    private MeetingMember createdBy;

    @Column(name = "amount", nullable = false)
    private Integer amount;

    @Column(name = "deadline", nullable = false)
    private LocalDate deadline;

    @OneToMany(mappedBy = "oneTimePayment")
    private List<OneTimePaymentMember> oneTimePaymentMembers = new ArrayList<>();

    @Builder
    public OneTimePayment(Meeting meeting, MeetingMember createdBy,
                          Integer amount, LocalDate deadline) {
        this.meeting = meeting;
        this.createdBy = createdBy;
        this.amount = amount;
        this.deadline = deadline;
    }
}
