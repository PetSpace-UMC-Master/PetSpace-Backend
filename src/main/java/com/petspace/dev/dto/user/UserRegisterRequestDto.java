package com.petspace.dev.dto.user;

import com.petspace.dev.domain.OauthProvider;
import com.petspace.dev.domain.Status;
import com.petspace.dev.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Getter
@NoArgsConstructor
public class UserRegisterRequestDto {

    private String username;
    private String nickname;
    private String birth;
    private String email;
    private String password;
    private boolean marketingAgreement;

    //default value
    private boolean privacyAgreement;
    private boolean hostPermission;
    private OauthProvider oauthProvider;
    private Status status;

    @Builder
    public UserRegisterRequestDto(String username, String nickname, String birth, String email,
                                  String password, boolean marketingAgreement) {
        this.username = username;
        this.nickname = nickname;
        this.birth = birth;
        this.email = email;
        this.password = password;
        this.marketingAgreement = marketingAgreement;

        // default value
        privacyAgreement = true;
        hostPermission = false;
        oauthProvider = OauthProvider.NONE;
        status = Status.ACTIVE;
    }

    public User toEntity() {
        //default value
        privacyAgreement = true;
        hostPermission = false;
        oauthProvider = OauthProvider.NONE;
        status = Status.ACTIVE;

        return User.builder().username(username).nickname(nickname).birth(birth).email(email).password(password)
                .privacyAgreement(privacyAgreement).marketingAgreement(marketingAgreement).hostPermission(hostPermission)
                .oauthProvider(oauthProvider).status(status).build();
    }
}
