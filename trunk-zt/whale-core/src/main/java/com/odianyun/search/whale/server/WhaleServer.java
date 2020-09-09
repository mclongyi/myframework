package com.odianyun.search.whale.server;

import com.odianyun.search.whale.api.common.SearchException;
import com.odianyun.search.whale.api.model.MerchantProduct;
import com.odianyun.search.whale.api.model.SearchResponse;
import com.odianyun.search.whale.api.model.req.*;
import com.odianyun.search.whale.api.model.resp.*;
import com.odianyun.search.whale.api.model.series.SeriesRequest;
import com.odianyun.search.whale.api.model.series.SeriesResponse;
import com.odianyun.search.whale.api.service.SearchService;
import com.odianyun.search.whale.cache.*;
import com.odianyun.search.whale.index.api.common.IndexConstants;
import com.odianyun.search.whale.tracker.SearchTracker;
import com.odianyun.search.whale.tracker.TrackContext;
import com.odianyun.soa.InputDTO;
import com.odianyun.soa.OutputDTO;
import com.odianyun.soa.SoaUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;


public class WhaleServer implements SearchService {

    static Logger logger = Logger.getLogger(WhaleServer.class);


    @Autowired
    SearchHandler searchHandler;

    @Autowired
    ShopSearchHandler shopSearchHandler;

    @Autowired
    SearchByIdHandler searchByIdHandler;

    @Autowired
    SeriesProductAttrHandler seriesProductAttrHandler;

    @Autowired
    PromotionSearchHandler promotionSearchHandler;

    @Autowired
    PromotionTypeSearchHandler promotionTypeSearchHandler;

    @Autowired
    HotSearchHandler hotSearchHandler;

    private static int batchMaxSize = 200;

    @Override
    public SearchResponse search(SearchRequest searchRequest) throws SearchException {
        //先从缓存中获取
        SearchResponse searchResponse = SearchCacheImpl.instance.get(searchRequest);
        if (searchResponse != null) {
            return searchResponse;
        }
        searchResponse = searchHandler.handle(searchRequest);//调用搜索并获得返回
        SearchTracker.process(new TrackContext(searchRequest, searchResponse));//构造track上下文对象并交给监测模块处理
        SearchCacheImpl.instance.put(searchRequest, searchResponse);
        return searchResponse;
    }

    @Override
    public HotSearchResponse hotSearch(HotSearchRequest searchRequest) throws SearchException {
        return hotSearchHandler.handle(searchRequest);
    }

    @Override
    public SearchResponse searchProducts(SearchRequest searchRequest) throws SearchException {
        searchRequest.setAggregation(false);
        searchRequest.setZeroResponseHandler(false);
        searchRequest.setRecommendWordsHandler(false);
        return search(searchRequest);
    }

    @Override
    public SearchResponse shopSearch(ShopSearchRequest shopRequest) throws SearchException {
        SearchResponse searchResponse = shopSearchHandler.handle(shopRequest);
        SearchTracker.process(new TrackContext(shopRequest, searchResponse));
        return searchResponse;
    }

    @Override
    public Map<Long, MerchantProduct> searchById(List<Long> mpIds, Integer companyId)
            throws SearchException {
        SearchByIdRequest searchByIdRequest = new SearchByIdRequest(mpIds, companyId);
        return searchByIdNew(searchByIdRequest);
    }

    @Override
    public Map<Long, MerchantProduct> searchByIdNew(SearchByIdRequest searchByIdRequest) {

//        logger.info("调用searchByIdNew...start,"+searchByIdRequest);
        List<Long> mpIds = searchByIdRequest.getMpIds();
        Integer companyId = searchByIdRequest.getCompanyId();
        boolean useCache = searchByIdRequest.isUseCache();

        Collections.sort(mpIds);
        if (CollectionUtils.isEmpty(mpIds)) {
            return new HashMap<Long, MerchantProduct>();
        }
        if (mpIds.size() >= batchMaxSize) {
            mpIds = new ArrayList<>(mpIds.subList(0, batchMaxSize));
        }
        SearchByIdRequest request = new SearchByIdRequest(mpIds, companyId);
        SearchByIdResponse response = null;
        if (useCache) {
            response = SearchByIdCacheImpl.instance.get(request);
            if (response != null) {
                return response.getMerchantProducts();
            }
        }
        response = new SearchByIdResponse();
        Map<Long, MerchantProduct> result = searchByIdHandler.searchById(mpIds, companyId);
        response.setMerchantProducts(result);
        response.setTotalHit(result.size());
        SearchByIdCacheImpl.instance.put(request, response);

//        logger.info("调用searchByIdNew..end"+result);
        return result;
    }

