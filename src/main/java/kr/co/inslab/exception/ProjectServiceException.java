package kr.co.inslab.exception;

import org.springframework.http.HttpStatus;

public class ProjectServiceException extends Exception{

    private final HttpStatus httpStatus;

    public ProjectServiceException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
