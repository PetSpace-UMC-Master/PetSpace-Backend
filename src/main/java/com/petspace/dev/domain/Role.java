package com.petspace.dev.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    GUEST("ROLE_GUEST", "게스트"),
    USER("ROLE_USER", "사용자");

    private final String key;
    private final String title;
}
