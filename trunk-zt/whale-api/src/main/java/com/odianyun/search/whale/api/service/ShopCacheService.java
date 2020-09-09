package com.odianyun.search.whale.api.service;

import com.odianyun.search.whale.api.common.PointConstants;
import com.odianyun.search.whale.api.common.SearchException;
import com.odianyun.search.whale.api.model.Merchant;
import com.odianyun.search.whale.api.model.SearchResponse;
import com.odianyun.search.whale.api.model.geo.Point;
import com.odianyun.search.whale.api.model.req.BaseSearchRequest;
import com.odianyun.search.whale.api.model.req.ChildMerchantSearchRequest;
import com.odianyun.search.whale.api.model.req.ShopListSearchRequest;
import com.odianyun.search.whale.api.model.resp.ChildMerchantSearchResponse;
import com.odianyun.search.whale.api.model.resp.ShopSearchResponse;
import com.odianyun.search.whale.index.api.common.SearchHistorySender;
import com.odianyun.search.whale.index.api.model.req.HistoryWriteRequest;
import com.odianyun.soa.InputDTO;
import com.odianyun.soa.OutputDTO;
import com.odianyun.soa.SoaUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by fishcus on 16/11/22.
 */
public class ShopCacheService implements ShopService{
    static Logger logger = Logger.getLogger(ShopCacheService.class);
    ShopService shopService;

    private static final Logger LOGGER = Logger.getLogger(ShopCacheService.class);

    private final static String keywordAll="*****";

    private static final int DEFAULT_FREQ = 1;

    static final double CHINA_MAX_LONGITUDE = 135.05d;
    static final double CHINA_MIN_LONGITUDE = 73.66d;

    static final double CHINA_MAX_LATITUDE = 53.55d;
    static final double CHINA_MIN_LATITUDE = 3.86d;

    public ShopCacheService(ShopService shopService){
        this.shopService = shopService;
    }
    @Override
    public ShopSearchResponse search(ShopListSearchRequest shopRequest) throws SearchException {
        validateRequest(shopRequest);
        ShopSearchResponse response = shopService.search(shopRequest);
        logSearchHistory(shopRequest,response);
        return response;
    }

    /**
     * 根据父商家id和当前位置返回覆盖当前点的门店列表
     *
     * @param parentMerchantIds
     * @param point
     * @param companyId
     * @return parentMerchantId---->List<ChildMerchant>
     * @throws SearchException
     */
    @Override
    public Map<Long, List<Merchant>> getChildMerchantByPoint(List<Long> parentMerchantIds, Point point, Integer companyId) throws Exception {
        if(CollectionUtils.isEmpty(parentMerchantIds) || parentMerchantIds.size()>100 || point==null || companyId==null){
            throw new RuntimeException("parentMerchantIds is empty or size > 100 or point ==null or companyId==null");
        }
        return shopService.getChildMerchantByPoint(parentMerchantIds,point,companyId);
    }

    /**
     * 根据父商家id和当前位置返回覆盖当前点的门店列表
     *
     * @param parentMerchantIds
     * @param point
     * @param companyId
     * @return List<ChildMerchant>
     * @throws SearchException
     */
    @Override
    public List<Merchant> getChildMerchantSortedByDistance(List<Long> parentMerchantIds, Point point, Integer companyId) throws Exception {
        if(CollectionUtils.isEmpty(parentMerchantIds) || parentMerchantIds.size()>100 || point==null || companyId==null){
            throw new RuntimeException("parentMerchantIds is empty or size > 100 or point ==null or companyId==null");
        }
        return shopService.getChildMerchantSortedByDistance(parentMerchantIds,point,companyId);
    }