    @Override
    public Map<String, MerchantProduct> searchByMpCodes(List<String> mpCodes, Integer companyId) {
        if (CollectionUtils.isEmpty(mpCodes)) {
            return new HashMap<String, MerchantProduct>();
        }
        if (mpCodes.size() >= batchMaxSize) {
            mpCodes = new ArrayList<>(mpCodes.subList(0, batchMaxSize));
        }
        SearchByCodeRequest request = new SearchByCodeRequest(mpCodes, companyId);
        SearchByCodeResponse response = SearchByCodeCacheImpl.instance.get(request);
        if (response != null) {
            return response.getMerchantProducts();
        }
        response = new SearchByCodeResponse();
        Map<String, MerchantProduct> result = searchByIdHandler.searchByMpCodes(mpCodes, companyId);
        response.setMerchantProducts(result);
        SearchByCodeCacheImpl.instance.put(request, response);
        return result;
    }

    @Override
    public SeriesResponse searchBySeriesRequest(SeriesRequest seriesRequest)
            throws SearchException {
        return seriesProductAttrHandler.handle(seriesRequest);
    }

    @Override
    public PromotionSearchResponse promotionSearch(PromotionSearchRequest searchRequest) throws SearchException {
        PromotionSearchResponse response = PromotionSearchCacheImpl.instance.get(searchRequest);
        if (response != null) {
            return response;
        }
        response = promotionSearchHandler.handle(searchRequest);
        PromotionSearchCacheImpl.instance.put(searchRequest, response);
        return response;
    }

    @Override
    public PromotionTypeSearchResponse promotionTypeSearch(PromotionTypeSearchRequest searchRequest) throws SearchException {
        PromotionTypeSearchResponse response = PromotionTypeSearchCacheImpl.instance.get(searchRequest);
        if (response != null) {
            return response;
        }
        response = promotionTypeSearchHandler.handle(searchRequest);
        PromotionTypeSearchCacheImpl.instance.put(searchRequest, response);
        return response;
    }

    @Override
    public SearchResponse pointMpSearch(PointSearchRequest searchRequest) throws SearchException {
        searchRequest.setRequestType(IndexConstants.POINTS_SEARCH);
        SearchResponse searchResponse = SearchCacheImpl.instance.get(searchRequest);
        if (searchResponse != null) {
            return searchResponse;
        }
        searchResponse = searchHandler.handle(searchRequest);//调用搜索并获得返回
        SearchCacheImpl.instance.put(searchRequest, searchResponse);
        return searchResponse;
    }

    //====================================包装成标准soa模式===================================================

    @Override
    public OutputDTO<SearchResponse> searchStandard(InputDTO<SearchRequest> inputDTO) throws SearchException {
//        logger.info("soa 调用入参 ,"+inputDTO.getData());

        SearchResponse search;
        try {
            search = search(inputDTO.getData());
        } catch (Exception e) {
            logger.error("soa fail {}", e);
            return SoaUtil.resultError(e.getMessage());
        }
//        logger.info("soa 调用出参 ,"+search);
        return SoaUtil.resultSucess(search);
    }

    @Override
    public OutputDTO<HotSearchResponse> hotSearchStandard(InputDTO<HotSearchRequest> inputDTO) throws SearchException {
//        logger.info("soa 调用入参 ,"+inputDTO.getData());

        HotSearchResponse response;
        try {
            response = hotSearch(inputDTO.getData());
        } catch (Exception e) {
            logger.error("soa fail {}", e);
            return SoaUtil.resultError(e.getMessage());
        }
//        logger.info("soa 调用出参 ,"+response);
        return SoaUtil.resultSucess(response);
    }

    @Override
    public OutputDTO<SearchResponse> searchProductsStandard(InputDTO<SearchRequest> inputDTO) throws SearchException {
//        logger.info("soa 调用入参 ,"+inputDTO.getData());

        SearchResponse response;
        try {
            response = searchProducts(inputDTO.getData());
        } catch (Exception e) {
            logger.error("soa fail {}", e);
            return SoaUtil.resultError(e.getMessage());
        }
//        logger.info("soa 调用出参 ,"+response);
        return SoaUtil.resultSucess(response);
    }

    @Override
    public OutputDTO<SearchResponse> shopSearchStandard(InputDTO<ShopSearchRequest> inputDTO) throws SearchException {
//        logger.info("soa 调用入参 ,"+inputDTO.getData());

        SearchResponse response;
        try {
            response = shopSearch(inputDTO.getData());
        } catch (Exception e) {
            logger.error("soa fail {}", e);
            return SoaUtil.resultError(e.getMessage());
        }
//        logger.info("soa 调用出参 ,"+response);
        return SoaUtil.resultSucess(response);
    }

