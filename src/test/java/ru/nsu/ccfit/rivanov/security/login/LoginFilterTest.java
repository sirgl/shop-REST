package ru.nsu.ccfit.rivanov.security.login;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LoginFilterTest {
    private final LoginFilter loginFilter = new LoginFilter(new AntPathRequestMatcher("/**"));
    private final AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);


    @Before
    public void setUp() throws Exception {
        loginFilter.setAuthenticationManager(authenticationManager);
    }

    @Test
    public void testSuccessLogin() throws Exception {
        when(request.getMethod()).thenReturn("POST");
        when(request.getHeader(LoginFilter.LOGIN_HEADER_NAME)).thenReturn("login");
        when(request.getHeader(LoginFilter.PASSWORD_HEADER_NAME)).thenReturn("pass");
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken("login", "pass");
        when(authenticationManager.authenticate(any())).thenReturn(token);

        Authentication authentication = loginFilter.attemptAuthentication(request, response);
        assertThat(authentication)
                .isEqualTo(token);
    }

    @Test(expected = BadCredentialsException.class)
    public void testFailureLogin() throws Exception {
        when(request.getMethod()).thenReturn("POST");
        when(request.getHeader(LoginFilter.LOGIN_HEADER_NAME)).thenReturn("login");
        when(request.getHeader(LoginFilter.PASSWORD_HEADER_NAME)).thenReturn("pass");
        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("nsg"));

        loginFilter.attemptAuthentication(request, response);
    }

    @Test(expected = AuthenticationServiceException.class)
    public void testWrongMethod() throws Exception {
        when(request.getMethod()).thenReturn("GET");
        loginFilter.attemptAuthentication(request, response);
    }
}
