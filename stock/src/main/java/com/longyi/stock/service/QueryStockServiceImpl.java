package com.longyi.stock.service;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class QueryStockServiceImpl implements QueryStockService {
    @Override
    public void queryStock(String skuCode) {
        List<String> list= Arrays.asList("aa","vv","ee");
        StringBuilder stringBuilder=new StringBuilder();
        list.stream().forEach(x->{
            for(int i=0;i<10000000;i++){
                stringBuilder.append(x+i);
            }
        });
        System.out.println(stringBuilder.toString());
    }
}
