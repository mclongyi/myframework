package com.longyi.stock.exception;

import lombok.Data;

/**
 * @author ly
 * @Description TODO
 * @date 2020/6/19 11:37
 */
@Data
public class BaseException extends RuntimeException {

    private String message;

    private Integer code;

    private IResponseEnum responseEnum;

    private Object[] args;

    private Throwable throwable;

    public BaseException(String message){
        super();
        this.message=message;
    }

    public BaseException(IResponseEnum responseEnum){
        super(responseEnum.getMessage());
        this.message=responseEnum.getMessage();
        this.code=responseEnum.getCode();
    }

    public BaseException(IResponseEnum responseEnum,Object[] args,String message){
        super(message);
        this.message=message;
        this.args=args;
        this.responseEnum=responseEnum;
    }

    public BaseException(IResponseEnum responseEnum,Object[] args,String message,Throwable throwable){
        super(message,throwable);
        this.responseEnum=responseEnum;
        this.throwable=throwable;
        this.args=args;
        this.message=message;
    }

}    
   