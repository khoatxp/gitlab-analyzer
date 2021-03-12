package com.eris.gitlabanalyzer.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// Based on https://www.baeldung.com/exception-handling-for-rest-with-spring#controlleradvice
// and https://stackoverflow.com/a/26449450
@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler  {
    @ExceptionHandler(value = { IllegalStateException.class })
    protected void handleIllegalState(RuntimeException ex, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }
}
