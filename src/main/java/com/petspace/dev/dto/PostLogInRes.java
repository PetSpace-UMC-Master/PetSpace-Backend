package com.petspace.dev.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostLogInRes {
    private Long userIdx;
    private String jwt;
}
