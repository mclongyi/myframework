package com.longyi.stock.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author ly
 * @Description TODO
 * @date 2020/6/19 11:53
 */
@Data
@AllArgsConstructor
public class ErrorResponse {

    private Integer code;

    private String message;

}    
   