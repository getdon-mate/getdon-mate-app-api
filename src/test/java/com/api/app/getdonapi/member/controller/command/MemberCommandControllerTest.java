package com.api.app.getdonapi.member.controller.command;

import com.api.app.getdonapi.global.ApiPath;
import com.api.app.getdonapi.global.RestDocsSupport;
import com.api.app.getdonapi.member.controller.request.UserJoinRequest;
import com.api.app.getdonapi.member.controller.request.UserLoginRequest;
import com.api.app.getdonapi.member.controller.response.UserLoginResponse;
import com.api.app.getdonapi.member.service.command.MemberCommandService;
import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MemberCommandControllerTest extends RestDocsSupport {

    private final MemberCommandService memberCommandService = mock(MemberCommandService.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected Object initController() {
        return new MemberCommandController(memberCommandService);
    }

    @Test
    @DisplayName("로그인 성공")
    void login() throws Exception {
        // given
        UserLoginRequest request = UserLoginRequest.builder()
                .email("test@test.com")
                .password("password123!")
                .build();

        UserLoginResponse response = UserLoginResponse.builder()
                .accessToken("access-token-value")
                .refreshToken("refresh-token-value")
                .build();

        when(memberCommandService.login(any())).thenReturn(response);

        // when & then
        mockMvc.perform(post(ApiPath.Member.ROOT + ApiPath.Member.LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("로그인이 정상적으로 완료됐습니다."))
                .andExpect(jsonPath("$.data.accessToken").exists())
                .andExpect(jsonPath("$.data.refreshToken").exists())
                .andDo(MockMvcRestDocumentationWrapper.document("member-login",
                        resource(ResourceSnippetParameters.builder()
                                .tag("Member")
                                .summary("로그인")
                                .description("이메일과 비밀번호로 로그인합니다.")
                                .requestSchema(Schema.schema("UserLoginRequest"))
                                .responseSchema(Schema.schema("UserLoginResponse"))
                                .requestFields(
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                        fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호")
                                )
                                .responseFields(
                                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                        fieldWithPath("data.accessToken").type(JsonFieldType.STRING).description("액세스 토큰"),
                                        fieldWithPath("data.refreshToken").type(JsonFieldType.STRING).description("리프레시 토큰")
                                )
                                .build()
                        )
                ));
    }

    @Test
    @DisplayName("회원가입 성공")
    void createMember() throws Exception {
        // given
        UserJoinRequest request = UserJoinRequest.builder()
                .userName("테스터")
                .email("test@test.com")
                .password("password123!")
                .build();

        doNothing().when(memberCommandService).createMember(any());

        // when & then
        mockMvc.perform(post(ApiPath.Member.ROOT + ApiPath.Member.JOIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(201))
                .andExpect(jsonPath("$.message").value("회원가입이 정상적으로 완료됐습니다."))
                .andDo(MockMvcRestDocumentationWrapper.document("member-join",
                        resource(ResourceSnippetParameters.builder()
                                .tag("Member")
                                .summary("회원가입")
                                .description("이메일과 비밀번호로 회원가입합니다.")
                                .requestSchema(Schema.schema("UserJoinRequest"))
                                .responseSchema(Schema.schema("ApiResponse"))
                                .requestFields(
                                        fieldWithPath("userName").type(JsonFieldType.STRING).description("사용자 이름"),
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                        fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호")
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