package com.project.api.enumerations;

import static com.project.api.constants.Authority.*;

public enum Role {
    ROLE_USER(USER_AUTHORITIES),
    ROLE_ADMIN(ADMIN_AUTHORITIES);

    private String[] authorities;

    Role(String... authorities) {
        this.authorities = authorities;
    }

    public String[] getAuthorities() {
        return authorities;
    }
}
