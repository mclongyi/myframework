package com.odianyun.search.whale.api.service;

import com.odianyun.search.whale.api.model.BrandResult;
import com.odianyun.search.whale.api.model.geo.GeoPathRequest;
import com.odianyun.search.whale.api.model.geo.GeoPathResponse;
import com.odianyun.search.whale.api.model.req.BrandSearchRequest;
import com.odianyun.search.whale.api.model.req.GeoPathSearchRequest;
import com.odianyun.search.whale.api.model.req.SuggestRequest;
import com.odianyun.search.whale.api.model.resp.AreaSuggestResponse;
import com.odianyun.search.whale.api.model.resp.GeoPathSearchResponse;
import com.odianyun.soa.InputDTO;
import com.odianyun.soa.OutputDTO;
import com.odianyun.soa.SoaUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;

/**
 * Created by zengfenghua on 16/12/7.
 */
public class SearchBusinessCacheService implements SearchBusinessService {
    static Logger logger = Logger.getLogger(SearchBusinessCacheService.class);
    private SearchBusinessService searchBusinessService;

    private String clientName;


    public SearchBusinessCacheService(SearchBusinessService searchBusinessService, String clientName){
        this.searchBusinessService = searchBusinessService;
        this.clientName = clientName;
    }

    @Override
    public BrandResult getBrand(String brandName) {
        if(StringUtils.isBlank(brandName)){
            throw new RuntimeException("brandName is null or '' ");
        }
        return searchBusinessService.getBrand(brandName);
    }

    @Override
    public AreaSuggestResponse areaSuggest(SuggestRequest suggestRequest) {
        if(StringUtils.isBlank(suggestRequest.getInput())){
            throw new RuntimeException("input is null or '' ");
        }
        return searchBusinessService.areaSuggest(suggestRequest);
    }

    @Override
    public GeoPathResponse geoPathSearch(GeoPathRequest geoPathRequest) {
        validateRequest(geoPathRequest);

        return searchBusinessService.geoPathSearch(geoPathRequest);
    }

    private void validateRequest(GeoPathRequest geoPathRequest) {
        if(geoPathRequest.getCompanyId() == null
                || geoPathRequest.getMerchantId() == null){
            throw new RuntimeException("CompanyId or MerchantId is null or '' ");
        }
        if(StringUtils.isBlank(geoPathRequest.getAddress())
                && geoPathRequest.getPoint() == null){
            throw new RuntimeException("Address or poi is null ");
        }
    }

    @Override
    public Map<GeoPathRequest, GeoPathResponse> multiGeoPathSearch(List<GeoPathRequest> geoPathRequestList) {
        if(CollectionUtils.isEmpty(geoPathRequestList)){
            throw new RuntimeException("geoPathRequestList is Empty ");
        }

        for(GeoPathRequest request : geoPathRequestList){
            validateRequest(request);
        }

        return searchBusinessService.multiGeoPathSearch(geoPathRequestList);
    }

    @Override
    public Map<Long, Boolean> checkMerchantProductSaleArea(List<Long> mpIds, Long areaCode) throws Exception {
        if(CollectionUtils.isEmpty(mpIds) || mpIds.size()>20 || areaCode==null){
            throw new RuntimeException("mpIds is null or mpIds size more than 20, or aredCode is null ");

        }
        return searchBusinessService.checkMerchantProductSaleArea(mpIds,areaCode);
    }

/*    @Override
    public AreaSuggestResponse merchantServiceArea(Long areaCode, Integer areaLevel) throws Exception {
        return searchBusinessService.merchantServiceArea(areaCode, areaLevel);
    }*/


    //--------------------SOA 标准化--------------------------------------------
    @Override
    public OutputDTO<BrandResult> getBrandStandard(InputDTO<BrandSearchRequest> inputDTO) {
//        logger.info("soa 调用入参 ," + inputDTO.getData());

        BrandResult response = new BrandResult();
        try {
            BrandSearchRequest inputDTOData = inputDTO.getData();
            if (inputDTOData != null) {
                response = getBrand(inputDTOData.getBrandName());
            }
        } catch (Exception e) {
            logger.error("soa fail {}," + inputDTO, e);
            return SoaUtil.resultError(e.getMessage());
        }
//        logger.info("soa 调用出参 ," + response);
        return SoaUtil.resultSucess(response);
    }

    @Override
    public OutputDTO<AreaSuggestResponse> areaSuggestStandard(InputDTO<SuggestRequest> inputDTO) {
//        logger.info("soa 调用入参 ," + inputDTO.getData());

        AreaSuggestResponse response;
        try {
            response = areaSuggest(inputDTO.getData());
        } catch (Exception e) {
            logger.error("soa fail {}," + inputDTO, e);
            return SoaUtil.resultError(e.getMessage());
        }
//        logger.info("soa 调用出参 ," + response);
        return SoaUtil.resultSucess(response);
    }

    @Override
    public OutputDTO<GeoPathResponse> geoPathSearchStandard(InputDTO<GeoPathRequest> inputDTO) {
//        logger.info("soa 调用入参 ," + inputDTO.getData());

        GeoPathResponse response;
        try {
            response = geoPathSearch(inputDTO.getData());
        } catch (Exception e) {
            logger.error("soa fail {}," + inputDTO, e);
            return SoaUtil.resultError(e.getMessage());
        }
//        logger.info("soa 调用出参 ," + response);
        return SoaUtil.resultSucess(response);
    }

    @Override
    public OutputDTO<GeoPathSearchResponse> multiGeoPathSearchStandard(InputDTO<GeoPathSearchRequest> inputDTO) {
//        logger.info("soa 调用入参 ," + inputDTO.getData());

        GeoPathSearchResponse response = new GeoPathSearchResponse();
        try {
            GeoPathSearchRequest inputDTOData = inputDTO.getData();
            if (inputDTOData!=null){
                Map<GeoPathRequest, GeoPathResponse> multiGeoPathSearch = multiGeoPathSearch(inputDTOData.getGeoPathRequestList());
                response.setMultiGeoPathSearch(multiGeoPathSearch);
            }

        } catch (Exception e) {
            logger.error("soa fail {}," + inputDTO, e);
            return SoaUtil.resultError(e.getMessage());
        }
//        logger.info("soa 调用出参 ," + response);
        return SoaUtil.resultSucess(response);
    }

    @Override
    public OutputDTO<GeoPathSearchResponse> checkMerchantProductSaleAreaStandard(InputDTO<GeoPathSearchRequest> inputDTO) throws Exception {
//        logger.info("soa 调用入参 ," + inputDTO.getData());

        GeoPathSearchResponse response = new GeoPathSearchResponse();
        try {
            GeoPathSearchRequest inputDTOData = inputDTO.getData();
            if (inputDTOData!=null){
                Map<Long, Boolean> productSaleArea = checkMerchantProductSaleArea(inputDTOData.getMpIds(), inputDTOData.getAreaCode());
                response.setProductSaleArea(productSaleArea);
            }

        } catch (Exception e) {
            logger.error("soa fail {}," + inputDTO, e);
            return SoaUtil.resultError(e.getMessage());
        }
//        logger.info("soa 调用出参 ," + response);
        return SoaUtil.resultSucess(response);
    }
    //--------------------SOA 标准化--------------------------------------------

}
