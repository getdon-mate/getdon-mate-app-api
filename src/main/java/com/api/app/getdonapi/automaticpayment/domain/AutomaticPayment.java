package com.api.app.getdonapi.automaticpayment.domain;

import com.api.app.getdonapi.global.BaseTimeEntity;
import com.api.app.getdonapi.meetingmember.domain.MeetingMember;
import com.api.app.getdonapi.paymenthistory.domain.PaymentHistory;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "automatic_payment")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AutomaticPayment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "automatic_payment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_member_id", nullable = false)
    private MeetingMember meetingMember;

    @Column(name = "payment_amount", nullable = false)
    private Integer paymentAmount = 0;

    @Column(name = "payment_day", nullable = false)
    private Integer paymentDay;

    @Column(name = "active_yn", nullable = false, length = 1)
    private String activeYn = "N";

    @OneToMany(mappedBy = "automaticPayment")
    private List<PaymentHistory> paymentHistories = new ArrayList<>();

    @Builder
    public AutomaticPayment(MeetingMember meetingMember, Integer paymentAmount,
                            Integer paymentDay) {
        this.meetingMember = meetingMember;
        this.paymentAmount = paymentAmount;
        this.paymentDay = paymentDay;
        this.activeYn = "N";
    }

    public void activate() {
        this.activeYn = "Y";
    }

    public void deactivate() {
        this.activeYn = "N";
    }

    public void updatePaymentAmount(Integer paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public void updatePaymentDay(Integer paymentDay) {
        this.paymentDay = paymentDay;
    }
}
