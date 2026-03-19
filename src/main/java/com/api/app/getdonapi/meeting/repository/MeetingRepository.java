package com.api.app.getdonapi.meeting.repository;

import com.api.app.getdonapi.meeting.domain.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MeetingRepository extends JpaRepository<Meeting, Long>, MeetingRepositoryCustom {
    Optional<Meeting> findByInviteCode(String inviteCode);
}