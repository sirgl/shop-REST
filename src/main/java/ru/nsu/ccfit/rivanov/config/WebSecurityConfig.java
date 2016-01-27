package ru.nsu.ccfit.rivanov.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import ru.nsu.ccfit.rivanov.security.HeaderProcessingFilter;
import ru.nsu.ccfit.rivanov.security.TokenProcessingAuthenticationProvider;
import ru.nsu.ccfit.rivanov.security.TokenRepository;
import ru.nsu.ccfit.rivanov.security.login.LoginFilter;

@Configuration
@EnableWebMvcSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    public static final String LOGIN_PATH = "/login";
    private static final String ADMIN_LOGIN = "admin";  //May be extracted to properties
    private static final String ADMIN_PASSWORD = "password";
    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private HeaderProcessingFilter headerProcessingFilter;

    @Autowired
    private TokenProcessingAuthenticationProvider tokenProcessingAuthenticationProvider;

    @Autowired
    private LoginFilter loginFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .addFilterBefore(loginFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(headerProcessingFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests().anyRequest().permitAll();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .authenticationProvider(tokenProcessingAuthenticationProvider)
                .inMemoryAuthentication()
                .withUser(ADMIN_LOGIN).password(ADMIN_PASSWORD).roles("admin"); // TODO extract to properties until prod
    }

    @Bean
    public TokenProcessingAuthenticationProvider tokenProcessingAuthenticationProvider() {
        return new TokenProcessingAuthenticationProvider(tokenRepository, userDetailsService);
    }


    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    @Override
    public UserDetailsService userDetailsServiceBean() throws Exception {
        return super.userDetailsServiceBean();
    }

    @Bean
    public HeaderProcessingFilter headerProcessingFilter() {
        HeaderProcessingFilter headerProcessingFilter = new HeaderProcessingFilter();
        headerProcessingFilter.setAuthenticationManager(authenticationManager);
        return headerProcessingFilter;
    }

    @Bean
    public LoginFilter customUsernamePasswordAuthenticationFilter() {
        AntPathRequestMatcher matcher = new AntPathRequestMatcher(LOGIN_PATH);
        LoginFilter filter = new LoginFilter(matcher);
        filter.setAuthenticationManager(authenticationManager);
        return filter;
    }
}
