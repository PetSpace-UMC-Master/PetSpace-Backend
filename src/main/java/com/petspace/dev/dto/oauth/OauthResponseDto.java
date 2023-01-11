package com.petspace.dev.dto.oauth;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OauthResponseDto {

    private String email;
    private String accessToken;
    private String refreshToken;

    @Builder
    public OauthResponseDto(String email, String accessToken, String refreshToken) {
        this.email = email;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
