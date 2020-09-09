package com.odianyun.search.whale.data.model.geo;

import java.util.Date;

/**
 * Created by fishcus on 16/12/14.
 */
public class BusinessTime {

    private Integer state = 1;

    private String start;

    private String end;

    public BusinessTime(String start, String end){
        this.start = start;
        this.end = end;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }
}
