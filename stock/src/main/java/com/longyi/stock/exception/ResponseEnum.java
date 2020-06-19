package com.longyi.stock.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ly
 * @Description TODO
 * @date 2020/6/19 11:47
 */

@Getter
@AllArgsConstructor
public enum  ResponseEnum implements BusinessExceptionAssert{
    /**
     * Bad licence type
     */
    BAD_LICENCE_TYPE(7001, "Bad licence type."),
    /**
     * Licence not found
     */
    LICENCE_NOT_FOUND(7002, "Licence not found."),


    ;

    /**
     * 返回码
     */
    private int code;
    /**
     * 返回消息
     */
    private String message;
}
   