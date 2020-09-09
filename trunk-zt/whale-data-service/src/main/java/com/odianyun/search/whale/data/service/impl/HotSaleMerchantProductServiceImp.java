package com.odianyun.search.whale.data.service.impl;

import com.odianyun.search.whale.data.dao.HotSaleMerchantProductDao;
import com.odianyun.search.whale.data.model.HotSaleType;
import com.odianyun.search.whale.data.model.MerchantProductSale;
import com.odianyun.search.whale.data.service.HotSaleMerchantProductService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by fishcus on 16/11/23.
 */
public class HotSaleMerchantProductServiceImp implements HotSaleMerchantProductService {

    static Logger logger = Logger.getLogger(HotSaleMerchantProductServiceImp.class);

    HotSaleMerchantProductDao hotSaleMerchantProductDao;

    static final String NULL_STR = "null";

    private Map<String,List<Long>> hotSaleMerchantProductMap = new ConcurrentHashMap<String,List<Long>>();

    @Override
    public void batchSave(List<MerchantProductSale> merchantProductSaleList,Integer type) throws Exception{
        hotSaleMerchantProductDao.batchSave(merchantProductSaleList,type);
    }

    @Override
    public List<Long> getHotSaleMerchantProducts(List<Long> merchantIdList, int companyId,int start,int count) throws Exception {
        List<Long> mpIdList = new ArrayList<>();
        String key = null;
        if(CollectionUtils.isEmpty(merchantIdList)){
            key = genKey(companyId,null);
            mpIdList.addAll(pageResult(key,start,count));
        }else{
            for(Long mId : merchantIdList){
                key = genKey(companyId,mId);
                mpIdList.addAll(pageResult(key,start,count));
            }
        }
        return mpIdList;
    }

    @Override
    public Map<Long, List<Long>> getHotSaleMerchantProductMap(List<Long> merchantIdList, int companyId, int start, int count) throws Exception {
        Map<Long, List<Long>> mpIdMap = new HashedMap();
        String key = null;
        if(CollectionUtils.isEmpty(merchantIdList)){
            key = genKey(companyId,null);
            mpIdMap.put(null,pageResult(key,start,count));
        }else{
            for(Long mId : merchantIdList){
                key = genKey(companyId,mId);
                mpIdMap.put(mId,pageResult(key,start,count));
            }
        }
        return mpIdMap;
    }

    private List <Long> pageResult(String key, int start, int count) {
        if(start < 0){
            start = 0;
        }
        List<Long> mpIdList = new ArrayList<>();
        List<Long> allMpIdList = hotSaleMerchantProductMap.get(key);
        if(CollectionUtils.isEmpty(allMpIdList) || allMpIdList.size() <= start){
            return mpIdList;
        }

        int end = start + count - 1;
        int length = allMpIdList.size() -1;
        int max = Math.min(end,length);

        /*for(int i = start; i <= max; i++ ){
            mpIdList.add(allMpIdList.get(i));
        }*/

        int i = 0;
        for(Long id : allMpIdList){
            if(i >= start){
                mpIdList.add(id);
            }
            if(max <= i){
                break;
            }
            i++;
        }

        return mpIdList;
    }

    public synchronized void tryReload(String version) throws Exception {
        logger.info("HotSaleMerchantProductService tryReload  start .......");
        Map<String,List<Long>> tempMpIdMap = new ConcurrentHashMap<String,List<Long>>();

        Map<String,List<MerchantProductSale>> tempMpSaleMap = new ConcurrentHashMap<String,List<MerchantProductSale>>();

        long maxId = 0l;
        int pageSize = 500;
        boolean hasNext = true;
        List<MerchantProductSale> list = null;
        while (hasNext){
            list = hotSaleMerchantProductDao.queryHotSaleMerchantProductWithPage(maxId,pageSize,version);
            if(CollectionUtils.isEmpty(list) || list.size() < pageSize){
                hasNext = false;
            }
            if(CollectionUtils.isNotEmpty(list) ){
                for(MerchantProductSale sale : list){
                    maxId = sale.getId();
                    String key = "";
                    if(HotSaleType.COMPANY_HOT.getCode().equals(sale.getType())){
                        key = genKey(sale.getCompanyId().intValue(),null);
                    }else{
                        key = genKey(sale.getCompanyId().intValue(),sale.getMerchantId());
                    }
                    List<MerchantProductSale> mpList = tempMpSaleMap.get(key);
                    if(null == mpList){
                        mpList = new ArrayList<MerchantProductSale>();
                        tempMpSaleMap.put(key,mpList);
                    }
                    mpList.add(sale);
                }
            }
        }

        if(tempMpSaleMap.size() > 0){
            List<Long> mpIdList = null;
            for(Map.Entry<String,List<MerchantProductSale>> entry : tempMpSaleMap.entrySet()){
                String key = entry.getKey();
                List<MerchantProductSale> values = entry.getValue();

                Collections.sort(values, new Comparator<MerchantProductSale>() {
                    @Override
                    public int compare(MerchantProductSale o1, MerchantProductSale o2) {
                        return o2.getVolume4sale().compareTo(o1.getVolume4sale());
                    }
                });
                List<Long> tempList = new ArrayList<Long>();
                for(MerchantProductSale sale : values){
                    tempList.add(sale.getMerchantProductId());
                }
                mpIdList = new CopyOnWriteArrayList<Long>(tempList);
                tempMpIdMap.put(key,mpIdList);

            }
            hotSaleMerchantProductMap = tempMpIdMap;
        }
        logger.info("HotSaleMerchantProductService tryReload  end .......");

    }

    @Override
    public void tryReload() throws Exception {
        String version = hotSaleMerchantProductDao.queryLatestVersion();
        logger.info("LatestVersion : " + version);
        if(StringUtils.isNoneBlank(version)){
            tryReload(version);
        }
    }

    @Override
    public void removeByKey(int companyId,Long merchantId,Long merchantProductId) throws Exception{
        if(merchantProductId == null || merchantProductId == 0){
            return;
        }
        String key = genKey(companyId,merchantId);
        List<Long> mpIdList = hotSaleMerchantProductMap.get(key);
        if(CollectionUtils.isNotEmpty(mpIdList)){
            mpIdList.remove(merchantProductId);
        }

    }

    @Override
    public void removeByKey(int companyId, Long merchantId, List<Long> merchantProductIdList) throws Exception {
        if(CollectionUtils.isEmpty(merchantProductIdList)){
            return;
        }
        String key = genKey(companyId,merchantId);
        List<Long> mpIdList = hotSaleMerchantProductMap.get(key);
        if(CollectionUtils.isNotEmpty(mpIdList)){
            mpIdList.removeAll(merchantProductIdList);
        } else {
            logger.info("key has no result : " + key);
        }

    }

    public HotSaleMerchantProductDao getHotSaleMerchantProductDao() {
        return hotSaleMerchantProductDao;
    }

    public void setHotSaleMerchantProductDao(HotSaleMerchantProductDao hotSaleMerchantProductDao) {
        this.hotSaleMerchantProductDao = hotSaleMerchantProductDao;
    }

    private String genKey(int companyId,Long merchantId){

        StringBuilder key = new StringBuilder();
        key.append(companyId);
        key.append("_");
        if(merchantId == null){
            key.append(NULL_STR);
        }else{
            key.append(merchantId);
        }
        return key.toString();

    }
}
