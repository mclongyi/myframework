package com.longyi.csjl;


import com.longyi.csjl.effective.Period;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author ly
 * @Description TODO
 * @date 2020/7/13 12:48
 */
public class PeriodTest {

    @Test
    public void proidDateTest(){
        Date date=new Date();
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date end =new Date();
        Period p=new Period(date,end);
        end.setYear(10);
        System.out.println(format.format(p.getEnd()) );
    }



}    
   