package com.api.app.getdonapi.meeting.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class MyMeetingListResponse {
    private Long meetingId;
    private String title;
    private String bankName;
    private Integer amount;
    private Long paidCount;
}
