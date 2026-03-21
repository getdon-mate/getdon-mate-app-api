package com.api.app.getdonapi.meetingmember.controller.query;

import com.api.app.getdonapi.global.ApiPath;
import com.api.app.getdonapi.global.RestDocsSupport;
import com.api.app.getdonapi.global.config.jwt.security.UserPrincipal;
import com.api.app.getdonapi.meetingmember.controller.response.MeetingMemberListResponse;
import com.api.app.getdonapi.meetingmember.enums.MeetingRole;
import com.api.app.getdonapi.meetingmember.service.query.MeetingMemberQueryService;
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

class MeetingMemberQueryControllerTest extends RestDocsSupport {

    private final MeetingMemberQueryService meetingMemberQueryService = mock(MeetingMemberQueryService.class);

    @Override
    protected Object initController() {
        return new MeetingMemberQueryController(meetingMemberQueryService);
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
    @DisplayName("모임 멤버 목록 조회 성공")
    void memberList() throws Exception {
        // given
        List<MeetingMemberListResponse> response = List.of(
                MeetingMemberListResponse.builder()
                        .meetingMemberId(1L)
                        .userId(1L)
                        .userName("홍길동")
                        .profileUrl("https://example.com/profile.jpg")
                        .role(MeetingRole.LEADER)
                        .build(),
                MeetingMemberListResponse.builder()
                        .meetingMemberId(2L)
                        .userId(2L)
                        .userName("김영희")
                        .profileUrl(null)
                        .role(MeetingRole.MEMBER)
                        .build()
        );

        when(meetingMemberQueryService.getMemberList(anyLong())).thenReturn(response);

        // when & then
        mockMvc.perform(get(ApiPath.MeetingMember.ROOT + ApiPath.MeetingMember.MEMBER_LIST)
                        .param("meetingId", "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("정상 처리됐습니다."))
                .andExpect(jsonPath("$.data[0].meetingMemberId").value(1))
                .andExpect(jsonPath("$.data[0].userName").value("홍길동"))
                .andExpect(jsonPath("$.data[0].role").value("LEADER"))
                .andExpect(jsonPath("$.data[1].userName").value("김영희"))
                .andExpect(jsonPath("$.data[1].role").value("MEMBER"))
                .andDo(MockMvcRestDocumentationWrapper.document("meeting-member-list",
                        resource(ResourceSnippetParameters.builder()
                                .tag("MeetingMember")
                                .summary("모임 멤버 목록 조회")
                                .description("모임 ID로 활성 멤버 목록을 조회합니다.")
                                .queryParameters(
                                        parameterWithName("meetingId").type(SimpleType.INTEGER).description("모임 ID")
                                )
                                .responseSchema(Schema.schema("MeetingMemberListResponse"))
                                .responseFields(
                                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                        fieldWithPath("data[].meetingMemberId").type(JsonFieldType.NUMBER).description("모임 멤버 ID"),
                                        fieldWithPath("data[].userId").type(JsonFieldType.NUMBER).description("유저 ID"),
                                        fieldWithPath("data[].userName").type(JsonFieldType.STRING).description("유저 이름"),
                                        fieldWithPath("data[].profileUrl").type(JsonFieldType.STRING).optional().description("프로필 이미지 URL"),
                                        fieldWithPath("data[].role").type(JsonFieldType.STRING).description("모임 역할 (LEADER / MEMBER)")
                                )
                                .build()
                        )
                ));
    }
}