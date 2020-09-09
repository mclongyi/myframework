package com.odianyun.search.whale.index.business.process;

import com.odianyun.search.whale.data.model.BusinessProduct;
import com.odianyun.search.whale.data.model.MerchantProductRelation;
import com.odianyun.search.whale.data.service.MerchantProductService;
import com.odianyun.search.whale.index.business.process.base.BaseMerchantProductRefIdProcess;
import com.odianyun.search.whale.index.common.ProcessorApplication;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2017/3/20.
 */
public class MerchantProductRefIdProcess extends BaseMerchantProductRefIdProcess {

    private static Logger logger = Logger.getLogger(MerchantProductRefIdProcess.class);
    private MerchantProductService merchantProductService;

    public MerchantProductRefIdProcess(){
        merchantProductService = (MerchantProductService) ProcessorApplication.getBean("merchantProductService");
    }

    @Override
    public void calcMerchantProductRefId(Map<Long, BusinessProduct> map, int companyId) {
        List<Long> mpIds = new ArrayList<>(map.keySet());
        try {
            Map<Long,MerchantProductRelation> prMap = merchantProductService.getStoreMerchantProductRelation(mpIds,companyId);
            if(prMap==null || prMap.size()==0){
                return;
            }
            for(Map.Entry<Long,BusinessProduct> entry:map.entrySet()){
                BusinessProduct businessProduct = entry.getValue();
                Long mpId = businessProduct.getId();
                MerchantProductRelation pr = prMap.get(mpId);
                if(pr!=null){
                    businessProduct.setRefId(pr.getRefId());
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
    }

    @Override
    public void calcMerchantIdRelation(Map<Long, BusinessProduct> map, int companyId) {
        List<Long> mpIds = new ArrayList<Long>(map.keySet());
        try {
            //对应父商家商品id map
            Map<Long,Long> PMPIdsMap = merchantProductService.queryPMPIdsBySMPIds(mpIds,companyId);
            List<Long> pIds = new ArrayList<>(PMPIdsMap.values());
            //父商家商品的商家map
            Map<Long,Long> PMerchantIdMap = new HashMap<Long,Long>();
            if(CollectionUtils.isNotEmpty(pIds)) {
                PMerchantIdMap = merchantProductService.getMerchantIdByMPId(pIds,companyId);
            }
            //父商家id map
            Map<Long,Long> parentMerchantIdMap = new HashMap<Long,Long>();
            if(PMerchantIdMap.size()>0) {
                for (Long mpId : mpIds) {
                    Long parentMerchantProductId = PMPIdsMap.get(mpId);
                    if (parentMerchantProductId != null) {
                        Long PmerchantId = PMerchantIdMap.get(parentMerchantProductId);
                        if (PmerchantId != null) {
                            parentMerchantIdMap.put(mpId, PmerchantId);
                        }
                    }
                }
            }

            Map<Long,List<Long>> SMPIdsMap = merchantProductService.querySMPIdsByPMPIds(mpIds,companyId);
            List<Long> subMerchantMPIds = new ArrayList<Long>();
            //子商家商品的商家map
            Map<Long,Long> subMerchantIdMap = new HashMap<Long,Long>();
            if(SMPIdsMap.size()>0){
                for(Map.Entry<Long,List<Long>> entry: SMPIdsMap.entrySet()){
                    subMerchantMPIds.addAll(entry.getValue());
                }
                if(CollectionUtils.isNotEmpty(subMerchantMPIds)){
                    subMerchantIdMap = merchantProductService.getMerchantIdByMPId(subMerchantMPIds,companyId);
                }
            }
            //该商品子商家id map
            Map<Long,List<Long>> subMerchantIdsMap = new HashMap<Long,List<Long>>();
            if(subMerchantIdMap.size()>0){
                for(Long mpId : mpIds){
                    List<Long> ids = SMPIdsMap.get(mpId);
                    if(ids!=null){
                        for (Long id : ids){
                            List<Long> list = subMerchantIdsMap.get(mpId);
                            if(list == null){
                                list = new ArrayList<Long>();
                                subMerchantIdsMap.put(mpId,list);
                            }
                            Long merId = subMerchantIdMap.get(id);
                            if(merId!=null && !list.contains(merId)){
                                list.add(merId);
                            }
                        }
                    }
                }
            }

            for(Map.Entry<Long, BusinessProduct> entry:map.entrySet()){
                BusinessProduct businessProduct = entry.getValue();
                Long mpId = businessProduct.getId();
                Long refId = PMPIdsMap.get(mpId);
                Long pMerId = parentMerchantIdMap.get(mpId);
                List<Long> subMerList = subMerchantIdsMap.get(mpId);
                if(refId!=null){
                    businessProduct.setRefId(refId);
                }
                if(pMerId!=null){
                    businessProduct.setParentMerchantId(pMerId);
                }
                if(subMerList!=null){
                    StringBuffer sb = new StringBuffer();
                    int i = 0;
                    for(Long sub:subMerList){
                        sb.append(sub);
                        if(i<subMerList.size()){
                            sb.append(" ");
                        }
                        i++;
                    }
                    businessProduct.setSubMerchantIds(sb.toString());
                }
            }

        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }

    }
}
