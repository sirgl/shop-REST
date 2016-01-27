package ru.nsu.ccfit.rivanov.security.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.nsu.ccfit.rivanov.config.WebSecurityConfig;
import ru.nsu.ccfit.rivanov.security.TokenData;
import ru.nsu.ccfit.rivanov.security.TokenRepository;
import ru.nsu.ccfit.rivanov.security.TokenUtils;

import java.util.Date;

@RestController
public class LoginController {
    private static final long ACCESS_TIME = 1000 * 60 * 60 * 24 * 7;
    
    private final TokenRepository tokenRepository;

    @Autowired
    public LoginController(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @RequestMapping(value = WebSecurityConfig.LOGIN_PATH, method = RequestMethod.POST)
    public String doLogin(@RequestHeader String login) {
        String token = TokenUtils.generateToken();
        Date expirationDate = new Date(System.currentTimeMillis() + ACCESS_TIME);
        tokenRepository.setTokenData(token, new TokenData(login, expirationDate));
        return token;
    }
}
