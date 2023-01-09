package com.petspace.dev.dto;

import com.petspace.dev.domain.User;
import lombok.Getter;

@Getter
public class UserLoginRequestDto {

    private String email;
    private String password;

    // dto -> entity
    public User toEntity() {
        return User.builder()
                .email(email)
                .password(password)
                .build();
    }
}
