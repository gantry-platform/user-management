package kr.co.inslab.exception;


import kr.co.inslab.model.Error;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class GlobalDefaultExceptionHandler extends ResponseEntityExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(GlobalDefaultExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Error> defaultHandler(Exception ex,HttpServletResponse response) throws Exception{
        ex.printStackTrace();
        Error err = new Error();
        err.setCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
        err.setMessage(ex.getMessage());
        return new ResponseEntity<>(err,HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @ExceptionHandler(APIException.class)
    public ResponseEntity<Error> handlerConflict(Exception ex,HttpServletResponse response) throws Exception{
        ex.printStackTrace();
        Error err = new Error();
        HttpStatus status = ((APIException) ex).getHttpStatus();
        err.setCode(status.toString());
        err.setMessage(ex.getMessage());
        return new ResponseEntity<>(err,status);
    }

}
