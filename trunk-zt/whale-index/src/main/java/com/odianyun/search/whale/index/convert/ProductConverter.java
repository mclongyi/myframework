package com.odianyun.search.whale.index.convert;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.odianyun.search.whale.data.dao.MerchantProductDao;
import com.odianyun.search.whale.index.api.common.IndexConstants;
import com.odianyun.search.whale.index.common.ProcessorApplication;

public class ProductConverter extends SeriesConverter {

	MerchantProductDao merchantProductDao;

	public ProductConverter(){
		merchantProductDao = (MerchantProductDao) ProcessorApplication.getBean("merchantProductDao");
	}
	@Override
	public List<Long> convert(List<Long> ids, int companyId) throws Exception {
		List<Long> mpIdList = new ArrayList<Long>();
		if (CollectionUtils.isNotEmpty(ids)) {
			mpIdList = merchantProductDao.queryProductMerchantIdsByProductIds(ids,companyId);
			if(CollectionUtils.isNotEmpty(mpIdList)){
				mpIdList = super.convert(mpIdList,companyId);
			}
		}
		return mpIdList;
	}

}
