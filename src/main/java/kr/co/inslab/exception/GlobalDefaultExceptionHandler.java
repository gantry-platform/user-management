package kr.co.inslab.exception;


import kr.co.inslab.model.Error;
import org.keycloak.common.VerificationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.WebApplicationException;

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
    public ResponseEntity<Error> apiExceptionHandler(Exception ex,HttpServletResponse response) throws Exception{
        ex.printStackTrace();
        Error err = new Error();
        HttpStatus status = ((APIException) ex).getHttpStatus();
        err.setCode(status.toString());
        err.setMessage(ex.getMessage());
        return new ResponseEntity<>(err,status);
    }

    @ExceptionHandler(KeyCloakAdminException.class)
    public ResponseEntity<Error> keyCloakExceptionHandler(Exception ex,HttpServletResponse response) throws Exception{
        ex.printStackTrace();
        Error err = new Error();
        HttpStatus status = ((KeyCloakAdminException) ex).getHttpStatus();
        String message = ((KeyCloakAdminException) ex).getMessage();
        err.setCode(status.toString());
        err.setMessage(message);
        return new ResponseEntity<>(err,status);
    }

    @ExceptionHandler(javax.ws.rs.WebApplicationException.class)
    public ResponseEntity<Error> webApplicationException(Exception ex,HttpServletResponse response) throws Exception{
        ex.printStackTrace();
        Error err = new Error();
        int statusCode = ((WebApplicationException) ex).getResponse().getStatus();
        String message = ((WebApplicationException) ex).getResponse().getStatusInfo().getReasonPhrase();
        HttpStatus status = HttpStatus.resolve(statusCode);
        err.setCode(status.toString());
        err.setMessage(message);
        return new ResponseEntity<>(err,status);
    }

    //임시코드
    @ExceptionHandler(VerificationException.class)
    public ResponseEntity<Error> verificationException(Exception ex,HttpServletResponse response) throws Exception{
        ex.printStackTrace();
        Error err = new Error();
        HttpStatus status = HttpStatus.resolve(401);
        err.setCode(status.toString());
        err.setMessage("Unauthorized temp");
        return new ResponseEntity<>(err,status);
    }





}
