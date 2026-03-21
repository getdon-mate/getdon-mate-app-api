package com.api.app.getdonapi.meetingmember.service.query;

import com.api.app.getdonapi.meetingmember.controller.response.MeetingMemberListResponse;
import com.api.app.getdonapi.meetingmember.repository.MeetingMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MeetingMemberQueryServiceImpl implements MeetingMemberQueryService {

    private final MeetingMemberRepository meetingMemberRepository;

    @Override
    public List<MeetingMemberListResponse> getMemberList(Long meetingId) {
        return meetingMemberRepository.findMemberList(meetingId);
    }
}
