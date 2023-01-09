package com.petspace.dev.dto;

import com.petspace.dev.domain.OauthProvider;
import com.petspace.dev.domain.Status;
import com.petspace.dev.domain.User;
import lombok.*;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserSignUpReqDto {

    private String username;
    private String nickname;
    private String birth;
    private String email;
    private String password;

    public User toEntity(){
        return User.builder()
                .username(username)
                .nickname(nickname)
                .birth(birth)
                .email(email)
                .password(password)
                .oauthProvider(OauthProvider.NONE)
                .status(Status.ACTIVE)
                .build();
    }


}
