package com.petspace.dev.oauth;

import com.petspace.dev.domain.OauthProvider;

public interface OAuthUserInfo {
    String getProviderId();

    OauthProvider getProvider();

    String getEmail();

    String getNickName();
}
