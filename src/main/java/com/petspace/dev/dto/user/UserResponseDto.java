package com.petspace.dev.dto.user;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponseDto {

    private Long id;
    private String email;
    private String username;
    private String nickname;
    private String profileImage;
    private String birth;
}
