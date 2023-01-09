package com.petspace.dev.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserLoginResponseDto {

    private String email;
    private String accessToken;
    private String refreshToken;
}
