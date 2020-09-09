package com.odianyun.search.whale.data.service;

import com.odianyun.search.whale.data.model.*;

import java.util.List;
import java.util.Map;

public interface PointsMallProductService {

	List<PointsMallProduct> getPointsMallProductsWithPage(long maxId, int pageSize, Integer companyId) throws Exception;

	List<PointsMallProduct> getPointsMallProductsByRefId(List<Long> mpIdList, Integer refType, int companyId) throws Exception;

	List<Long> getMpIdListByPointsMallProductId(List<Long> ids, int companyId) throws Exception;

	List<PointsMallProductPrice> getPointsMallProductPrice(List<Long> pointProductIdList, List<Long> pointsRuleIdList, List<Long> mpIdList, int companyId) throws Exception;

}
