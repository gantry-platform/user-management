package kr.co.inslab.exception;

import org.springframework.http.HttpStatus;

public class APIException extends Exception{

    private HttpStatus httpStatus;

    public APIException(String resource, HttpStatus httpStatus) {
        super("Exception Resource : "+resource);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
}
