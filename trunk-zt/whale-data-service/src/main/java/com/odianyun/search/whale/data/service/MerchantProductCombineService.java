package com.odianyun.search.whale.data.service;

import com.odianyun.search.whale.data.model.CombineProduct;
import com.odianyun.search.whale.data.model.MerchantProduct;

import java.util.List;
import java.util.Map;

/**
 * Created by Botao on 2016/12/28.
 */
public interface MerchantProductCombineService {

    /**
     * 根据组合品id查找子商品
     * @param mpIds 组合品ids
     * @param companyId
     * @return List<MerchantProduct> 子商品集合（不包含组合品）
     * @throws Exception
     */
    public Map<Long,List<MerchantProduct>> querySubMerchantProducts(List<Long> mpIds, int companyId) throws Exception;
    /**
     * 根据商品id获取子品关联list
     * */
    List<CombineProduct> queryCombineProductsByMpids(List<Long> merchantProductIds, int companyId) throws Exception;
}
