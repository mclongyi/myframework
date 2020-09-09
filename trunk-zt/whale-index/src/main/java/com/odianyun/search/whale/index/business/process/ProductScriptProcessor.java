package com.odianyun.search.whale.index.business.process;

import com.odianyun.search.whale.data.model.BusinessProduct;
import com.odianyun.search.whale.data.model.MerProScript;
import com.odianyun.search.whale.data.model.SuperScript;
import com.odianyun.search.whale.data.service.SuperScriptService;
import com.odianyun.search.whale.index.business.process.base.BaseProductScriptProcessor;
import com.odianyun.search.whale.index.common.ProcessorApplication;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2016/12/5.
 */
public class ProductScriptProcessor extends BaseProductScriptProcessor {

    SuperScriptService superScriptService;

    public ProductScriptProcessor(){
        superScriptService = (SuperScriptService) ProcessorApplication.getBean("superScriptService");
    }
    @Override
    public void calcProductSuperScript(Map<Long, BusinessProduct> map, int companyId) throws Exception {
        List<Long> merchantProductIds = new ArrayList<Long>(map.keySet());
        Map<Long,List<SuperScript>> merProScripts  = superScriptService.queryMerPorScript(merchantProductIds,companyId);
        if(merProScripts.size()==0){
            return;
        }
        for(Map.Entry<Long,BusinessProduct> entry : map.entrySet()){
            Long merchantProductId = entry.getKey();
            BusinessProduct businessProduct = entry.getValue();
            List<SuperScript> suList = merProScripts.get(merchantProductId);
            if(CollectionUtils.isNotEmpty(suList)){
                businessProduct.setScriptIds(buildScriptIds(suList));
            }
        }
    }

    private String buildScriptIds(List<SuperScript> suList) {
        StringBuffer scriptIds = new StringBuffer();
        int i = 0;
        for(SuperScript su : suList){
            scriptIds.append(su.getSuperscriptId());
            i++;
            if(i<suList.size()){
                scriptIds.append(" ");
            }
        }
        return scriptIds.toString();
    }

}
