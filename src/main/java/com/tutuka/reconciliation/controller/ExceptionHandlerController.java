package com.tutuka.reconciliation.controller;

import com.tutuka.reconciliation.dto.GenericResponse;
import com.tutuka.reconciliation.exception.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@Slf4j
@ResponseBody
@ControllerAdvice(annotations = RestController.class, basePackages = "com.tutuka.reconciliation")
public class ExceptionHandlerController {

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public GenericResponse<?> handleBadRequestException(BadRequestException e) {
        return new GenericResponse<>(HttpStatus.BAD_REQUEST.getReasonPhrase(), e.getMessage(), null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public GenericResponse<?> handleCustomValidationException(MethodArgumentNotValidException e) {
        return new GenericResponse<>(HttpStatus.BAD_REQUEST.getReasonPhrase(), e.getMessage(), null);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public GenericResponse<?> handleBadRequestException(Exception e) {
        return new GenericResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), "An error occurred while processing your request"+
                "Please contact server Administrator for more details", null);
    }
}
