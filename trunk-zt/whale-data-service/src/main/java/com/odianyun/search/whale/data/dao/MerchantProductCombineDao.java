package com.odianyun.search.whale.data.dao;

import com.odianyun.search.whale.data.model.CombineProduct;
import com.odianyun.search.whale.data.model.MerchantProduct;

import java.util.List;

/**
 * Created by Botao on 2016/12/27.
 */
public interface MerchantProductCombineDao {

    /**
     * 根据组合品id查找子商品
     * @param mpId 组合品id
     * @param companyId
     * @return List<MerchantProduct> 子商品集合（不包含组合品）
     * @throws Exception
     */
    public List<MerchantProduct> querySubMerchantProducts(long mpId, int companyId) throws Exception;

    /**
     * 获取所有组合商品关联表
     * */
    List<CombineProduct> queryAllCombineProduct(int companyId) throws Exception;
}
