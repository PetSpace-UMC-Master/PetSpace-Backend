package com.petspace.dev.dto.user;

import lombok.Getter;

@Getter
public class UserTokenReissueRequestDto {

    private String accessToken;
    private String refreshToken;
}
