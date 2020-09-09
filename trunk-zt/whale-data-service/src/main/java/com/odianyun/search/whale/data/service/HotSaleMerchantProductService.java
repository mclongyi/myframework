package com.odianyun.search.whale.data.service;

import com.odianyun.search.whale.data.model.MerchantProductSale;

import java.util.List;
import java.util.Map;

/**
 * Created by fishcus on 16/11/23.
 */
public interface HotSaleMerchantProductService {

    void batchSave(List<MerchantProductSale> merchantProductSaleList,Integer type) throws Exception;

    List<Long> getHotSaleMerchantProducts(List<Long> merchantIdList, int companyId,int start,int count) throws Exception;

    Map<Long,List<Long>> getHotSaleMerchantProductMap(List<Long> merchantIdList, int companyId, int start, int count) throws Exception;

    void tryReload(String version) throws Exception;

    void tryReload() throws Exception;

    void removeByKey(int companyId,Long merchantId,Long merchantProductId) throws Exception;

    void removeByKey(int companyId,Long merchantId,List<Long> merchantProductIdList) throws Exception;

}
