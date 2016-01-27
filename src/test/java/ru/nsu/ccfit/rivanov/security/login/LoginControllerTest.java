package ru.nsu.ccfit.rivanov.security.login;

import org.junit.Test;
import ru.nsu.ccfit.rivanov.security.TokenRepository;

import static org.mockito.Mockito.*;

public class LoginControllerTest {
    private final TokenRepository repository = mock(TokenRepository.class);
    private final LoginController controller = new LoginController(repository);

    @Test
    public void testLogin() throws Exception {
        controller.doLogin("login");
        verify(repository, times(1)).setTokenData(anyString(), anyObject());
    }
}
