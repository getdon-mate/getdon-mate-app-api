package com.api.app.getdonapi.meeting.service.internal;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MyMeetingList {
    private Long meetingId;
    private String title;
    private String bankName;
    private Integer amount;
    private Long paidCount;
}