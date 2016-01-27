package ru.nsu.ccfit.rivanov.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class TokenAuthenticationToken extends AbstractAuthenticationToken {
    public TokenAuthenticationToken(String token) {
        this(token, null);
    }

    public TokenAuthenticationToken(String token, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        setDetails(token);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }
}
