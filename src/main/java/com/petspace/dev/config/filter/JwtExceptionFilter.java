package com.petspace.dev.config.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petspace.dev.util.BaseResponse;
import com.petspace.dev.util.exception.JwtNotAvailableException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtExceptionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            filterChain.doFilter(request, response);
        } catch (JwtNotAvailableException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            BaseResponse<Object> baseResponse = new BaseResponse<>(e.getStatus());
            objectMapper.writeValue(response.getWriter(), baseResponse);
        }
    }
}
