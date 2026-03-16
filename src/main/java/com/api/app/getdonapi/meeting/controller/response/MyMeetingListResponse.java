package com.api.app.getdonapi.meeting.controller.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyMeetingListResponse {
    private Long meetingId;
    private String title;
    private String bankName;
    private Integer amount;
    private Integer paidCount;
}
