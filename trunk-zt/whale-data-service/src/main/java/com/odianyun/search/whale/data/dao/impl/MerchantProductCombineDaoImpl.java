package com.odianyun.search.whale.data.dao.impl;

import com.odianyun.search.whale.data.common.ServiceConstants;
import com.odianyun.search.whale.data.dao.MerchantProductCombineDao;
import com.odianyun.search.whale.data.model.CombineProduct;
import com.odianyun.search.whale.data.model.MerchantProduct;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Botao on 2016/12/27.
 */
public class MerchantProductCombineDaoImpl extends SqlMapClientDaoSupport implements MerchantProductCombineDao {

    @Override
    public List<MerchantProduct> querySubMerchantProducts(long mpId, int companyId) throws Exception{
        HashMap<String,Object> paramMap = new HashMap<>();
        paramMap.put(ServiceConstants.ID, mpId);
        paramMap.put(ServiceConstants.COMPANYID, companyId);
        return getSqlMapClientTemplate().queryForList("querySubMerchantProducts",paramMap);
    }

    @Override
    public List<CombineProduct> queryAllCombineProduct(int companyId) throws Exception {
        return getSqlMapClientTemplate().queryForList("queryAllCombineProduct",companyId);
    }

}
