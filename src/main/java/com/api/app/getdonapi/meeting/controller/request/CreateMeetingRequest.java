package com.api.app.getdonapi.meeting.controller.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateMeetingRequest {
    @NotNull(message = "제목은 필수입니다.")
    private String title;
    @NotNull(message = "은행명은 필수입니다.")
    private String bankName;
    @NotNull(message = "은행 계좌는 필수입니다.")
    private Integer bankAccount;

    @Builder
    public CreateMeetingRequest(String title, String bankName, Integer bankAccount) {
        this.title = title;
        this.bankName = bankName;
        this.bankAccount = bankAccount;
    }
}
