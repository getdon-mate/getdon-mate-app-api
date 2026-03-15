package com.api.app.getdonapi.paymenthistory.domain;

import com.api.app.getdonapi.automaticpayment.domain.AutomaticPayment;
import com.api.app.getdonapi.global.BaseTimeEntity;
import com.api.app.getdonapi.meetingmember.domain.MeetingMember;
import com.api.app.getdonapi.onetimepaymentmember.domain.OneTimePaymentMember;
import com.api.app.getdonapi.paymenthistory.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "payment_history")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentHistory extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_history_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_member_id", nullable = false)
    private MeetingMember meetingMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "automatic_payment_id")
    private AutomaticPayment automaticPayment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "one_time_fee_member_id")
    private OneTimePaymentMember oneTimePaymentMember;

    @Column(name = "amount", nullable = false)
    private Integer amount = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", length = 10)
    private PaymentStatus paymentStatus;

    @Builder
    public PaymentHistory(MeetingMember meetingMember, AutomaticPayment automaticPayment,
                          OneTimePaymentMember oneTimePaymentMember, Integer amount,
                          PaymentStatus paymentStatus) {
        this.meetingMember = meetingMember;
        this.automaticPayment = automaticPayment;
        this.oneTimePaymentMember = oneTimePaymentMember;
        this.amount = amount;
        this.paymentStatus = paymentStatus;
    }
}
