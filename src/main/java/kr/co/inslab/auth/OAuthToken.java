package kr.co.inslab.auth;

import kr.co.inslab.model.Token;

public interface OAuthToken {
    Token getToken(String username, String password) throws Exception;
}
