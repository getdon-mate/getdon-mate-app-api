package com.api.app.getdonapi.meetingmember.controller.command;

import com.api.app.getdonapi.global.ApiPath;
import com.api.app.getdonapi.global.RestDocsSupport;
import com.api.app.getdonapi.global.config.jwt.security.UserPrincipal;
import com.api.app.getdonapi.meetingmember.controller.request.JoinMeetingRequest;
import com.api.app.getdonapi.meetingmember.service.command.MeetingMemberCommandService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.method.annotation.AuthenticationPrincipalArgumentResolver;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import com.epages.restdocs.apispec.SimpleType;

import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MeetingMemberCommandControllerTest extends RestDocsSupport {

    private final MeetingMemberCommandService meetingMemberCommandService = mock(MeetingMemberCommandService.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected Object initController() {
        return new MeetingMemberCommandController(meetingMemberCommandService);
    }

    @BeforeEach
    void setUp(RestDocumentationContextProvider provider) {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(initController())
                .setCustomArgumentResolvers(new AuthenticationPrincipalArgumentResolver())
                .apply(MockMvcRestDocumentation.documentationConfiguration(provider))
                .build();

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        new UserPrincipal(1L, "test@test.com", "USER"),
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_USER"))
                )
        );
    }

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("모임 참여 성공")
    void joinMeeting() throws Exception {
        // given
        JoinMeetingRequest request = JoinMeetingRequest.builder()
                .inviteCode("ABCD1234")
                .build();

        doNothing().when(meetingMemberCommandService).joinMeeting(any(), anyLong());

        // when & then
        mockMvc.perform(post(ApiPath.MeetingMember.ROOT + ApiPath.MeetingMember.JOIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(201))
                .andExpect(jsonPath("$.message").value("모임 통장에 초대되었습니다."))
                .andDo(MockMvcRestDocumentationWrapper.document("meeting-member-join",
                        resource(ResourceSnippetParameters.builder()
                                .tag("MeetingMember")
                                .summary("모임 참여")
                                .description("초대코드로 모임에 참여합니다.")
                                .requestSchema(Schema.schema("JoinMeetingRequest"))
                                .responseSchema(Schema.schema("ApiResponse"))
                                .requestFields(
                                        fieldWithPath("inviteCode").type(JsonFieldType.STRING).description("모임 초대코드 (8자리)")
                                )
                                .responseFields(
                                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                        fieldWithPath("data").type(JsonFieldType.NULL).description("응답 데이터")
                                )
                                .build()
                        )
                ));
    }

    @Test
    @DisplayName("모임 멤버 내보내기 성공")
    void withdrawalMember() throws Exception {
        // given
        doNothing().when(meetingMemberCommandService).withdrawalMember(anyLong(), anyLong());

        // when & then
        mockMvc.perform(delete(ApiPath.MeetingMember.ROOT + ApiPath.MeetingMember.WITHDRAWAL + "?meetingMemberId=1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("모임 멤버가 탈퇴 처리되었습니다."))
                .andDo(MockMvcRestDocumentationWrapper.document("meeting-member-withdrawal",
                        resource(ResourceSnippetParameters.builder()
                                .tag("MeetingMember")
                                .summary("모임 멤버 내보내기")
                                .description("LEADER가 특정 모임 멤버를 내보냅니다.")
                                .responseSchema(Schema.schema("ApiResponse"))
                                .queryParameters(
                                        parameterWithName("meetingMemberId").type(SimpleType.INTEGER).description("내보낼 모임 멤버 ID")
                                )
                                .responseFields(
                                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                        fieldWithPath("data").type(JsonFieldType.NULL).description("응답 데이터")
                                )
                                .build()
                        )
                ));
    }
}