package com.odianyun.search.whale.index.convert;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.odianyun.search.whale.data.dao.ProductDao;
import com.odianyun.search.whale.index.common.ProcessorApplication;

public class CateTreeNodeConverter extends ProductConverter{
	
	ProductDao productDao;
	
	public CateTreeNodeConverter(){
		super();
		productDao = (ProductDao) ProcessorApplication.getBean("productDao");
	}
	@Override
	public List<Long> convert(List<Long> ids, int companyId) throws Exception {
		List<Long> mpIdList = new ArrayList<Long>(); 
		
		if(CollectionUtils.isNotEmpty(ids)){
			List<Long> rightCategoryTreeNodeIds = productDao.getRightCategoryTreeNodeIds(ids,companyId);
			if(CollectionUtils.isNotEmpty(rightCategoryTreeNodeIds)){
				List<Long> productIdList = productDao.getProductsByCategoryTreeNodeIds(rightCategoryTreeNodeIds,companyId);
				if(CollectionUtils.isNotEmpty(productIdList)){
					mpIdList = super.convert(productIdList, companyId);
				}
			}
			
		}
		
		return mpIdList;
	}

}
