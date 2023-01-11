package com.petspace.dev.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter @Setter
public class OauthUserDto implements Serializable {

    private String email;

    public OauthUserDto(String email) {
        this.email = email;
    }
}
