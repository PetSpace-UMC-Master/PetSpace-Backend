package com.petspace.dev.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginResponseDto {
    private String result;

    private String msg;

    private String token;

    private Object object;

    public LoginResponseDto(String result, String msg, String token, Object object) {
        this.result = result;
        this.msg = msg;
        this.token = token;
        this.object = object;
    }
}