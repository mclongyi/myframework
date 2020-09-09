package com.odianyun.search.whale.data.service.impl;

import com.odianyun.search.whale.data.dao.MerchantProductDao;
import com.odianyun.search.whale.data.dao.PointsMallProductDao;
import com.odianyun.search.whale.data.model.*;
import com.odianyun.search.whale.data.service.MerchantProductService;
import com.odianyun.search.whale.data.service.PointsMallProductService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PointsMallProductServiceImpl implements PointsMallProductService {
	
	@Autowired
	PointsMallProductDao pointsMallProductDao;

	@Override
	public List<PointsMallProduct> getPointsMallProductsWithPage(long maxId, int pageSize, Integer companyId) throws Exception {
		return pointsMallProductDao.getPointsMallProductsWithPage(maxId,pageSize,companyId);
	}

	@Override
	public List<PointsMallProduct> getPointsMallProductsByRefId(List<Long> mpIdList,Integer refType,int companyId) throws Exception {
		return pointsMallProductDao.getPointsMallProductsByRefId(mpIdList,refType,companyId);
	}

	@Override
	public List<Long> getMpIdListByPointsMallProductId(List<Long> ids, int companyId) throws Exception {
		return pointsMallProductDao.getMpIdListByPointsMallProductId(ids,companyId);
	}

	@Override
	public List<PointsMallProductPrice> getPointsMallProductPrice(List<Long> pointProductIdList, List<Long> pointsRuleIdList, List<Long> mpIdList, int companyId) throws Exception {
		return pointsMallProductDao.getPointsMallProductPrice(pointProductIdList,pointsRuleIdList,mpIdList,companyId);
	}


}
