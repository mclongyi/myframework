package com.odianyun.search.whale.common.util;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fu Yifan on 2017/8/19.
 */
public class JsonUtil {

    public static <T> List<T> parseJsonToList(String jsonString, java.lang.Class<T> clazz) {
        List<T> result = JSON.parseArray(jsonString, clazz);
        return result;
    }

    public static String getJsonStringByList (List<?> list) {
        return JSON.toJSONString(list);
    }

    public static void main(String[] args) {
        List<String> aa = new ArrayList<>();
        aa.add("官方店");
        aa.add("旗舰店");
        aa.add("自营店");
        aa.add("专营店");
        aa.add("直营店");
        String jsonstring = JsonUtil.getJsonStringByList(aa);
        System.out.println(jsonstring);
        List<String> bb = JsonUtil.parseJsonToList(jsonstring, String.class);
        for (String stringClass : bb) {
            System.out.println(stringClass);
        }
    }
}
