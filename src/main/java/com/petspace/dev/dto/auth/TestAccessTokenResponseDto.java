package com.petspace.dev.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TestAccessTokenResponseDto {

    @JsonProperty("access_token")
    private String accessToken;
    private String scope;

    @JsonProperty("token_type")
    private String tokenType;

    @Builder
    public TestAccessTokenResponseDto(String accessToken, String scope, String tokenType) {
        this.accessToken = accessToken;
        this.scope = scope;
        this.tokenType = tokenType;
    }

}
