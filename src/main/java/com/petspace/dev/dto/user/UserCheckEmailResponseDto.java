package com.petspace.dev.dto.user;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserCheckEmailResponseDto {

    private String email;
    private Boolean isAvailable;
}
