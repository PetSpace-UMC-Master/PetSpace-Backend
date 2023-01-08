package com.petspace.dev.config.oauth.provider;

import com.petspace.dev.domain.OauthProvider;

public interface OAuthUserInfo {
    String getProviderId();

    OauthProvider getProvider();

    String getEmail();

    String getNickName();
}
