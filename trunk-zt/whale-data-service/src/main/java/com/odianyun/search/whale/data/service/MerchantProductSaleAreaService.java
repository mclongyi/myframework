package com.odianyun.search.whale.data.service;

import com.odianyun.search.whale.data.model.SaleAreasCover;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by admin on 2016/12/7.
 */
public interface MerchantProductSaleAreaService {

    public Map<Long,Set<SaleAreasCover>> queryMerProSaleAreaCoverByMpIds(List<Long> mpIds, Integer companyId) throws Exception;

    public Set<SaleAreasCover> queryDefaultSaleCodeByMerchantId(Long merchantId,Integer companyId) throws Exception;

    public Map<Long,Set<SaleAreasCover>> queryDefaultSaleCodeByMerchantIds(Set<Long> merchantIds, Integer companyId) throws Exception;

    public Map<Long,List<Long>> querySupplierMerchantProductRelByMpIds(List<Long> mpIds, int companyId) throws Exception;

    public Set<SaleAreasCover> queryMerProSaleAreaByMerchantIds(List<Long> merchantIdList, int companyId) throws Exception;

    public Set<SaleAreasCover> queryMerchantCategorySaleAreasByMerchantCategory(List<Long> supplierIdList, Long categoryId, int companyId) throws Exception;
}
