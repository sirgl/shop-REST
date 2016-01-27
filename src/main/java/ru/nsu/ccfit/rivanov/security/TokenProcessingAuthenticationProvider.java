package ru.nsu.ccfit.rivanov.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public class TokenProcessingAuthenticationProvider implements AuthenticationProvider {
    private final TokenRepository tokenRepository;
    private final UserDetailsService userDetailsService;


    @Autowired
    public TokenProcessingAuthenticationProvider(TokenRepository tokenRepository, UserDetailsService userDetailsService) {
        this.tokenRepository = tokenRepository;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        TokenAuthenticationToken authenticationToken = (TokenAuthenticationToken) authentication;
        String token = (String) authenticationToken.getDetails(); // always not null

        if(token.equals("")) {
            throw new BadCredentialsException("Token missed");
        }

        TokenData tokenData = tokenRepository.getTokenData(token);

        if(tokenData == null) {
            throw new BadCredentialsException("No user with such token");
        } else if(System.currentTimeMillis() > tokenData.getDate().getTime()) {
            throw new BadCredentialsException("Token expired");
        }

        String login = tokenData.getLogin();

        UserDetails userDetails = userDetailsService.loadUserByUsername(login);

        return new UsernamePasswordAuthenticationToken(userDetails.getUsername(),
                userDetails.getPassword(),
                userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(TokenAuthenticationToken.class);
    }
}
