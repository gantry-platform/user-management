package kr.co.inslab.api;

import org.springframework.http.HttpStatus;

public class ApiException extends Exception{

    private final HttpStatus httpStatus;

    public ApiException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
