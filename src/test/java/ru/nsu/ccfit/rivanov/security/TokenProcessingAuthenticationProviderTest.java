package ru.nsu.ccfit.rivanov.security;

import org.junit.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.ArrayList;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TokenProcessingAuthenticationProviderTest {
    private final TokenRepository tokenRepository = mock(TokenRepository.class);
    private final UserDetailsService userDetailsService = mock(UserDetailsService.class);
    private final TokenProcessingAuthenticationProvider provider = new TokenProcessingAuthenticationProvider(tokenRepository, userDetailsService);

    @Test
    public void testSupport() throws Exception {

        assertThat(provider.supports(TokenAuthenticationToken.class))
                .isTrue();
    }

    @Test
    public void testSuccessfulAuthentication() throws Exception {
        TokenData tokenData = new TokenData("login", new Date(System.currentTimeMillis() + 100000000));
        when(tokenRepository.getTokenData("token")).thenReturn(tokenData);
        User user = new User("user", "pass", new ArrayList<>());
        when(userDetailsService.loadUserByUsername("login")).thenReturn(user);

        Authentication token = provider.authenticate(new TokenAuthenticationToken("token"));
        assertThat(token.getPrincipal())
                .isEqualTo("user");
        assertThat(token.getCredentials())
                .isEqualTo("pass");
    }

    @Test(expected = AuthenticationException.class)
    public void testExpiredAuthentication() throws Exception {
        TokenData tokenData = new TokenData("login", new Date(System.currentTimeMillis() - 1000));
        when(tokenRepository.getTokenData("token")).thenReturn(tokenData);

        provider.authenticate(new TokenAuthenticationToken("token"));
    }

    @Test(expected = AuthenticationException.class)
    public void testEmptyToken() throws Exception {
        provider.authenticate(new TokenAuthenticationToken(""));
    }

    @Test(expected = AuthenticationException.class)
    public void testUsernameNotFound() throws Exception {
        when(tokenRepository.getTokenData("token")).thenReturn(null);

        provider.authenticate(new TokenAuthenticationToken("token"));
    }
}
