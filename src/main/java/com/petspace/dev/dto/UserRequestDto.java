package com.petspace.dev.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {
    private String username;
    private String nickname;
    private String birth;
    private String email;
    private String password;
    private boolean privacyAgreement;
    private boolean marketingAgreement;
    private boolean hostPermission;
    private String oauthProvider;
    private String status;

}
