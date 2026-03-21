package com.api.app.getdonapi.meetingmember.repository;

import com.api.app.getdonapi.meetingmember.domain.MeetingMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MeetingMemberRepository extends JpaRepository<MeetingMember, Long>, MeetingMemberRepositoryCustom {
    Optional<MeetingMember> findByUserIdAndMeetingId(Long userId, Long meetingId);
}