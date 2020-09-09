package com.odianyun.search.whale.data.dao;

import com.odianyun.search.whale.data.model.MerchantProductSale;

import java.util.List;

/**
 * Created by fishcus on 16/11/24.
 */
public interface HotSaleMerchantProductDao {

    void batchSave(List<MerchantProductSale> merchantProductSaleList, Integer type) throws Exception;

    List<MerchantProductSale> queryHotSaleMerchantProductWithPage(long maxId, int pageSize,String version) throws Exception;

    String queryLatestVersion() throws Exception;
}
