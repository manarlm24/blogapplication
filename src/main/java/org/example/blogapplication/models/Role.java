package org.example.blogapplication.models;


import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    USER,
    ADMIN;

    @Override
    public String getAuthority() {
        return "ROLE_" + name();  // 👈 Required by Spring Security
    }
}
