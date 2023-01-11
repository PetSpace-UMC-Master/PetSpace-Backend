package com.petspace.dev.dto;

import com.petspace.dev.domain.OauthProvider;
import com.petspace.dev.domain.Status;
import com.petspace.dev.domain.User;
import lombok.Getter;

@Getter
public class UserJoinRequestDto {

    private String email;
    private String password;
    private String username;
    private String nickname;
    private String birth;
    private boolean marketingAgreement;

    // dto -> entity
    public User toEntity() {
        return User.builder()
                .email(email)
                .password(password)
                .username(username)
                .nickname(nickname)
                .birth(birth)
                .marketingAgreement(marketingAgreement)
                .privacyAgreement(true)
                .hostPermission(false)
                .oauthProvider(OauthProvider.NONE)
                .status(Status.ACTIVE)
                .build();
    }
}
