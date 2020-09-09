package com.odianyun.search.whale.rest.model.vo;

import java.io.Serializable;

/**
 * Created by Fu Yifan on 2017/8/16.
 */
public class SearchKeywordVO implements Serializable {
    /**
     * 关键词
     */
    private String keyword;

    /**
     * 词类型
     */
    private Integer type;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
