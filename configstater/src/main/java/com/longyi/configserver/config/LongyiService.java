package com.longyi.configserver.config;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author ly
 * @Description TODO
 * @date 2020/6/16 22:56
 */
@Data
public class LongyiService {
    private String name;
    private String desc;

    public LongyiService(){

    }

    public LongyiService(String name,String desc){
        this.name=name;
        this.desc=desc;
    }


    public String join(){
        return this.name+":"+this.desc;
    }

}    
   