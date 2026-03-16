package com.api.app.getdonapi.meeting.controller.command;

import com.api.app.getdonapi.global.ApiPath;
import com.api.app.getdonapi.global.RestDocsSupport;
import com.api.app.getdonapi.global.config.jwt.security.UserPrincipal;
import com.api.app.getdonapi.meeting.controller.request.CreateMeetingRequest;
import com.api.app.getdonapi.meeting.service.command.MeetingCommandService;
import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MeetingCommandControllerTest extends RestDocsSupport {

    private final MeetingCommandService meetingCommandService = mock(MeetingCommandService.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected Object initController() {
        return new MeetingCommandController(meetingCommandService);
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
    @DisplayName("모임 생성 성공")
    void createMeeting() throws Exception {
        // given
        CreateMeetingRequest request = CreateMeetingRequest.builder()
                .title("테스트 모임")
                .bankName("카카오뱅크")
                .bankAccount(1234567890)
                .build();

        doNothing().when(meetingCommandService).createMeeting(anyLong(), any());

        // when & then
        mockMvc.perform(post(ApiPath.Meeting.ROOT + ApiPath.Meeting.CREATE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(201))
                .andExpect(jsonPath("$.message").value("모임 통장 개설 완료되었습니다."))
                .andDo(MockMvcRestDocumentationWrapper.document("meeting-create",
                        resource(ResourceSnippetParameters.builder()
                                .tag("Meeting")
                                .summary("모임 생성")
                                .description("모임 통장을 개설합니다. 생성자는 자동으로 LEADER가 됩니다.")
                                .requestSchema(Schema.schema("CreateMeetingRequest"))
                                .responseSchema(Schema.schema("ApiResponse"))
                                .requestFields(
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("모임 이름").optional(),
                                        fieldWithPath("bankName").type(JsonFieldType.STRING).description("은행명").optional(),
                                        fieldWithPath("bankAccount").type(JsonFieldType.NUMBER).description("계좌번호").optional()
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