package com.longyi.csjl.effective;

import lombok.Data;

import java.util.Date;

/**
 * @author ly
 * @Description TODO
 * @date 2020/7/13 12:44
 */
public class Period {

    private final Date start;

    private final Date end;

    public Period(Date start,Date end) {
        if(start.compareTo(end)>0){
            throw new IllegalArgumentException(start+" after "+end);
        }
        this.start=new Date(start.getTime());
        this.end=new Date(end.getTime());
    }

    public Date getStart(){
        return start;
    }

    public Date getEnd(){
        return end;
    }

}    
   