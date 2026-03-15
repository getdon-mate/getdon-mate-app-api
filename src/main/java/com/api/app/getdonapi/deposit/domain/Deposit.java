package com.api.app.getdonapi.deposit.domain;

import com.api.app.getdonapi.global.BaseTimeEntity;
import com.api.app.getdonapi.meetingmember.domain.MeetingMember;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "deposit")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Deposit extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "deposit_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_member_id", nullable = false)
    private MeetingMember meetingMember;

    @Column(name = "deposit_amount", nullable = false)
    private Integer depositAmount = 0;

    @Builder
    public Deposit(MeetingMember meetingMember, Integer depositAmount) {
        this.meetingMember = meetingMember;
        this.depositAmount = depositAmount;
    }
}
