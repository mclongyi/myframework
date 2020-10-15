package com.longyi.csjl.thread.local;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author ly
 * @description TODO
 * @date 2020/10/13 22:25
 * @throw
 */
public class ThreadLocalTest {

    ThreadLocal<SimpleDateFormat> threadLocal=new ThreadLocal(){
        @Override
        protected Object initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    public void convertDate(){
        SimpleDateFormat simpleDateFormat = threadLocal.get();
        String format = simpleDateFormat.format(new Date());
        System.out.println(format);
    }


}    
   