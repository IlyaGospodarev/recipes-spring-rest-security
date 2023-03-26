package recipes.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Object> handleInvalidParameterException() {
        return ResponseEntity.badRequest().build();
    }

}
