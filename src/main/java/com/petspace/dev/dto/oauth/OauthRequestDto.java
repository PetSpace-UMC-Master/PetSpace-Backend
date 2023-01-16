package com.petspace.dev.dto.oauth;

import lombok.Getter;

@Getter
public class OauthRequestDto {

    // 프론트에서 넘겨주는 accessToken, JWT 관련 아님에 주의!
    private String accessToken;
}
