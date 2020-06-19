package com.sepfort.sheet.domain;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    /** @noinspection checkstyle:JavadocVariable*/ USER,
    /** @noinspection checkstyle:JavadocVariable*/ ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}
