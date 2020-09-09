package com.odianyun.search.whale.server;

import com.odianyun.search.whale.api.model.MerchantProduct;
import com.odianyun.search.whale.api.model.req.ReloadCacheRequest;
import com.odianyun.search.whale.api.model.req.SearchByCodeRequest;
import com.odianyun.search.whale.api.model.resp.SearchByCodeResponse;
import com.odianyun.search.whale.manager.SearchUpdateConsumer;
import com.odianyun.soa.InputDTO;
import com.odianyun.soa.SoaUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;

import com.odianyun.search.whale.api.common.SearchException;
import com.odianyun.search.whale.api.service.CacheService;
import com.odianyun.search.whale.data.common.DBManagerInitedEvent;
import com.odianyun.search.whale.data.manager.CompanyDBCacheManager;
import com.odianyun.search.whale.data.manager.UpdateConsumer;
import com.odianyun.search.whale.index.api.common.IndexConstants;

import java.util.Map;

public class CacheServer implements CacheService, ApplicationListener<DBManagerInitedEvent> {

    static Logger logger = Logger.getLogger(CacheServer.class);

    @Autowired
    SearchUpdateConsumer searchUpdateConsumer;

    public void startConsumer() throws SearchException {
        if (searchUpdateConsumer != null) {
            logger.info("start core Consumer=================================================");
            searchUpdateConsumer.startConsumerReload(IndexConstants.CACHE_TOPIC);
            //updateConsumer.startConsumerReload(IndexConstants.CACHE_TOPIC,IndexConstants.OMQ_NAMESPACE);

            logger.info("start core Consumer sucessfully=================================================");

        } else {
            logger.error("updateConsumer is null");
        }
    }

    @Override
    public void reloadCache(String name, int companyId) throws SearchException {
        CompanyDBCacheManager.instance.reload(name, companyId);
    }

    @Override
    public void reloadCacheStandard(InputDTO<ReloadCacheRequest> inputDTO) throws SearchException {
//        logger.info("soa 调用入参 ," + inputDTO.getData());
        try {
            ReloadCacheRequest request = inputDTO.getData();
            reloadCache(request.getName(), request.getCompanyId());
        } catch (Exception e) {
            logger.error("soa fail {}", e);
        }
//        logger.info("soa 调用出参 ");
    }

    @Override
    public void onApplicationEvent(DBManagerInitedEvent event) {
        startConsumer();
    }

}
