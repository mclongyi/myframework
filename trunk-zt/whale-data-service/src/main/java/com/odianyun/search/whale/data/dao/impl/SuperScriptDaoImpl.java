package com.odianyun.search.whale.data.dao.impl;

import com.odianyun.search.whale.data.common.ServiceConstants;
import com.odianyun.search.whale.data.dao.SuperScriptDao;
import com.odianyun.search.whale.data.model.MerProScript;
import com.odianyun.search.whale.data.model.SuperScript;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2016/12/1.
 */
public class SuperScriptDaoImpl extends SqlMapClientDaoSupport implements SuperScriptDao {
    @Override
    public List<SuperScript> querySuperScriptByIds(List<Long> ids, Integer companyId) throws Exception {
        Map<String,Object> param = new HashMap<String,Object>();
        param.put(ServiceConstants.IDS,ids);
        param.put(ServiceConstants.COMPANYID,companyId);
        try {
            List<SuperScript> ret =  getSqlMapClientTemplate().queryForList("querySuperScriptByIds",param);
            return  ret;

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
        /*return getSqlMapClientTemplate().queryForList("querySuperScriptByIds",param);*/
    }

    @Override
    public List<SuperScript> queryAllSuperScripts(Integer companyId) throws Exception {
        return getSqlMapClientTemplate().queryForList("queryAllSuperScripts",companyId);
    }

    @Override
    public List<MerProScript> queryAllMerScript(Integer companyId) throws Exception {
        return getSqlMapClientTemplate().queryForList("queryAllMerScript",companyId);
    }

    @Override
    public List<MerProScript> queryMerScriptByMPIds(List<Long> ids, int companyId) throws Exception {
        Map<String,Object> param = new HashMap<String,Object>();
        param.put(ServiceConstants.IDS,ids);
        param.put(ServiceConstants.COMPANYID,companyId);
        param.put("nowDate",new Date());
        return getSqlMapClientTemplate().queryForList("queryMerScriptByMPIds",param);
    }
}
