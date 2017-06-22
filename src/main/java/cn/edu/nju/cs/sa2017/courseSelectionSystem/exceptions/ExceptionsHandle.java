package cn.edu.nju.cs.sa2017.courseSelectionSystem.exceptions;


import cn.edu.nju.cs.sa2017.courseSelectionSystem.models.Error;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.UnexpectedTypeException;

@RestControllerAdvice
public class ExceptionsHandler {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Error> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        e.printStackTrace();
        return new ResponseEntity<Error>(Error.typedError(HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnexpectedTypeException.class)
    public ResponseEntity<Error> handleUnexpectedTypeException(UnexpectedTypeException e) {
        e.printStackTrace();
        return new ResponseEntity<Error>(Error.typedError(HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Error> handleException(Exception e) {
        e.printStackTrace();
        return new ResponseEntity<Error>(Error.Error503(), HttpStatus.SERVICE_UNAVAILABLE);
    }

}
