package com.odianyun.search.whale.data.dao;

import com.odianyun.search.whale.data.model.PointsMallProduct;
import com.odianyun.search.whale.data.model.PointsMallProductPrice;

import java.util.List;

public interface CurrentPointsMallProductDao {

	List<PointsMallProduct> getPointsMallProductsByRefId(List<Long> mpIdList, int companyId) throws Exception;

}
