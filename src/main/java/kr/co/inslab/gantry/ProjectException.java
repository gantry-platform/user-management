package kr.co.inslab.gantry;


import org.springframework.http.HttpStatus;

public class ProjectException extends Exception{

    private final HttpStatus httpStatus;

    public ProjectException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
