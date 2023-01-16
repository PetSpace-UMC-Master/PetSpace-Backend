package com.petspace.dev.dto.oauth;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OauthResponseDto {

    private String email;
    private String accessToken;
    private String refreshToken;
}
