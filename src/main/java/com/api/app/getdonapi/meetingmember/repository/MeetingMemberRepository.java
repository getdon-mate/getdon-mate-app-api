package com.api.app.getdonapi.meetingmember.repository;

import com.api.app.getdonapi.meetingmember.domain.MeetingMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingMemberRepository extends JpaRepository<MeetingMember, Long> {
}