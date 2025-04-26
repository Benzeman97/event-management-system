package com.event.api.exception;

public class ApplicationException extends RuntimeException{

    private String errorCode;

    public ApplicationException(String errorCode,String msg){
        super(msg);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
