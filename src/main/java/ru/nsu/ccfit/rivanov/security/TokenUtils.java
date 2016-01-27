package ru.nsu.ccfit.rivanov.security;

import javax.xml.bind.DatatypeConverter;
import java.security.SecureRandom;

public class TokenUtils {
    private final static SecureRandom random = new SecureRandom();
    private static final int TOKEN_BYTE_SIZE = 20;

    private TokenUtils(){}

    public static String generateToken() {
        byte[] buffer = new byte[TOKEN_BYTE_SIZE];
        random.nextBytes(buffer);
        return DatatypeConverter.printHexBinary(buffer);
    }
}
