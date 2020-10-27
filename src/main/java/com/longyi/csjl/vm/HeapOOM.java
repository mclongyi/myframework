package com.longyi.csjl.vm;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ly
 * @description TODO
 * @date 2020/10/21 22:29
 * @throw
 */
public class HeapOOM {

    static class objectMapper{

    }

    public static void main(String[] args) {
             List<ObjectMapper> list=new ArrayList<>();
             while (true){
                 list.add(new ObjectMapper());
             }
    }

}

   