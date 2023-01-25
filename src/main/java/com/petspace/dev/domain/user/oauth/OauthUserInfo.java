package com.petspace.dev.domain.user.oauth;

import com.petspace.dev.domain.user.OauthProvider;

public interface OauthUserInfo {
    String getProviderId();

    OauthProvider getProvider();

    String getEmail();

    String getNickName();
}
