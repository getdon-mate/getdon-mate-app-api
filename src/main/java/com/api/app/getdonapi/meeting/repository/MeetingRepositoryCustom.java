package com.api.app.getdonapi.meeting.repository;

import com.api.app.getdonapi.meeting.service.internal.MyMeetingList;

import java.util.List;

public interface MeetingRepositoryCustom {
    List<MyMeetingList> findMyMeetingList(Long userId);
}