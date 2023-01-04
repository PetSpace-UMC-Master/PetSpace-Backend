package com.petspace.dev.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ResponseDto {

    private String result;
    private String message;
    private Object object;

    public ResponseDto(String result, String message, Object object) {
        this.result = result;
        this.message = message;
        this.object = object;
    }
}
