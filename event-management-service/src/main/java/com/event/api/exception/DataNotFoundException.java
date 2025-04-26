package com.event.api.exception;

public class DataNotFoundException extends RuntimeException{

    public DataNotFoundException(String msg){
        super(msg);
    }
}
