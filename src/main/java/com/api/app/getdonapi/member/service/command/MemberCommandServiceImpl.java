package com.api.app.getdonapi.member.service.command;

import com.api.app.getdonapi.global.config.jwt.TokenProvider;
import com.api.app.getdonapi.global.enums.ErrorCode;
import com.api.app.getdonapi.global.enums.UseYn;
import com.api.app.getdonapi.global.exception.CustomException;
import com.api.app.getdonapi.member.controller.request.UserLoginRequest;
import com.api.app.getdonapi.member.controller.request.UserJoinRequest;
import com.api.app.getdonapi.member.controller.response.UserLoginResponse;
import com.api.app.getdonapi.member.domain.User;
import com.api.app.getdonapi.member.domain.enums.LOGINTYPE;
import com.api.app.getdonapi.member.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberCommandServiceImpl implements MemberCommandService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    @Override
    public void createMember(UserJoinRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
        }

        User user = User.builder()
                .userName(request.getUserName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .provider(LOGINTYPE.NORMAL)
                .build();

        userRepository.save(user);
    }

    @Override
    public UserLoginResponse login(UserLoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        String accessToken  = tokenProvider.generateAccessToken(user.getEmail(), user.getProvider().name());
        String refreshToken = tokenProvider.generateRefreshToken(user.getEmail(), user.getProvider().name());

        return UserLoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}