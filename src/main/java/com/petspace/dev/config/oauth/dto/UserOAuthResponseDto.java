package com.petspace.dev.config.oauth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserOAuthResponseDto {

    private String email;
    private String accessToken;
    private String refreshToken;
}
