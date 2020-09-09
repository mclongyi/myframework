package com.odianyun.search.whale.shop;

import com.odianyun.search.whale.api.common.SearchException;
import com.odianyun.search.whale.api.model.Merchant;
import com.odianyun.search.whale.api.model.geo.Point;
import com.odianyun.search.whale.api.model.req.ChildMerchantSearchRequest;
import com.odianyun.search.whale.api.model.req.ShopListSearchRequest;
import com.odianyun.search.whale.api.model.resp.ChildMerchantSearchResponse;
import com.odianyun.search.whale.api.model.resp.ShopSearchResponse;
import com.odianyun.search.whale.api.service.ShopService;
import com.odianyun.search.whale.es.api.ESService;
import com.odianyun.search.whale.es.request.ESSearchRequest;
import com.odianyun.search.whale.index.api.common.MerchantAreaIndexContants;
import com.odianyun.search.whale.index.api.common.OplusOIndexConstants;
import com.odianyun.search.whale.shop.req.ShopRequestBuilder;
import com.odianyun.search.whale.shop.resq.ShopResponseHandler;
import com.odianyun.soa.InputDTO;
import com.odianyun.soa.OutputDTO;
import com.odianyun.soa.SoaUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.elasticsearch.action.search.SearchResponse;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * Created by fishcus on 16/11/22.
 */
public class ShopServer implements ShopService{

    static Logger logger = Logger.getLogger(ShopServer.class);

//    @Autowired
//    GeoSearchServer geoSearchServer;

    List<ShopRequestBuilder> requestBuilders;

    List<ShopResponseHandler> responseHandlers;


    @Autowired
    GetChildMerchantByPointHandler getChildMerchantByPointHandler;

    @Override
    public ShopSearchResponse search(ShopListSearchRequest shopRequest) throws SearchException {

        ShopSearchResponse response = new ShopSearchResponse();
        response.setCompanyId(shopRequest.getCompanyId());
        try{
            if(StringUtils.isNotBlank(shopRequest.getKeyword())){
                shopRequest.setAdditionalHotProduct(false);
            }
            /*preSearch(shopRequest,response);

            if(CollectionUtils.isEmpty(response.getShopResult()) || !shopRequest.isAdditionalHotProduct()){
                return response;
            }*/

            ESSearchRequest esSearchRequest = new ESSearchRequest(OplusOIndexConstants.index_alias,
                    MerchantAreaIndexContants.index_type);
            for(ShopRequestBuilder builder : requestBuilders){
                builder.build(esSearchRequest,shopRequest,response);
            }

            SearchResponse searchResponse = ESService.search(esSearchRequest);

            for(ShopResponseHandler handler : responseHandlers){
                handler.handle(searchResponse,response,shopRequest);
            }

        }catch (SearchException e) {
            logger.error(e.getMessage(), e);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

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
        return getChildMerchantByPointHandler.getChildMerchantByPoint(parentMerchantIds,point,companyId);
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
        return getChildMerchantByPointHandler.getChildMerchantSortedByDistance(parentMerchantIds,point,companyId);
    }

    //--------------------SOA 标准化---------------------------

    @Override
    public OutputDTO<ShopSearchResponse> searchStandard(InputDTO<ShopListSearchRequest> inputDTO) throws SearchException {
        logger.info("soa 调用入参 ,"+inputDTO.getData());

        ShopSearchResponse response;
        try {
            response = search(inputDTO.getData());
        } catch (Exception e) {
            logger.error("soa fail {},"+inputDTO, e);
            return SoaUtil.resultError(e.getMessage());
        }
        logger.info("soa 调用出参 ,"+response);
        return SoaUtil.resultSucess(response);
    }

    @Override
    public OutputDTO<ChildMerchantSearchResponse> getChildMerchantByPointStandard(InputDTO<ChildMerchantSearchRequest> inputDTO) throws Exception {
        logger.info("soa 调用入参 ,"+inputDTO.getData());

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
        logger.info("soa 调用出参 ,"+response);
        return SoaUtil.resultSucess(response);
    }

    @Override
    public OutputDTO<ChildMerchantSearchResponse> getChildMerchantSortedByDistanceStandard(InputDTO<ChildMerchantSearchRequest> inputDTO) throws Exception {
        logger.info("soa 调用入参 ,"+inputDTO.getData());

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
        logger.info("soa 调用出参 ,"+response);
        return SoaUtil.resultSucess(response);
    }

    //--------------------SOA 标准化---------------------------

    /*private void preSearch(ShopListSearchRequest shopRequest, ShopSearchResponse response) {

        GeoSearchResponse geoSearchResponse = geoSearchServer.search(shopRequest);
        List<Merchant> merchants = geoSearchResponse.getMerchants();
        if(CollectionUtils.isNotEmpty(merchants)){
            List<ShopSearchResult> shopResultList = new ArrayList<>();
            for(Merchant merchant : merchants){
                ShopSearchResult shopResult = new ShopSearchResult();
                shopResult.setMerchant(merchant);
                shopResultList.add(shopResult);
            }
            response.setShopResult(shopResultList);
        }
        response.setTotalHit(geoSearchResponse.getTotalHit());
    }*/

    public List<ShopRequestBuilder> getRequestBuilders() {
        return requestBuilders;
    }

    public void setRequestBuilders(List<ShopRequestBuilder> requestBuilders) {
        this.requestBuilders = requestBuilders;
    }

    public List<ShopResponseHandler> getResponseHandlers() {
        return responseHandlers;
    }

    public void setResponseHandlers(List<ShopResponseHandler> responseHandlers) {
        this.responseHandlers = responseHandlers;
    }
}
