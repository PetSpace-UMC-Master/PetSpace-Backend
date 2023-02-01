package com.petspace.dev.dto.auth;

import lombok.Getter;

@Getter
public class LoginTokenReissueRequestDto {

    private String accessToken;
    private String refreshToken;
}
