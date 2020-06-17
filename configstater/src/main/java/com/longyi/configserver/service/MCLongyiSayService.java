package com.longyi.configserver.service;

import com.longyi.configserver.config.LongyiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ly
 * @Description TODO
 * @date 2020/6/17 9:56
 */
@Service
public class  MCLongyiSayService {

    @Autowired
    private LongyiService longyiService;

    public void sayHello(){
    System.out.println(longyiService.join());
    }

}    
   