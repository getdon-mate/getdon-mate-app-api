package com.api.app.getdonapi.meetingmember.controller.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class JoinMeetingRequest {
    @NotNull(message = "초대코드는 필수입니다.")
    private String inviteCode;

    @Builder
    public JoinMeetingRequest(String inviteCode) {
        this.inviteCode = inviteCode;
    }
}
