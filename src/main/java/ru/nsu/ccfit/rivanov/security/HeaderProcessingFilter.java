package ru.nsu.ccfit.rivanov.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


//Not extends AbstractAuthenticationProcessingFilter because we need user to have anonymous access if no token present
public class HeaderProcessingFilter extends GenericFilterBean {
    public static final String HEADER_NAME = "X_ACCESS_TOKEN";

    private AuthenticationManager authenticationManager;

    public AuthenticationManager getAuthenticationManager() {
        return authenticationManager;
    }

    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;

        String token = request.getHeader(HEADER_NAME);
        if (token == null) {
            token = "";
        }

        TokenAuthenticationToken authenticationToken = new TokenAuthenticationToken(token);

        Authentication authentication;
        try {
            authentication = getAuthenticationManager().authenticate(authenticationToken);
        } catch (AuthenticationException ignored) {
            filterChain.doFilter(req, resp);
            return;
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(req, resp);
    }
}
