package ru.nsu.ccfit.rivanov.security;

import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class TokenRepository {
    private Map<String, TokenData> tokenDataMap = new ConcurrentHashMap<>();

    public TokenData getTokenData(String token) {
        return tokenDataMap.get(token);
    }

    public void setTokenData(String token, TokenData tokenData) {
        tokenDataMap.put(token, tokenData);
    }
}
