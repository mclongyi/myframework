package com.odianyun.search.whale.rest.model.vo;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by Fu Yifan on 2017/8/16.
 */
public class SuggestVO implements Serializable {
    private static final long serialVersionUID = -7141803464481373774L;

    /** 关键词 **/
    private String keyword;
    /** 　搜索结果数　 **/
    private int count;
    /** 附加的信息 **/
    private Map attachedInfo;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Map getAttachedInfo() {
        return attachedInfo;
    }

    public void setAttachedInfo(Map attachedInfo) {
        this.attachedInfo = attachedInfo;
    }
}
