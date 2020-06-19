package kr.co.inslab.utils;

import java.security.SecureRandom;
import java.util.Base64;

public class SimpleToken {

    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder();

    public static String generateNewToken(){
        byte[] randomByte = new byte[24];
        secureRandom.nextBytes(randomByte);
        return base64Encoder.encodeToString(randomByte);
    }
}
