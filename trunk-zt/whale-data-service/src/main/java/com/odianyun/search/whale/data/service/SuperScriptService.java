package com.odianyun.search.whale.data.service;

import com.odianyun.search.whale.data.model.MerProScript;
import com.odianyun.search.whale.data.model.SuperScript;

import java.util.List;
import java.util.Map;

/**
 * Created by jzz on 2016/12/1.
 */
public interface SuperScriptService {
    public Map<Long,List<SuperScript>> queryMerPorScript(List<Long> mpIds, Integer companyId) throws Exception;
    public List<MerProScript> queryMerPorScriptById(Long mpId,Integer companyId) throws Exception;

    List<SuperScript> queryMerPorScriptByIds(List<Long> superScriptIds, int companyId) throws Exception;
    //public Map<Long,List<SuperScript>> queryMerPorScriptMap(List<Long> mpIds, Integer companyId) throws Exception;
}
