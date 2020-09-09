package com.odianyun.search.whale.data.service.impl;

import com.odianyun.search.whale.data.dao.CurrentPointsMallProductDao;
import com.odianyun.search.whale.data.dao.PointsMallProductDao;
import com.odianyun.search.whale.data.model.PointsMallProduct;
import com.odianyun.search.whale.data.model.PointsMallProductPrice;
import com.odianyun.search.whale.data.service.CurrentPointsMallProductService;
import com.odianyun.search.whale.data.service.PointsMallProductService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class CurrentPointsMallProductServiceImpl implements CurrentPointsMallProductService {
	
	@Autowired
	CurrentPointsMallProductDao pointsMallProductDao;

	@Override
	public List<PointsMallProduct> getPointsMallProductsByRefId(List<Long> mpIdList,int companyId) throws Exception {
		return pointsMallProductDao.getPointsMallProductsByRefId(mpIdList,companyId);
	}

}
