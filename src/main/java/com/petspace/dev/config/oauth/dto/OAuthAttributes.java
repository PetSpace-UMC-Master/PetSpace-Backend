package com.petspace.dev.config.oauth.dto;

import com.petspace.dev.config.oauth.provider.KakaoUserInfo;
import com.petspace.dev.domain.Status;
import com.petspace.dev.domain.User;

import java.util.Arrays;
import java.util.Map;

public enum OAuthAttributes {
    KAKAO("kakao") {
        public User of(Map<String, Object> attributes) {
            KakaoUserInfo kakaoUserInfo = new KakaoUserInfo(attributes);
            return User.builder()
                    .nickname(kakaoUserInfo.getNickName())
                    .email(kakaoUserInfo.getEmail())
                    .oauthProvider(kakaoUserInfo.getProvider())
                    .privacyAgreement(true)
                    .hostPermission(false)
                    .status(Status.ACTIVE)
                    .build();
        }
    };

    private final String providerName;

    OAuthAttributes(String providerName) {
        this.providerName = providerName;
    }

    public static User extract(String providerName, Map<String, Object> userAttributes) {
        return Arrays.stream(values())
                .filter(provider -> providerName.equals(provider.providerName))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new)
                .of(userAttributes);
    }

    public abstract User of(Map<String, Object> attributes);
}
