package com.odianyun.search.whale.data.dao;


import com.odianyun.search.whale.data.model.ProductSeriesAttribute;

import java.util.List;

public interface ProductSeriesDao {

	List<ProductSeriesAttribute> queryProductSeriesAttribute(List<Long> virtualMerchantProductIds, int companyId) throws Exception;

	List<Long> querySeriesMerchantProductIds(List<Long> virtualMerchantProductIds, int companyId) throws Exception;
}
