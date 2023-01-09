package com.petspace.dev.oauth2;

import com.petspace.dev.domain.HostPermission;
import com.petspace.dev.domain.OauthProvider;
import com.petspace.dev.domain.Status;
import com.petspace.dev.domain.User;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributes {
    private Map<String, Object> attributes; // OAuth2 반환하는 유저 정보 Map
    private String nameAttributeKey;
    private String nickname;
    private String email;
    private String imgUrl;
    private String birth;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String nickname, String email, String imgUrl, String birth) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.nickname = nickname;
        this.email = email;
        this.imgUrl = imgUrl;
        this.birth = birth;
    }

    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes){
        // 여기서 네이버와 카카오 등 구분 (ofNaver, ofKakao) 등
        switch (registrationId) {
            case "kakao":
                return ofKakao(userNameAttributeName, attributes);
        }

        // TODO: Exception 발생
        return null;
    }

    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
        // kakao는 kakao_account에 유저정보가 있다. (email)
        Map<String, Object> kakaoAccount = (Map<String, Object>)attributes.get("kakao_account");
        // kakao_account안에 또 profile이라는 JSON객체가 있다. (nickname, profile_image)
        Map<String, Object> kakaoProfile = (Map<String, Object>)kakaoAccount.get("profile");

        return OAuthAttributes.builder()
                .nickname((String) kakaoProfile.get("nickname"))
                .email((String) kakaoAccount.get("email"))
                .imgUrl((String) kakaoProfile.get("profile_image_url"))
                .birth((String) kakaoProfile.get("birthday"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    public User toEntity(){
        return User.builder()
                .nickname(nickname)
                .email(email)
                .imgUrl(imgUrl)
                .birth(birth)
                .hostPermission(HostPermission.GUEST)
                .oauthProvider(OauthProvider.KAKAO)
                .status(Status.ACTIVE)
                .build();
    }
}