package com.longyi.csjl.retrofitscan;

import java.io.Serializable;

/**
 * @author ly
 * @Description TODO
 * @date 2020/8/12 9:39
 */
public class Response<T> implements Serializable {
    private static final long serialVersionUID = -4505655308965878999L;
    private static final String successCode = "0";

    private T data;

    private String code = "0";

    private Object msg = "success";

    private Response() {
    }

    private Response success(T data) {
        this.data = data;
        return this;
    }

    private Response fail(String code, Object msg) {
        this.code = code;
        this.msg = msg;
        return this;
    }

    public static Response builderSuccess(Object o) {
        return (new Response()).success(o);
    }

    public static Response builderFail(String code, Object msg) {
        return (new Response()).fail(code, msg);
    }

    public T getData() {
        return this.data;
    }

    public String getCode() {
        return this.code;
    }

    public Object getMsg() {
        return this.msg;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setMsg(Object msg) {
        this.msg = msg;
    }


}