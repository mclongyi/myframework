package com.odianyun.search.whale.data.dao.impl;

import com.odianyun.search.whale.data.common.ServiceConstants;
import com.odianyun.search.whale.data.dao.ProductSeriesDao;
import com.odianyun.search.whale.data.model.ProductSeriesAttribute;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import java.util.HashMap;
import java.util.List;

/**
 * Created by fishcus on 16/11/17.
 */
public class ProductSeriesDaoImpl extends SqlMapClientDaoSupport implements ProductSeriesDao {

    @Override
    public List<ProductSeriesAttribute> queryProductSeriesAttribute(List<Long> virtualMerchantProductIds, int companyId) throws Exception {
        HashMap<String,Object> paramMap = new HashMap<>();
        paramMap.put(ServiceConstants.IDS, virtualMerchantProductIds);
        paramMap.put(ServiceConstants.COMPANYID, companyId);
        return getSqlMapClientTemplate().queryForList("queryProductSeriesAttributeList",paramMap);

    }

    @Override
    public List<Long> querySeriesMerchantProductIds(List<Long> virtualMerchantProductIds, int companyId) throws Exception {
        HashMap<String,Object> paramMap = new HashMap<>();
        paramMap.put(ServiceConstants.IDS, virtualMerchantProductIds);
        paramMap.put(ServiceConstants.COMPANYID, companyId);
        return getSqlMapClientTemplate().queryForList("querySeriesMerchantProductIds",paramMap);

    }
}
