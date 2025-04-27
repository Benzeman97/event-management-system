package com.event.api.exception;

import com.event.api.dto.response.ErrorMessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorMessageResponse> handleApplicationException(ApplicationException ex, Locale locale) {
        ErrorMessageResponse errorMessage = new ErrorMessageResponse(
                ex.getErrorCode(),
                messageSource.getMessage(ex.getMessage(),null,locale)
        );
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<ErrorMessageResponse> handleDataNotFoundException(DataNotFoundException ex, Locale locale) {
        ErrorMessageResponse errorMessage = new ErrorMessageResponse(
                HttpStatus.NOT_FOUND.value(),
                messageSource.getMessage(ex.getMessage(),null,locale)
        );
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessageResponse> handleGenericException(Exception ex, Locale locale) {
        ErrorMessageResponse errorMessage = new ErrorMessageResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                messageSource.getMessage("error.internal.server",null,locale)
        );
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
