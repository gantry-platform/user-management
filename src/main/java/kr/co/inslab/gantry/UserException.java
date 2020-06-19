package kr.co.inslab.gantry;

import org.springframework.http.HttpStatus;

public class UserException extends Exception{

    private final HttpStatus httpStatus;

    public UserException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
