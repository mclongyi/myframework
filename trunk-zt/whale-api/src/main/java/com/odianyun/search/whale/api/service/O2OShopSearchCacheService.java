package com.odianyun.search.whale.api.service;

import com.odianyun.search.whale.api.common.SearchException;
import com.odianyun.search.whale.api.model.o2o.O2OShopSearchRequest;
import com.odianyun.search.whale.api.model.o2o.O2OShopSearchResponse;
import com.odianyun.search.whale.api.model.o2o.O2OShopSearchService;
import com.odianyun.search.whale.api.model.req.BaseSearchRequest;
import com.odianyun.soa.InputDTO;
import com.odianyun.soa.OutputDTO;
import com.odianyun.soa.SoaUtil;
import org.apache.log4j.Logger;

public class O2OShopSearchCacheService implements O2OShopSearchService {
    static Logger logger = Logger.getLogger(O2OShopSearchCacheService.class);
    private O2OShopSearchService o2OShopSearchService;

    public O2OShopSearchCacheService(O2OShopSearchService o2OShopSearchService) {
        this.o2OShopSearchService = o2OShopSearchService;
    }


    @Override
    public O2OShopSearchResponse shopSearch(
            O2OShopSearchRequest o2oShopSearchRequest) throws SearchException {
        if (o2oShopSearchRequest.getMerchantId() == null) {
            throw new SearchException("merchantId is null");
        }
        return o2OShopSearchService.shopSearch(o2oShopSearchRequest);
    }

    @Override
    public OutputDTO<O2OShopSearchResponse> shopSearchStandard(InputDTO<O2OShopSearchRequest> inputDTO) throws SearchException {
//        logger.info("soa 调用入参 ," + inputDTO.getData());

        O2OShopSearchResponse response;
        try {
            response = shopSearch(inputDTO.getData());
        } catch (Exception e) {
            logger.error("soa fail {}," + inputDTO, e);
            return SoaUtil.resultError(e.getMessage());
        }
//        logger.info("soa 调用出参 ," + response);
        return SoaUtil.resultSucess(response);
    }

}
