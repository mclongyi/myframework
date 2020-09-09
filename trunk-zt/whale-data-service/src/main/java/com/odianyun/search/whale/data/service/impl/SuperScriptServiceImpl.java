package com.odianyun.search.whale.data.service.impl;

import com.odianyun.search.whale.data.dao.SuperScriptDao;
import com.odianyun.search.whale.data.model.MerProScript;
import com.odianyun.search.whale.data.model.SuperScript;
import com.odianyun.search.whale.data.service.AbstractCompanyDBService;
import com.odianyun.search.whale.data.service.SuperScriptService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * Created by admin on 2016/12/1.
 */
public class SuperScriptServiceImpl extends AbstractCompanyDBService implements SuperScriptService {

    Logger logger = Logger.getLogger(SuperScriptServiceImpl.class);

    private SuperScriptDao superScriptDao;

    private Map<Integer,SuperScriptContext> superScriptContexts = new HashMap<Integer,SuperScriptContext>();

    @Override
    protected void tryReload(int companyId) throws Exception {
        SuperScriptContext superScriptContext = new SuperScriptContext();
        loadSuperScript(superScriptContext,companyId);
        superScriptContexts.put(companyId,superScriptContext);
    }

    public void reload(List<Long> ids,int companyId){

    }

    private void loadSuperScript(SuperScriptContext superScriptContext,int companyId){
        try {
            List<SuperScript> superScripts = superScriptDao.queryAllSuperScripts(companyId);
            Map<Long,SuperScript> tmpMap = new HashMap<Long,SuperScript>();
            if(CollectionUtils.isNotEmpty(superScripts)){
                for(SuperScript s : superScripts){
                    tmpMap.put(s.getSuperscriptId(),s);
                }
            }
            if(tmpMap.size()>0){
                superScriptContext.merProScriptMap = tmpMap;
            }
        }catch (Exception e){
            //e.printStackTrace();
            logger.error(e.getMessage(),e);
        }
    }


    @Override
    public Map<Long,List<SuperScript>> queryMerPorScript(List<Long> mpIds, Integer companyId) throws Exception {
        Map<Long,List<SuperScript>> retMap = new HashMap<Long,List<SuperScript>>();
        List<MerProScript> merProScripts =  superScriptDao.queryMerScriptByMPIds(mpIds,companyId);
        SuperScriptContext context = superScriptContexts.get(companyId);
        if(context == null){
            return retMap;
        }
        Map<Long,SuperScript> superScriptCache =  context.merProScriptMap;
        if(CollectionUtils.isNotEmpty(merProScripts)){
            for(MerProScript mp:merProScripts){
                List<SuperScript> tmpSc = retMap.get(mp.getMpId());
                if(tmpSc == null){
                    tmpSc = new ArrayList<SuperScript>();
                }
                SuperScript superScript = superScriptCache.get(mp.getSuperscriptId());
                if(superScript!=null){
                    tmpSc.add(superScript);
                }
                retMap.put(mp.getMpId(),tmpSc);
            }
        }
        return retMap;
    }

    @Override
    public List<MerProScript> queryMerPorScriptById(Long mpId, Integer companyId) throws Exception {
        /*if(superScriptContexts.containsKey(companyId)){
            Map<Long,List<MerProScript>> merMap = superScriptContexts.get(companyId).merProScriptMap;
            if(merMap.containsKey(mpId)){
                return merMap.get(mpId);
            }
        }*/
        return new ArrayList<MerProScript>();
    }

    @Override
    public List<SuperScript> queryMerPorScriptByIds(List<Long> superScriptIds, int companyId) throws Exception {
        List<SuperScript> retList = new ArrayList<SuperScript>();
        SuperScriptContext context = superScriptContexts.get(companyId);
        if(context == null){
            return retList;
        }
        Map<Long,SuperScript> superScriptMap = context.merProScriptMap;
        for(Long id : superScriptIds){
            SuperScript superScript = superScriptMap.get(id);
            if(superScript!=null){
                retList.add(superScript);
            }
        }
        return retList;
    }


    public static class SuperScriptContext{
        Map<Long,SuperScript> merProScriptMap = new HashMap<Long,SuperScript>();//key 角标id value角标对象
    }

    public SuperScriptDao getSuperScriptDao() {
        return superScriptDao;
    }

    public void setSuperScriptDao(SuperScriptDao superScriptDao) {
        this.superScriptDao = superScriptDao;
    }

    public int getInterval(){
        return 3;
    }
}