    private void validateRequest(ShopListSearchRequest shopRequest){
        if(shopRequest.getCompanyId()==null){
            throw new SearchException("companyId is null");
        }
        if(StringUtils.isBlank(shopRequest.getAddress()) && null == shopRequest.getPoint()){
            throw new SearchException("Address and Point is null ");
        }
        Point point = shopRequest.getPoint();
        if(point != null){
            Double latitude = point.getLatitude();
            if(latitude == null || latitude < PointConstants.MIN_LATITUDE || latitude > PointConstants.MAX_LATITUDE){
                throw new SearchException("latitude is illegal ");
            }
            Double longitude = point.getLongitude();
            if(longitude == null || longitude < PointConstants.MIN_LONGITUDE || longitude > PointConstants.MAX_LONGITUDE){
                throw new SearchException("longitude is illegal ");
            }
        }

    }

    private void logSearchHistory(ShopListSearchRequest searchRequest, ShopSearchResponse response) {

        if (StringUtils.isBlank(searchRequest.getKeyword()) || searchRequest.getKeyword().equals(keywordAll) ) {
            return;
        }
        try{
            final HistoryWriteRequest request = new HistoryWriteRequest(searchRequest.getCompanyId(), searchRequest.getUserId(), searchRequest.getKeyword().trim());
            request.setFrequency(DEFAULT_FREQ);
            int total = (int) response.getTotalHit();
            request.setResultCount(total);
            SearchHistorySender.sendHistory(request);

        }catch(Throwable e){
            LOGGER.warn(e.getMessage());
        }
    }



    //--------------------SOA 标准化---------------------------

    @Override
    public OutputDTO<ShopSearchResponse> searchStandard(InputDTO<ShopListSearchRequest> inputDTO) throws SearchException {
//        logger.info("soa 调用入参 ,"+inputDTO.getData());

        ShopSearchResponse response;
        try {
            response = search(inputDTO.getData());
        } catch (Exception e) {
            logger.error("soa fail {},"+inputDTO, e);
            return SoaUtil.resultError(e.getMessage());
        }
//        logger.info("soa 调用出参 ,"+response);
        return SoaUtil.resultSucess(response);
    }

    @Override
    public OutputDTO<ChildMerchantSearchResponse> getChildMerchantByPointStandard(InputDTO<ChildMerchantSearchRequest> inputDTO) throws Exception {
//        logger.info("soa 调用入参 ,"+inputDTO.getData());

        ChildMerchantSearchResponse response=new ChildMerchantSearchResponse();
        try {
            ChildMerchantSearchRequest inputDTOData = inputDTO.getData();
            if (inputDTOData!=null){
                Map<Long, List<Merchant>> childMerchantByPoint = getChildMerchantByPoint(inputDTOData.getParentMerchantIds(), inputDTOData.getPoint(), 30);
                response.setChildMerchantByPoint(childMerchantByPoint);
            }
        } catch (Exception e) {
            logger.error("soa fail {},"+inputDTO, e);
            return SoaUtil.resultError(e.getMessage());
        }
//        logger.info("soa 调用出参 ,"+response);
        return SoaUtil.resultSucess(response);
    }

    @Override
    public OutputDTO<ChildMerchantSearchResponse> getChildMerchantSortedByDistanceStandard(InputDTO<ChildMerchantSearchRequest> inputDTO) throws Exception {
//        logger.info("soa 调用入参 ,"+inputDTO.getData());

        ChildMerchantSearchResponse response=new ChildMerchantSearchResponse();
        try {
            ChildMerchantSearchRequest inputDTOData = inputDTO.getData();
            if (inputDTOData!=null){
                List<Merchant> childMerchantSortedByDistance = getChildMerchantSortedByDistance(inputDTOData.getParentMerchantIds(), inputDTOData.getPoint(), 30);
                response.setMerchantList(childMerchantSortedByDistance);
            }
        } catch (Exception e) {
            logger.error("soa fail {},"+inputDTO, e);
            return SoaUtil.resultError(e.getMessage());
        }
//        logger.info("soa 调用出参 ,"+response);
        return SoaUtil.resultSucess(response);
    }

}
