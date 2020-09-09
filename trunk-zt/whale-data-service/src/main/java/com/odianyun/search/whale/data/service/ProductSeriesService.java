package com.odianyun.search.whale.data.service;


import com.odianyun.search.whale.data.model.ProductSeriesAttribute;

import java.util.List;
import java.util.Map;

/**
 * Created by fishcus on 16/11/17.
 */
public interface ProductSeriesService {

    Map<Long,List<ProductSeriesAttribute>> getProductAttrValues(List<Long> virtualMerchantProductIds, int companyId) throws Exception;

    List<Long> getSeriesMerchantProductIds(List<Long> virtualMerchantProductIds, int companyId)throws Exception;

}
