package com.api.app.getdonapi.meetingmember.domain;

import com.api.app.getdonapi.automaticpayment.domain.AutomaticPayment;
import com.api.app.getdonapi.deposit.domain.Deposit;
import com.api.app.getdonapi.global.BaseTimeEntity;
import com.api.app.getdonapi.meeting.domain.Meeting;
import com.api.app.getdonapi.meetingmember.enums.MeetingRole;
import com.api.app.getdonapi.member.domain.User;
import com.api.app.getdonapi.onetimepaymentmember.domain.OneTimePaymentMember;
import com.api.app.getdonapi.paymenthistory.domain.PaymentHistory;
import com.api.app.getdonapi.withdrawal.domain.Withdrawal;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "meeting_member")
public class MeetingMember extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meeting_member_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_id", nullable = false)
    private Meeting meeting;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 10)
    private MeetingRole role;

    @Column(name = "withdrawal_yn", nullable = false, length = 1)
    private String withdrawalYn = "N";

    @OneToMany(mappedBy = "meetingMember")
    private List<Deposit> deposits = new ArrayList<>();

    @OneToMany(mappedBy = "meetingMember")
    private List<Withdrawal> withdrawals = new ArrayList<>();

    @OneToMany(mappedBy = "meetingMember")
    private List<AutomaticPayment> automaticPayments = new ArrayList<>();

    @OneToMany(mappedBy = "meetingMember")
    private List<PaymentHistory> paymentHistories = new ArrayList<>();

    @OneToMany(mappedBy = "meetingMember")
    private List<OneTimePaymentMember> oneTimePaymentMembers = new ArrayList<>();

    @Builder
    public MeetingMember(User user, Meeting meeting, MeetingRole role) {
        this.user = user;
        this.meeting = meeting;
        this.role = role;
        this.withdrawalYn = "N";
    }

    public void withdraw() {
        this.withdrawalYn = "Y";
    }
}
