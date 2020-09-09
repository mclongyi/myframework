package com.odianyun.search.autobot.index.suggest;

import com.odianyun.search.whale.index.api.model.req.HistoryWriteRequest;
import com.odianyun.search.whale.index.history.redis.UserHistoryResdis;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by zengfenghua on 16/11/2.
 */
public class UserHistoryResdisTest {

    static ExecutorService executorService= new ThreadPoolExecutor(5,5,60,
            TimeUnit.MILLISECONDS,new ArrayBlockingQueue<Runnable>(100),
            new ThreadPoolExecutor.CallerRunsPolicy());
    public static void main(String[] args){
        System.setProperty("global.config.path", "/data/ds_trunk_env");
        final UserHistoryResdis userHistoryResdis=new UserHistoryResdis();

        for(int i=0;i<10000;i++){
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    HistoryWriteRequest historyWriteRequest=new HistoryWriteRequest(10,"1","2");
                    try{
                        userHistoryResdis.setUserHisttory(historyWriteRequest,null);
                    }catch (Exception e){
                       e.printStackTrace();
                    }
                }
            });


        }



    }

}
