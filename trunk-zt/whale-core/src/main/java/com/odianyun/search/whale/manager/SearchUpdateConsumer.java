package com.odianyun.search.whale.manager;

import com.odianyun.search.whale.data.manager.UpdateConsumer;
import com.odianyun.search.whale.data.model.MerchantProduct;
import com.odianyun.search.whale.data.model.MerchantProductSimple;
import com.odianyun.search.whale.data.service.HotSaleMerchantProductService;
import com.odianyun.search.whale.index.api.common.UpdateMessage;
import com.odianyun.search.whale.index.api.common.UpdateType;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fishcus on 16/11/24.
 */
public class SearchUpdateConsumer extends UpdateConsumer {

    static Logger logger = Logger.getLogger(SearchUpdateConsumer.class);

    public static final String SEARCH_CONSUMER_SUFFIX = "search";

    @Autowired
    HotSaleMerchantProductService hotSaleMerchantProductService;

    @Override
    public void startConsumerReload(String topic) {
        String consumerId = genConsumerIdWithSuffix(SEARCH_CONSUMER_SUFFIX);
        startConsumerReload(topic,consumerId);
    }

    @Override
    protected void updateByMessage(UpdateMessage updateMessage) {
        super.updateByMessage(updateMessage);
        UpdateType updateType = updateMessage.getUpdateType();
        switch (updateType){
            case FULL_INDEX:
                updateByFullIndex(updateMessage);
                break;
            case merchant_product_id:
                updateHotSaleMerchantProduct(updateMessage);
                break;
        }
    }

    private void updateHotSaleMerchantProduct(UpdateMessage updateMessage) {
        List<Long> mpIds = updateMessage.getIds();
        if(CollectionUtils.isEmpty(mpIds)){
            return;
        }
        try {
            List<MerchantProductSimple> mpList = merchantProductDao.queryMerchantProductsAllSimple(mpIds,updateMessage.getCompanyId());
            if(CollectionUtils.isEmpty(mpList)){
                return;
            }
            Map<Long,List<Long>> merchantProductMap = new HashMap<>();
            for(MerchantProductSimple mp : mpList){
                if(mp.getManagementState() == 1 && mp.getStatus() == 2){
                    continue;
                }
                Long merchantId = mp.getMerchant_id();
                List<Long> mpIdList = merchantProductMap.get(merchantId);
                if(mpIdList == null){
                    mpIdList = new ArrayList<>();
                    merchantProductMap.put(merchantId,mpIdList);
                }
                mpIdList.add(mp.getId());

            }
            if(merchantProductMap.size() > 0){
                for(Map.Entry<Long,List<Long>> entry : merchantProductMap.entrySet()){
                    Long merchantId = entry.getKey();
                    List<Long> mpIdList = entry.getValue();
                    hotSaleMerchantProductService.removeByKey(updateMessage.getCompanyId(),merchantId,mpIdList);
                    hotSaleMerchantProductService.removeByKey(updateMessage.getCompanyId(),null,mpIdList);
                }
            }


        } catch (Exception e) {
            logger.error("getMerchantProductsAll error : "+ updateMessage + e.getMessage(),e);
        }
    }

    private void updateByFullIndex(UpdateMessage updateMessage) {
        String version = updateMessage.getVersion();
        logger.info("consume FullIndex version : " + version);
        try {
            IKSegmentManager.reload();
            hotSaleMerchantProductService.tryReload(version);
        } catch (Exception e) {
            logger.error("hotSaleMerchantProductService reload error : " + e.getMessage(),e);
        }
    }
}
