package com.odianyun.search.whale.tracker;

import com.odianyun.search.whale.api.model.req.BaseSearchRequest;
import com.odianyun.search.whale.common.util.ConfigUtil;
import com.odianyun.search.whale.common.util.EmailUtil;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Administrator on 2016/4/26.
 */

public class ShopSearchCountTracker {
    //private Set<String> noResultSet = new HashSet<>();
    private Map<String,Integer> noResultSearchMap = new HashMap<>();
    private AtomicInteger totalSearchCount = new AtomicInteger(0);
    private AtomicInteger noResultSearchCount = new AtomicInteger(0);
    private static long sendIntervalMinus;
    private static float noResultThreshold;



    static {
        ConfigUtil.loadPropertiesFile("mail.properties");
        sendIntervalMinus = ConfigUtil.getLong("mail.sendIntervalMinus", 10);
        noResultThreshold = ConfigUtil.getFloat("mail.noResultThreshold",0.3f);

    }

    public ShopSearchCountTracker(){
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {//统计搜索词召回
                if(noResultSearchCount.intValue() != 0 && noResultSearchCount.floatValue()/totalSearchCount.intValue() > noResultThreshold){
                    Map<String,Object> noResultMap = new HashMap<String, Object>();
                    noResultMap.put("totalSearchCount",totalSearchCount);
                    noResultMap.put("noResultSearchCount",noResultSearchCount);
                    noResultMap.put("noResultRequestList",sortMapByValue(noResultSearchMap));
                    EmailUtil emailUtil = new EmailUtil();
                    emailUtil.sendMail("店铺查询出错",noResultMap);
                }
                noResultSearchCount.set(0);
                totalSearchCount.set(0);
                //noResultSet.clear();
                noResultSearchMap.clear();
            }
        },1000 * 60 * sendIntervalMinus, 1000 * 60 * sendIntervalMinus);
    }

    /*public void incrTotalSearchCount() {
        totalSearchCount.getAndIncrement();
    }*/

    public synchronized void checkHitNum(long hitNum,BaseSearchRequest shopSearchRequest){
        totalSearchCount.getAndIncrement();
        if(noResultSearchMap.containsKey(shopSearchRequest.toString())){
            Integer count = noResultSearchMap.get(shopSearchRequest.toString());
            count ++;
            noResultSearchMap.put(shopSearchRequest.toString(),count);
        }else{
            noResultSearchMap.put(shopSearchRequest.toString(),1);
        }
    }

    private  Map<String, Integer> sortMapByValue(Map<String, Integer> oriMap) {
        if (oriMap == null || oriMap.isEmpty()) {
            return null;
        }
        Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
        List<Map.Entry<String, Integer>> entryList = new ArrayList<Map.Entry<String, Integer>>(
                oriMap.entrySet());
        Collections.sort(entryList, new MapValueComparator());

        Iterator<Map.Entry<String, Integer>> iter = entryList.iterator();
        Map.Entry<String, Integer> tmpEntry = null;
        while (iter.hasNext()) {
            tmpEntry = iter.next();
            sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());
        }
        return sortedMap;
    }

    class MapValueComparator implements Comparator<Map.Entry<String, Integer>> {

        @Override
        public int compare(Map.Entry<String, Integer> me1, Map.Entry<String, Integer> me2) {

            return me2.getValue().compareTo(me1.getValue());
        }
    }

}


