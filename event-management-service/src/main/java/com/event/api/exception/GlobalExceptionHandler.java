package com.event.api.exception;

import com.event.api.dto.response.ErrorMessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorMessageResponse> handleApplicationException(ApplicationException ex) {
        ErrorMessageResponse errorMessage = new ErrorMessageResponse(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage()
        );
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<ErrorMessageResponse> handleDataNotFoundException(DataNotFoundException ex) {
        ErrorMessageResponse errorMessage = new ErrorMessageResponse(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage()
        );
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessageResponse> handleGenericException(Exception ex) {
        ErrorMessageResponse errorMessage = new ErrorMessageResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error"
        );
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
