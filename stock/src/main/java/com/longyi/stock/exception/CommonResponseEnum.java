package com.longyi.stock.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommonResponseEnum implements BusinessExceptionAssert{

    SERVER_ERROR(9001,"服务器异常");


    /**
     * 返回码
     */
    private int code;
    /**
     * 返回消息
     */
    private String message;
}
