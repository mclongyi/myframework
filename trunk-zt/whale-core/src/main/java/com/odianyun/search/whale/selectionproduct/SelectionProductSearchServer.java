package com.odianyun.search.whale.selectionproduct;

import com.odianyun.search.whale.api.common.SearchException;
import com.odianyun.search.whale.api.model.ManagementType;
import com.odianyun.search.whale.api.model.selectionproduct.*;
import com.odianyun.search.whale.cache.PromotionSelectionCacheImpl;
import com.odianyun.search.whale.cache.SelectionOCacheImpl;
import com.odianyun.search.whale.common.IndexNameManager;
import com.odianyun.search.whale.data.service.ConfigService;
import com.odianyun.search.whale.es.api.ESService;
import com.odianyun.search.whale.es.request.ESSearchRequest;
import com.odianyun.soa.InputDTO;
import com.odianyun.soa.OutputDTO;
import com.odianyun.soa.SoaUtil;
import org.apache.log4j.Logger;
import org.elasticsearch.action.search.SearchResponse;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class SelectionProductSearchServer implements SelectionProductSearchService {

    static Logger logger = Logger.getLogger(SelectionProductSearchServer.class);

    @Autowired
    SelectionProductSearchResponseHandler selectionProductSearchResponseHandler;
    @Autowired
    PromotionProductSearchResponseHandler promotionProductSearchResponseHandler;
    @Autowired
    ConfigService configService;

    @Override
    public SelectionProductSearchResponse selectionSearch(SelectionProductSearchRequest selectionProductSearchRequest)
            throws SearchException {
        SelectionProductSearchResponse selectionProductSearchResponse = new SelectionProductSearchResponse();

        try {
            ESSearchRequest esSearchRequest = SelectionProductSearchRequestHandler.handle(selectionProductSearchRequest,
                    IndexNameManager.getIndexName());
            SearchResponse searchResponse = ESService.search(esSearchRequest);
            selectionProductSearchResponse = selectionProductSearchResponseHandler.handle(searchResponse);
        } catch (SearchException e) {
            logger.error(e.getMessage(), e);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return selectionProductSearchResponse;
    }

    @Override
    public PromotionProductSearchResponse promotionSelectionSearch(
            PromotionProductSearchRequest promotionProductSearchRequest) throws SearchException {

        if (validateRequest(promotionProductSearchRequest)) {
            return new PromotionProductSearchResponse();
        }

        PromotionProductSearchResponse promotionProductSearchResponse = PromotionSelectionCacheImpl.instance.get(promotionProductSearchRequest);
        if (promotionProductSearchResponse != null) {
            return promotionProductSearchResponse;
        }
        promotionProductSearchResponse = new PromotionProductSearchResponse();

        try {
            List<Integer> excludeTypes = promotionProductSearchRequest.getExcludeTypes();
            boolean isHiddenServiceProduct = configService.getBool("is_hidden_service_product", false, promotionProductSearchRequest.getCompanyId());
            if (isHiddenServiceProduct) {
                if (excludeTypes == null) {
                    excludeTypes = new ArrayList<>();
                }
                excludeTypes.add(6);
                promotionProductSearchRequest.setExcludeTypes(excludeTypes);
            }

            ESSearchRequest esSearchRequest = PromotionProductSearchRequestHandler.handle(promotionProductSearchRequest,
                    IndexNameManager.getIndexName());
            SearchResponse searchResponse = ESService.search(esSearchRequest);
            promotionProductSearchResponse = promotionProductSearchResponseHandler.handle(searchResponse);
            PromotionSelectionCacheImpl.instance.put(promotionProductSearchRequest, promotionProductSearchResponse);
        } catch (SearchException e) {
            logger.error(e.getMessage(), e);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return promotionProductSearchResponse;
    }

    private boolean validateRequest(PromotionProductSearchRequest promotionProductSearchRequest) {
        if (promotionProductSearchRequest == null) {
            return true;
        }
        if (promotionProductSearchRequest.getCompanyId() == null || promotionProductSearchRequest.getCompanyId() == 0) {
            return true;
        }
        if (promotionProductSearchRequest.getTypeOfProductFilter() == null) {
            return true;
        }
        return false;
    }

    @Override
    public PromotionProductSearchResponse promotionProductSelectionSearch(PromotionProductSearchRequest promotionProductSearchRequest) throws SearchException {
        promotionProductSearchRequest.setManagementState(ManagementType.VERIFIED);
        return promotionSelectionSearch(promotionProductSearchRequest);
    }

    @Override
    public SelectionMerchantProductSearchResponse selectionSearch2(
            SelectionProductSearchRequest selectionProductSearchRequest) throws SearchException {
        SelectionMerchantProductSearchResponse selectionProductSearchResponse = SelectionOCacheImpl.instance.get(selectionProductSearchRequest);
        if (selectionProductSearchResponse != null) {
            return selectionProductSearchResponse;
        }
        selectionProductSearchResponse = new SelectionMerchantProductSearchResponse();

        try {
            List<Integer> excludeTypes = selectionProductSearchRequest.getExcludeTypes();
            boolean isHiddenServiceProduct = configService.getBool("is_hidden_service_product", false, selectionProductSearchRequest.getCompanyId());
            if (isHiddenServiceProduct) {
                if (excludeTypes == null) {
                    excludeTypes = new ArrayList<>();
                }
                excludeTypes.add(6);
                selectionProductSearchRequest.setExcludeTypes(excludeTypes);
            }
            ESSearchRequest esSearchRequest = SelectionProductSearchRequestHandler.handle2(selectionProductSearchRequest,
                    IndexNameManager.getIndexName());
            SearchResponse searchResponse = ESService.search(esSearchRequest);
            selectionProductSearchResponse = selectionProductSearchResponseHandler.handle2(searchResponse);
            SelectionOCacheImpl.instance.put(selectionProductSearchRequest, selectionProductSearchResponse);
        } catch (SearchException e) {
            logger.error(e.getMessage(), e);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return selectionProductSearchResponse;
    }

    //------------SOA 包装--------------

    @Override
    public OutputDTO<SelectionProductSearchResponse> selectionSearchStandard(InputDTO<SelectionProductSearchRequest> inputDTO) throws SearchException {
//        logger.info("soa 调用入参 ,"+inputDTO.getData());

        SelectionProductSearchResponse response;
        try {
            response = selectionSearch(inputDTO.getData());
        } catch (Exception e) {
            logger.error("soa fail {}"+inputDTO, e);
            return SoaUtil.resultError(e.getMessage());
        }
//        logger.info("soa 调用出参 ,"+response);
        return SoaUtil.resultSucess(response);
    }

    @Override
    public OutputDTO<SelectionMerchantProductSearchResponse> selectionSearch2Standard(InputDTO<SelectionProductSearchRequest> inputDTO) throws SearchException {
        SelectionMerchantProductSearchResponse response;
        try {
            response = selectionSearch2(inputDTO.getData());
        } catch (Exception e) {
            logger.error("soa fail {}"+inputDTO, e);
            return SoaUtil.resultError(e.getMessage());
        }
        return SoaUtil.resultSucess(response);
    }

    @Override
    public OutputDTO<PromotionProductSearchResponse> promotionSelectionSearchStandard(InputDTO<PromotionProductSearchRequest> inputDTO) throws SearchException {
//        logger.info("soa 调用入参 ,"+inputDTO.getData());

        PromotionProductSearchResponse response;
        try {
            response = promotionSelectionSearch(inputDTO.getData());
        } catch (Exception e) {
            logger.error("soa fail {}"+inputDTO, e);
            return SoaUtil.resultError(e.getMessage());
        }
//        logger.info("soa 调用出参 ,"+response);
        return SoaUtil.resultSucess(response);
    }

    @Override
    public OutputDTO<PromotionProductSearchResponse> promotionProductSelectionSearchStandard(InputDTO<PromotionProductSearchRequest> inputDTO) throws SearchException {
//        logger.info("soa 调用入参 ,"+inputDTO.getData());

        PromotionProductSearchResponse response;
        try {
            response = promotionProductSelectionSearch(inputDTO.getData());
        } catch (Exception e) {
            logger.error("soa fail {}"+inputDTO, e);
            return SoaUtil.resultError(e.getMessage());
        }
//        logger.info("soa 调用出参 ,"+response);
        return SoaUtil.resultSucess(response);
    }

}
