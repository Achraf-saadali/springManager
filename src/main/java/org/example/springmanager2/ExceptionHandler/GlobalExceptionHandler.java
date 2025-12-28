package org.example.springmanager2.ExceptionHandler;


import org.example.springmanager2.Controller.Status;
import org.example.springmanager2.Exception.RolesException;
import org.example.springmanager2.Exception.WrongCredentialsException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Status handleInvalidPassword(Exception ex) {
        return new Status(401,ex.toString());
    }

}