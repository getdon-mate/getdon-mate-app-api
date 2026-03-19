package com.api.app.getdonapi.global.config.jwt.exception;

import com.api.app.getdonapi.global.enums.ErrorCode;
import com.api.app.getdonapi.global.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        Object exception = request.getAttribute("exception");
        if(ObjectUtils.isEmpty(exception)) { // Header에 JWT Token이 없을 경우 setting
            response.getWriter().write(mapper.writeValueAsString(new ErrorResponse(ErrorCode.UNAUTHORIZED)));
        } else {
            response.getWriter().write(mapper.writeValueAsString(exception));
        }
    }
}