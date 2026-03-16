package com.api.app.getdonapi.meeting.service.query;

import com.api.app.getdonapi.meeting.controller.response.MyMeetingListResponse;
import com.api.app.getdonapi.meeting.repository.MeetingRepository;
import com.api.app.getdonapi.meeting.service.internal.MyMeetingList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MeetingQueryServiceImpl implements MeetingQueryService {

    private final MeetingRepository meetingRepository;

    @Override
    public List<MyMeetingListResponse> getMyList(Long userId) {
        return meetingRepository.findMyMeetingList(userId).stream()
                .map(this::toResponse)
                .toList();
    }

    private MyMeetingListResponse toResponse(MyMeetingList item) {
        return MyMeetingListResponse.builder()
                .meetingId(item.getMeetingId())
                .title(item.getTitle())
                .bankName(item.getBankName())
                .amount(item.getAmount())
                .paidCount(item.getPaidCount().intValue())
                .build();
    }
}
