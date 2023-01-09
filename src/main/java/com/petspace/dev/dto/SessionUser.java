package com.petspace.dev.dto;

import com.petspace.dev.domain.User;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {
    private String nickname;
    private String email;
    private String imgUrl;
    private String birth;

    public SessionUser(User user) {
        this.nickname = user.getNickname();
        this.email = user.getEmail();
        this.imgUrl = user.getImgUrl();
        this.birth = user.getBirth();
    }
}
