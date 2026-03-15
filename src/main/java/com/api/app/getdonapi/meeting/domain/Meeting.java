package com.api.app.getdonapi.meeting.domain;

import com.api.app.getdonapi.global.BaseTimeEntity;
import com.api.app.getdonapi.meetingmember.domain.MeetingMember;
import com.api.app.getdonapi.member.domain.User;
import com.api.app.getdonapi.onetimepayment.domain.OneTimePayment;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "meeting")
public class Meeting extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meeting_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "title", nullable = false, length = 50)
    private String title;

    @Column(name = "bank_name", nullable = false, length = 20)
    private String bankName;

    @Column(name = "bank_account", nullable = false)
    private Integer bankAccount;

    @Column(name = "amount", nullable = false)

    private Integer amount = 0;

    @Column(name = "delete_yn", nullable = false, length = 1)
    private String deleteYn = "N";

    @OneToMany(mappedBy = "meeting")
    private List<MeetingMember> meetingMembers = new ArrayList<>();

    @OneToMany(mappedBy = "meeting")
    private List<OneTimePayment> oneTimePayments = new ArrayList<>();

    @Builder
    public Meeting(User user, String title, String bankName, Integer bankAccount) {
        this.user = user;
        this.title = title;
        this.bankName = bankName;
        this.bankAccount = bankAccount;
        this.amount = 0;
        this.deleteYn = "N";
    }

    public void delete() {
        this.deleteYn = "Y";
    }

    public void updateAmount(int amount) {
        this.amount = amount;
    }
}
