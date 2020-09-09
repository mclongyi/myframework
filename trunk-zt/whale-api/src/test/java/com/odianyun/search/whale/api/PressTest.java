package com.odianyun.search.whale.api;

import com.odianyun.cc.client.spring.OccPropertiesLoaderUtils;
import com.odianyun.search.whale.api.model.req.SearchRequest;
import com.odianyun.search.whale.api.service.SearchClient;
import com.odianyun.search.whale.api.service.SearchService;

import java.util.concurrent.*;

/**
 * Created by zengfenghua on 16/10/30.
 */
public class PressTest extends AbstractTest{

    static ExecutorService executorService= new ThreadPoolExecutor(50,50,60,
            TimeUnit.MILLISECONDS,new ArrayBlockingQueue<Runnable>(100),
            new ThreadPoolExecutor.CallerRunsPolicy());

    public static void  main(String[] args){
        final SearchService searchService= SearchClient.getSearchService("local-test");
        for (int i=0;i<100;i++){
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    for(int j=0;j<100;j++){
                        try {
                            SearchRequest searchRequest = new SearchRequest(10);
                            searchRequest.setKeyword("" +j);
                            searchService.search(searchRequest);
                            System.out.println("j====" + j);
                        }catch (Throwable e){

                        }
                    }
                }
            });

            System.out.println("i===="+i);
        }
    }
}
