package kr.co.inslab.exception;

import org.springframework.http.HttpStatus;

public class KeyCloakAdminException extends Exception{

    private final HttpStatus httpStatus;

    public KeyCloakAdminException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
