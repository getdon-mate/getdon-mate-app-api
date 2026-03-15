package com.api.app.getdonapi.withdrawal.domain;

import com.api.app.getdonapi.global.BaseTimeEntity;
import com.api.app.getdonapi.meetingmember.domain.MeetingMember;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "withdrawal")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Withdrawal extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "withdrawal_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_member_id", nullable = false)
    private MeetingMember meetingMember;

    @Column(name = "withdrawal_amount", nullable = false)
    private Integer withdrawalAmount;

    @Builder
    public Withdrawal(MeetingMember meetingMember, Integer withdrawalAmount) {
        this.meetingMember = meetingMember;
        this.withdrawalAmount = withdrawalAmount;
    }
}
