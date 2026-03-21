package com.api.app.getdonapi.meeting.controller.query;

import com.api.app.getdonapi.global.ApiPath;
import com.api.app.getdonapi.global.RestDocsSupport;
import com.api.app.getdonapi.global.config.jwt.security.UserPrincipal;
import com.api.app.getdonapi.meeting.controller.response.MyMeetingListResponse;
import com.api.app.getdonapi.meeting.service.query.MeetingQueryService;
import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.epages.restdocs.apispec.SimpleType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.method.annotation.AuthenticationPrincipalArgumentResolver;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MeetingQueryControllerTest extends RestDocsSupport {

    private final MeetingQueryService meetingQueryService = mock(MeetingQueryService.class);

    @Override
    protected Object initController() {
        return new MeetingQueryController(meetingQueryService);
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
    @DisplayName("내 모임 목록 조회 성공")
    void getMyList() throws Exception {
        // given
        List<MyMeetingListResponse> response = List.of(
                MyMeetingListResponse.builder()
                        .meetingId(1L)
                        .title("테스트 모임")
                        .bankName("카카오뱅크")
                        .amount(50000)
                        .paidCount(3L)
                        .build()
        );

        when(meetingQueryService.getMyList(anyLong())).thenReturn(response);

        // when & then
        mockMvc.perform(get(ApiPath.Meeting.ROOT + ApiPath.Meeting.MY_LIST))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("정상 처리됐습니다."))
                .andExpect(jsonPath("$.data[0].meetingId").value(1))
                .andExpect(jsonPath("$.data[0].title").value("테스트 모임"))
                .andExpect(jsonPath("$.data[0].paidCount").value(3))
                .andDo(MockMvcRestDocumentationWrapper.document("meeting-my-list",
                        resource(ResourceSnippetParameters.builder()
                                .tag("Meeting")
                                .summary("내 모임 목록 조회")
                                .description("로그인한 사용자가 속한 모임 목록을 조회합니다.")
                                .responseSchema(Schema.schema("MyMeetingListResponse"))
                                .responseFields(
                                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                        fieldWithPath("data[].meetingId").type(JsonFieldType.NUMBER).description("모임 ID"),
                                        fieldWithPath("data[].title").type(JsonFieldType.STRING).description("모임 이름"),
                                        fieldWithPath("data[].bankName").type(JsonFieldType.STRING).description("은행명"),
                                        fieldWithPath("data[].amount").type(JsonFieldType.NUMBER).description("모임 통장 잔액"),
                                        fieldWithPath("data[].paidCount").type(JsonFieldType.NUMBER).description("납부 완료 횟수")
                                )
                                .build()
                        )
                ));
    }

    @Test
    @DisplayName("모임 초대코드 조회 성공")
    void getInviteCode() throws Exception {
        // given
        when(meetingQueryService.getInviteCode(anyLong())).thenReturn("ABCD1234");

        // when & then
        mockMvc.perform(get(ApiPath.Meeting.ROOT + ApiPath.Meeting.INVITE_CODE)
                        .param("meetingId", "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("정상 처리됐습니다."))
                .andExpect(jsonPath("$.data").value("ABCD1234"))
                .andDo(MockMvcRestDocumentationWrapper.document("meeting-invite-code",
                        resource(ResourceSnippetParameters.builder()
                                .tag("Meeting")
                                .summary("모임 초대코드 조회")
                                .description("모임 ID로 초대코드를 조회합니다.")
                                .queryParameters(parameterWithName("meetingId").type(SimpleType.INTEGER).description("모임 ID"))
                                .responseSchema(Schema.schema("InviteCodeResponse"))
                                .responseFields(
                                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                        fieldWithPath("data").type(JsonFieldType.STRING).description("초대코드 (8자리 대문자)")
                                )
                                .build()
                        )
                ));
    }
}