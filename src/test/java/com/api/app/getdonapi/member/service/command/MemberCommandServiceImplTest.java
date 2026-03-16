package com.api.app.getdonapi.member.service.command;

import com.api.app.getdonapi.global.config.jwt.TokenProvider;
import com.api.app.getdonapi.global.enums.ErrorCode;
import com.api.app.getdonapi.global.exception.CustomException;
import com.api.app.getdonapi.member.controller.request.UserJoinRequest;
import com.api.app.getdonapi.member.controller.request.UserLoginRequest;
import com.api.app.getdonapi.member.controller.response.UserLoginResponse;
import com.api.app.getdonapi.member.domain.User;
import com.api.app.getdonapi.member.domain.enums.LoginType;
import com.api.app.getdonapi.member.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MemberCommandServiceImplTest {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    private final TokenProvider tokenProvider = mock(TokenProvider.class);
    private final MemberCommandServiceImpl memberCommandService =
            new MemberCommandServiceImpl(userRepository, passwordEncoder, tokenProvider);

    @Test
    @DisplayName("회원가입 성공")
    void createMember_success() {
        // given
        UserJoinRequest request = UserJoinRequest.builder()
                .userName("테스터")
                .email("test@test.com")
                .password("password123!")
                .build();

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");

        // when
        memberCommandService.createMember(request);

        // then
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("회원가입 실패 - 이미 사용 중인 이메일")
    void createMember_duplicateEmail() {
        // given
        UserJoinRequest request = UserJoinRequest.builder()
                .userName("테스터")
                .email("duplicate@test.com")
                .password("password123!")
                .build();

        User existing = User.builder()
                .userName("기존유저")
                .email("duplicate@test.com")
                .password("encodedPassword")
                .provider(LoginType.NORMAL)
                .build();

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(existing));

        // when & then
        assertThatThrownBy(() -> memberCommandService.createMember(request))
                .isInstanceOf(CustomException.class)
                .satisfies(e -> assertThat(((CustomException) e).getErrorCode()).isEqualTo(ErrorCode.DUPLICATE_EMAIL));

        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("로그인 성공")
    void login_success() {
        // given
        UserLoginRequest request = UserLoginRequest.builder()
                .email("test@test.com")
                .password("password123!")
                .build();

        User user = User.builder()
                .userName("테스터")
                .email("test@test.com")
                .password("encodedPassword")
                .provider(LoginType.NORMAL)
                .build();

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.getPassword(), user.getPassword())).thenReturn(true);
        when(tokenProvider.generateAccessToken(any(), anyString(), anyString())).thenReturn("access-token");
        when(tokenProvider.generateRefreshToken(any(), anyString(), anyString())).thenReturn("refresh-token");

        // when
        UserLoginResponse response = memberCommandService.login(request);

        // then
        assertThat(response.getAccessToken()).isEqualTo("access-token");
        assertThat(response.getRefreshToken()).isEqualTo("refresh-token");
    }

    @Test
    @DisplayName("로그인 실패 - 존재하지 않는 회원")
    void login_memberNotFound() {
        // given
        UserLoginRequest request = UserLoginRequest.builder()
                .email("notfound@test.com")
                .password("password123!")
                .build();

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> memberCommandService.login(request))
                .isInstanceOf(CustomException.class)
                .satisfies(e -> assertThat(((CustomException) e).getErrorCode()).isEqualTo(ErrorCode.MEMBER_NOT_FOUND));
    }

    @Test
    @DisplayName("로그인 실패 - 비밀번호 불일치")
    void login_invalidPassword() {
        // given
        UserLoginRequest request = UserLoginRequest.builder()
                .email("test@test.com")
                .password("wrongPassword!")
                .build();

        User user = User.builder()
                .userName("테스터")
                .email("test@test.com")
                .password("encodedPassword")
                .provider(LoginType.NORMAL)
                .build();

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.getPassword(), user.getPassword())).thenReturn(false);

        // when & then
        assertThatThrownBy(() -> memberCommandService.login(request))
                .isInstanceOf(CustomException.class)
                .satisfies(e -> assertThat(((CustomException) e).getErrorCode()).isEqualTo(ErrorCode.INVALID_PASSWORD));
    }
}