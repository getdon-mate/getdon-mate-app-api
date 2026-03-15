package com.api.app.getdonapi.onetimepaymentmember.domain;

import com.api.app.getdonapi.global.BaseTimeEntity;
import com.api.app.getdonapi.meetingmember.domain.MeetingMember;
import com.api.app.getdonapi.onetimepayment.domain.OneTimePayment;
import com.api.app.getdonapi.paymenthistory.domain.PaymentHistory;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "one_time_payment_member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OneTimePaymentMember extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "one_time_payment_member_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "one_time_payment_id", nullable = false)
    private OneTimePayment oneTimePayment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_member_id", nullable = false)
    private MeetingMember meetingMember;

    @OneToMany(mappedBy = "oneTimePaymentMember")
    private List<PaymentHistory> paymentHistories = new ArrayList<>();

    @Builder
    public OneTimePaymentMember(OneTimePayment oneTimePayment, MeetingMember meetingMember) {
        this.oneTimePayment = oneTimePayment;
        this.meetingMember = meetingMember;
    }
}
