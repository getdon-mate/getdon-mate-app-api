package com.api.app.getdonapi.meeting.repository;

import com.api.app.getdonapi.meeting.domain.Meeting;

import java.util.List;

public interface MeetingRepositoryCustom {

    List<Meeting> findAllActiveByUserId(Long userId);
}