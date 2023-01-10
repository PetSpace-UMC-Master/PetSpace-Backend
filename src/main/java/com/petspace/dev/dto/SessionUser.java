package com.petspace.dev.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter @Setter
@Builder
public class SessionUser implements Serializable {
    private String nickname;
    private String email;
    private String imgUrl;
    private String birth;


}
