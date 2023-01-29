package com.petspace.dev.config.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petspace.dev.util.BaseResponse;
import com.petspace.dev.util.BaseResponseStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.petspace.dev.util.BaseResponseStatus.EMPTY_AUTHORIZATION_HEADER;

@Component
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.info("in EntryPoint");
        BaseResponseStatus jwtExceptionStatus = (BaseResponseStatus) request.getAttribute("JwtException");
        log.info("Exception={}", jwtExceptionStatus);
        // 헤더를 아예 추가하지 않을 시
        if (jwtExceptionStatus == null) {
            customizeError(response, EMPTY_AUTHORIZATION_HEADER);
        } else{ // 토큰 값이 잘못된 경우
            customizeError(response, jwtExceptionStatus);
        }
    }

    public void customizeError(HttpServletResponse response, BaseResponseStatus status) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        BaseResponse<Object> baseResponse = new BaseResponse<>(status);
        objectMapper.writeValue(response.getWriter(), baseResponse);
    }
}
