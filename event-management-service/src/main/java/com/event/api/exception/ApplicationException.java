package com.event.api.exception;

public class ApplicationException extends RuntimeException{

    private int errorCode;

    public ApplicationException(int errorCode,String msg){
        super(msg);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
