package com.odianyun.search.whale.index.convert;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.odianyun.search.whale.data.dao.ProductDao;
import com.odianyun.search.whale.index.common.ProcessorApplication;

public class BrandConverter extends ProductConverter {
	
	ProductDao productDao;
	
	public BrandConverter(){
		super();
		productDao = (ProductDao) ProcessorApplication.getBean("productDao");
	}

	@Override
	public List<Long> convert(List<Long> ids, int companyId) throws Exception {
		List<Long> mpIdList = new ArrayList<Long>(); 
		if(CollectionUtils.isNotEmpty(ids)){
			List<Long> productIdList = productDao.getProductsByBrandIds(ids,companyId);
			if(CollectionUtils.isNotEmpty(productIdList)){
				mpIdList = super.convert(productIdList, companyId);
			}
		}
		
		return mpIdList;
	}

}
