package com.odianyun.search.whale.data.dao.impl;

import com.odianyun.search.whale.data.common.ResultConvertor;
import com.odianyun.search.whale.data.common.ServiceConstants;
import com.odianyun.search.whale.data.dao.RateDao;
import com.odianyun.search.whale.data.model.MerchantProductVolume4Sale;
import com.odianyun.search.whale.data.model.Rate;
import com.odianyun.search.whale.data.saas.model.DBType;
import com.odianyun.search.whale.data.saas.service.BaseDaoService;
import com.odianyun.search.whale.data.saas.service.CompanySqlService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by juzhongzheng on 2016-08-22.
 */
public class RateDaoImpl extends SqlMapClientDaoSupport implements RateDao {
    /*@Autowired
    BaseDaoService baseDaoService;

    @Autowired
    CompanySqlService companySqlService;*/

    @Override
    public List<Rate> queryMerchantRateByIds(List<Long> merchantIds, Integer companyId) throws Exception {
       /* String sql = companySqlService.getSql(companyId, "queryRate");
        HashMap<String,Object> paramMap = new HashMap<>();
        paramMap.put(ServiceConstants.COMPANYID, companyId);
        paramMap.put(ServiceConstants.IDS, merchantIds);
        List<Map> list = baseDaoService.getBaseDao(companyId, DBType.front).queryForList(sql,paramMap);
        List<Rate> result = new ArrayList<Rate>();
        if(CollectionUtils.isNotEmpty(list)){
            result = ResultConvertor.convertFromMap(list, Rate.class);
        }
        return result;*/
    	HashMap<String,Object> paramMap = new HashMap<>();
		paramMap.put(ServiceConstants.IDS, merchantIds);
		paramMap.put(ServiceConstants.COMPANYID, companyId);
		return getSqlMapClientTemplate().queryForList("queryRate",paramMap);
    }
}
