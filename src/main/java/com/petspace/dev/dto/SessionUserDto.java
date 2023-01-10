package com.petspace.dev.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter @Setter
public class SessionUserDto implements Serializable {
    private String username;
    private String nickname;
    private String email;
    private String imgUrl;
    private String birth;
    private String password;

    public SessionUserDto(String username, String nickname, String email, String imgUrl, String birth, String password) {
        this.username = username;
        this.nickname = nickname;
        this.email = email;
        this.imgUrl = imgUrl;
        this.birth = birth;
        this.password = password;
    }
}
