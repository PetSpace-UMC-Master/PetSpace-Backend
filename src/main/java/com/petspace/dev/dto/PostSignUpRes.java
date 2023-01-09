package com.petspace.dev.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostSignUpRes {
    private Long userIdx;
    // TODO Res 로 어느 정보 넘겨줄지 논의
    private String welcomeMessage;
}
