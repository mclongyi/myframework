package com.odianyun.search.whale.data.dao;

import com.odianyun.search.whale.data.model.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by admin on 2016/12/7.
 */
public interface MerchantProductSaleAreaDao {
    public List<MerchantProductSaleArea> queryAllSaleAreaIds(Integer companyId) throws Exception;
    public List<SaleAreasCover> querySaleAreaByAreaIds(List<Long> ids,Integer companyId) throws Exception;
    public List<SaleAreasCover> queryAllSaleArea(Integer companyId) throws Exception;

    List<MerchantProductSaleArea> queryAllSaleAreaIdsByMPIds(List<Long> mpIds, Integer companyId) throws Exception;

    Map<Long,Long> queryAllDefaultSaleAreaIds(int companyId) throws Exception;

    Map<Long,Long> queryAllDefaultSaleAreaIdsByMerchantIds(Set<Long> merchantIds, Integer companyId) throws Exception;

    List<SupplierMerchantProductRel> querySupplierMerchantProductRelByMpIds(List<Long> mpIds, int companyId) throws Exception;

    List<Long> querySaleAreaIdsByMerchantIds(List<Long> merchantIdList, int companyId) throws Exception;

    List<Long> querySaleAreaIdsByMerchantCategory(List<Long> supplierIdList, Long categoryId, int companyId) throws Exception;
}
