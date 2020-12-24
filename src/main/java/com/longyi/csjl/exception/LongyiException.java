package com.longyi.csjl.exception;

public class LongyiException  extends RuntimeException{

    private String code;

    public LongyiException(String code) {
        this.code = code;
    }

    public LongyiException(String code, Object message) {
        super(message.toString());
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }


}
