package com.odianyun.search.whale.data.service;

import com.odianyun.search.whale.data.model.PointsMallProduct;

import java.util.List;

/**
 * Created by fishcus on 17/9/20.
 */
public interface CurrentPointsMallProductService {

    List<PointsMallProduct> getPointsMallProductsByRefId(List<Long> mpIdList, int companyId) throws Exception;

}
