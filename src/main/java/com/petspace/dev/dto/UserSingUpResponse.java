package com.petspace.dev.dto;

import com.petspace.dev.domain.OauthProvider;
import com.petspace.dev.domain.Status;
import com.petspace.dev.domain.User;
import lombok.Data;

@Data
public class UserSingUpResponse {

    String email;
    String password;
    String username;
    String nickname;
    String birth;
    boolean marketingAgreement;
    boolean privacyAgreement;
    OauthProvider oauthProvider;
    Status status;

    public UserSingUpResponse(String email, String password, String username, String nickname, String birth, boolean marketingAgreement, boolean privacyAgreement) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.nickname = nickname;
        this.birth = birth;
        this.marketingAgreement = marketingAgreement;
        this.privacyAgreement = privacyAgreement;
        this.oauthProvider = oauthProvider;
        this.status = status;
    }

    public User toEntity() {
        return User.builder()
                .email(email)
                .password(password)
                .username(username)
                .nickname(nickname)
                .birth(birth)
                .marketingAgreement(marketingAgreement)
                .privacyAgreement(privacyAgreement)
                .oauthProvider(oauthProvider)
                .status(status)
                .build();

    }
}
