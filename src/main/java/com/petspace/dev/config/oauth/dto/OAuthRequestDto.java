package com.petspace.dev.config.oauth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OAuthRequestDto {

    // 프론트에서 넘겨주는 accessToken, JWT 관련 아님에 주의!
    private String accessToken;
}
