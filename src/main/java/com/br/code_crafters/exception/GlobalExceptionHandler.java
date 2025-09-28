package com.br.code_crafters.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT) // HTTP 409
    public ModelAndView handleUserAlreadyExists(UserAlreadyExistsException ex) {
        ModelAndView mav = new ModelAndView("register");
        mav.addObject("error", ex.getMessage());
        return mav;
    }
}
