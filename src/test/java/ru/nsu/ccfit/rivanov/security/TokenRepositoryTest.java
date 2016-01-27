package ru.nsu.ccfit.rivanov.security;

import org.junit.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

public class TokenRepositoryTest {
    TokenRepository repository = new TokenRepository();

    @Test
    public void testAdd() throws Exception {
        TokenData token = new TokenData("login", new Date());
        repository.setTokenData("token", token);

        assertThat(repository.getTokenData("token"))
                .isEqualTo(token);
    }
}
