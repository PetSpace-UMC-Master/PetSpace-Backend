package com.petspace.dev.domain;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Role {
    GUEST("ROLE_GUEST"),
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN");

    private final String roleName;
}
