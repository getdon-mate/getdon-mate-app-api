package com.api.app.getdonapi.member.domain;

import com.api.app.getdonapi.global.BaseTimeEntity;
import com.api.app.getdonapi.global.enums.UseYn;
import com.api.app.getdonapi.meeting.domain.Meeting;
import com.api.app.getdonapi.meetingmember.domain.MeetingMember;
import com.api.app.getdonapi.member.domain.enums.LOGINTYPE;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "users")
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String userName;

    @Column(name = "profile_url")
    private String profileUrl;

    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    @Column(length = 6)
    private LOGINTYPE provider;

    @Column(name = "provider_id")
    private String providerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "use_yn",length = 1)
    private UseYn useYn = UseYn.Y;

    @OneToMany(mappedBy = "user")
    private List<Meeting> meetings = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<MeetingMember> meetingMembers = new ArrayList<>();

    @Builder
    public User(String userName, String profileUrl, String email, String password,
                LOGINTYPE provider, String providerId) {
        this.userName = userName;
        this.profileUrl = profileUrl;
        this.email = email;
        this.password = password;
        this.provider = provider;
        this.providerId = providerId;
        this.useYn = UseYn.Y;
    }

    public void deactivate() {
        this.useYn =  UseYn.N;
    }
}