    @Override
    public OutputDTO<SearchByIdResponse> searchByIdStandard(InputDTO<SearchByIdRequest> inputDTO) throws SearchException {
//        logger.info("soa 调用入参 ,"+inputDTO.getData());
        SearchByIdResponse searchByIdResponse = new SearchByIdResponse();

        Map<Long, MerchantProduct> merchantProductMap = null;
        try {
            SearchByIdRequest inputDTOData = inputDTO.getData();
            if (inputDTOData!=null){
                merchantProductMap = searchById(inputDTOData.getMpIds(),30);
            }
        } catch (Exception e) {
            logger.error("soa fail {}", e);
            return SoaUtil.resultError(e.getMessage());
        }
//        logger.info("soa 调用出参 ,"+merchantProductMap);

        searchByIdResponse.setMerchantProducts(merchantProductMap);
        return SoaUtil.resultSucess(searchByIdResponse);
    }

    @Override
    public OutputDTO<SearchByIdResponse> searchByIdNewStandard(InputDTO<SearchByIdRequest> inputDTO) throws SearchException {
//        logger.info("soa 调用入参 ,"+inputDTO.getData());
        SearchByIdResponse searchByIdResponse = new SearchByIdResponse();

        Map<Long, MerchantProduct> merchantProductMap;
        try {
            merchantProductMap = searchByIdNew(inputDTO.getData());
        } catch (Exception e) {
            logger.error("soa fail {}", e);
            return SoaUtil.resultError(e.getMessage());
        }
//        logger.info("soa 调用出参 ,"+merchantProductMap);

        searchByIdResponse.setMerchantProducts(merchantProductMap);
        return SoaUtil.resultSucess(searchByIdResponse);
    }

    @Override
    public OutputDTO<SearchByCodeResponse> searchByMpCodesStandard(InputDTO<SearchByCodeRequest> inputDTO) {
//        logger.info("soa 调用入参 ,"+inputDTO.getData());
        SearchByCodeResponse response = new SearchByCodeResponse();

        Map<String, MerchantProduct> searchByMpCodes;
        try {
            SearchByCodeRequest data = inputDTO.getData();
            searchByMpCodes = searchByMpCodes(data.getMpCodes(), data.getCompanyId());
        } catch (Exception e) {
            logger.error("soa fail {}", e);
            return SoaUtil.resultError(e.getMessage());
        }
//        logger.info("soa 调用出参 ,"+searchByMpCodes);
        response.setMerchantProducts(searchByMpCodes);
        return SoaUtil.resultSucess(response);
    }

    @Override
    public OutputDTO<SeriesResponse> searchBySeriesRequestStandard(InputDTO<SeriesRequest> inputDTO) throws SearchException {
//        logger.info("soa 调用入参 ,"+inputDTO.getData());
        SeriesResponse response;
        try {
            response = searchBySeriesRequest(inputDTO.getData());
        } catch (Exception e) {
            logger.error("soa fail {}", e);
            return SoaUtil.resultError(e.getMessage());
        }
//        logger.info("soa 调用出参 ,"+response);
        return SoaUtil.resultSucess(response);
    }

    @Override
    public OutputDTO<PromotionSearchResponse> promotionSearchStandard(InputDTO<PromotionSearchRequest> inputDTO) throws SearchException {
//        logger.info("soa 调用入参 ,"+inputDTO.getData());
        PromotionSearchResponse response;
        try {
            response = promotionSearch(inputDTO.getData());
        } catch (Exception e) {
            logger.error("soa fail {}", e);
            return SoaUtil.resultError(e.getMessage());
        }
//        logger.info("soa 调用出参 ,"+response);
        return SoaUtil.resultSucess(response);
    }

    @Override
    public OutputDTO<PromotionTypeSearchResponse> promotionTypeSearchStandard(InputDTO<PromotionTypeSearchRequest> inputDTO) throws SearchException {
//        logger.info("soa 调用入参 ,"+inputDTO.getData());
        PromotionTypeSearchResponse response;
        try {
            response = promotionTypeSearch(inputDTO.getData());
        } catch (Exception e) {
            logger.error("soa fail {}", e);
            return SoaUtil.resultError(e.getMessage());
        }
//        logger.info("soa 调用出参 ,"+response);
        return SoaUtil.resultSucess(response);
    }

    @Override
    public OutputDTO<SearchResponse> pointMpSearchStandard(InputDTO<PointSearchRequest> inputDTO) throws SearchException {
//        logger.info("soa 调用入参 ,"+inputDTO.getData());

        SearchResponse search;
        try {
            search = pointMpSearch(inputDTO.getData());
        } catch (Exception e) {
            logger.error("soa fail {}", e);
            return SoaUtil.resultError(e.getMessage());
        }
//        logger.info("soa 调用出参 ,"+search);
        return SoaUtil.resultSucess(search);
    }


    //====================================包装成标准soa模式===================================================
}
