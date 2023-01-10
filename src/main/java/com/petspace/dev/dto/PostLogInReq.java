package com.petspace.dev.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostLogInReq {
    private String email;
    private String password;
}
