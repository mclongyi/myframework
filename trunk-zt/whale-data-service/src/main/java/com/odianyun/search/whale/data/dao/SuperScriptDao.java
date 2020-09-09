package com.odianyun.search.whale.data.dao;

import com.odianyun.search.whale.data.model.MerProScript;
import com.odianyun.search.whale.data.model.SuperScript;

import java.util.List;

/**
 * Created by jzz on 2016/12/1.
 * 商品角标
 */
public interface SuperScriptDao {
    //根据id获得角标对象
    public List<SuperScript> querySuperScriptByIds(List<Long> ids,Integer companyId) throws Exception;

    /**
     * 查询所有角标对象
     * */
    public List<SuperScript> queryAllSuperScripts(Integer companyId) throws Exception;
    /**
     * 查询商品和角标关联表
     * */
    public List<MerProScript> queryAllMerScript(Integer companyId) throws Exception;

    List<MerProScript> queryMerScriptByMPIds(List<Long> ids, int companyId) throws Exception;
}
