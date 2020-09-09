package com.odianyun.search.whale.index.sort;

import com.odianyun.search.whale.data.model.HotSaleType;
import com.odianyun.search.whale.data.model.MerchantProductSale;
import com.odianyun.search.whale.data.service.ConfigService;
import com.odianyun.search.whale.data.service.HotSaleMerchantProductService;
import com.odianyun.search.whale.index.api.common.SearchUpdateSender;
import com.odianyun.search.whale.index.common.ProcessorApplication;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by fishcus on 16/11/23.
 */
public class MerchantProductSaleSorter {

    HotSaleMerchantProductService hotSaleMerchantProductService;

    ConfigService configService;

    public static MerchantProductSaleSorter instance = new MerchantProductSaleSorter();

    BlockingQueue<MerchantProductSale> queue = new LinkedBlockingQueue<MerchantProductSale>(500);

    static int MAX_NUM = 100;

    static int MERCHANT_MAX_NUM = 10;

    static final String NULL_STR = "null";

    Map<String,FixSizedPriorityQueue> merchantMap = new ConcurrentHashMap<String,FixSizedPriorityQueue>();

//    FixSizedPriorityQueue<MerchantProductSale> sortedQueue = new FixSizedPriorityQueue(MAX_NUM);

    private MerchantProductSaleSorter(){
    }

    public void init(){
        if(hotSaleMerchantProductService == null){
            hotSaleMerchantProductService = (HotSaleMerchantProductService) ProcessorApplication.getBean("hotSaleMerchantProductService");
        }
        if(configService == null){
            configService = (ConfigService) ProcessorApplication.getBean("configService");
        }
//        sortedQueue = new FixSizedPriorityQueue(MAX_NUM);
        merchantMap = new ConcurrentHashMap<String,FixSizedPriorityQueue>();
    }

    public void doSort(MerchantProductSale mpSale) {
//        sortedQueue.add(mpSale);

        Long companyId = mpSale.getCompanyId();
        Long merchantId = mpSale.getMerchantId();
        //门店topK

        String key = companyId+"_"+merchantId;
        doSortByKey(mpSale,key);
        //公司topK
        key = companyId+"_"+NULL_STR;
        doSortByKey(mpSale,key);

    }

    private void doSortByKey(MerchantProductSale mpSale, String key) {
        int size = 0;
        if(key.endsWith(NULL_STR)){
            size = configService.getInt("company_hot_sale_num",MAX_NUM,mpSale.getCompanyId().intValue());;
        }else{
            size = configService.getInt("merchant_hot_sale_num",MERCHANT_MAX_NUM,mpSale.getCompanyId().intValue());
        }
        if(size <= 0){
            return;
        }
        FixSizedPriorityQueue mqueue = merchantMap.get(key);
        if(mqueue == null){
            synchronized (MerchantProductSaleSorter.class){
                mqueue = merchantMap.get(key);
                if(mqueue == null){
                    mqueue = new FixSizedPriorityQueue(size);
                    merchantMap.put(key,mqueue);

                }
            }
        }
        mqueue.add(mpSale);
    }


    public void done() throws Exception

        /*if(sortedQueue.get().size() > 0){
            hotSaleMerchantProductService.batchSave(new ArrayList<MerchantProductSale>(sortedQueue.get()), HotSaleType.COMPANY_HOT.getCode());
        }*/
        {
            if(merchantMap != null && merchantMap.size() > 0){
                for(Map.Entry<String,FixSizedPriorityQueue> entry : merchantMap.entrySet()){
                    FixSizedPriorityQueue tempQueue = entry.getValue();
                    String key = entry.getKey();
                    if(tempQueue.get().size() > 0){
                        Integer code = HotSaleType.COMPANY_HOT.getCode();
                        if(!key.endsWith(NULL_STR)){
                            code = HotSaleType.MERCHANT_HOT.getCode();

                        }
                        hotSaleMerchantProductService.batchSave(new ArrayList<MerchantProductSale>(tempQueue.get()), code);

                }
            }
        }

    }


    private static class SortCacheContext{
        Map<Long,FixSizedPriorityQueue> merchantMap = new ConcurrentHashMap<Long,FixSizedPriorityQueue>();
    }
}
