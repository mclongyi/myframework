package com.odianyun.search.whale.server;

import com.odianyun.architecture.caddy.SystemContext;
import com.odianyun.search.whale.api.model.*;
import com.odianyun.search.whale.api.model.MerchantProduct;
import com.odianyun.search.whale.api.model.geo.GeoPath;
import com.odianyun.search.whale.api.model.geo.GeoPathRequest;
import com.odianyun.search.whale.api.model.geo.GeoPathResponse;
import com.odianyun.search.whale.api.model.geo.Point;
import com.odianyun.search.whale.api.model.req.BrandSearchRequest;
import com.odianyun.search.whale.api.model.req.GeoPathSearchRequest;
import com.odianyun.search.whale.api.model.req.SuggestRequest;
import com.odianyun.search.whale.api.model.resp.AreaResult;
import com.odianyun.search.whale.api.model.resp.AreaSuggestResponse;
import com.odianyun.search.whale.api.model.resp.GeoPathSearchResponse;
import com.odianyun.search.whale.api.service.SearchBusinessService;
import com.odianyun.search.whale.common.*;
import com.odianyun.search.whale.data.geo.service.POIService;
import com.odianyun.search.whale.data.model.*;
import com.odianyun.search.whale.data.model.geo.POI;
import com.odianyun.search.whale.data.service.*;
import com.odianyun.search.whale.es.api.ESService;
import com.odianyun.search.whale.es.request.ESSearchRequest;
import com.odianyun.search.whale.index.api.common.IndexConstants;
import com.odianyun.search.whale.index.api.common.MerchantAreaIndexContants;
import com.odianyun.search.whale.index.api.common.MerchantAreaIndexFieldContants;
import com.odianyun.soa.InputDTO;
import com.odianyun.soa.OutputDTO;
import com.odianyun.soa.SoaUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHitField;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.*;

/**
 * Created by zengfenghua on 16/12/6.
 */
public class SearchBusinessServer implements SearchBusinessService {

    static Logger logger = Logger.getLogger(SearchBusinessServer.class);

    private static final int CORE_POOL = 50;
    private static final int MAX_POOL = 100;
    private static final int KEEP_ALIVE = 60;
    private static final int QUEUE_SIZE = 200;
    private static final ExecutorService es = new ThreadPoolExecutor(CORE_POOL, MAX_POOL, KEEP_ALIVE,
            TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(QUEUE_SIZE), new ThreadPoolExecutor.CallerRunsPolicy());
    @Autowired
    BrandService brandService;

    @Autowired
    AreaService areaService;

    @Autowired
    ConfigService configService;

    @Autowired
    POIService poiService;

    @Autowired
    MerchantProductSaleAreaService merchantProductSaleAreaService;

    @Autowired
    MerchantProductService merchantProductService;

    @Autowired
    SearchByIdHandler searchByIdHandler;

    @Override
    public BrandResult getBrand(String brandName) {
        BrandResult brandResult = null;
        Long companyId = SystemContext.getCompanyId();
        if (StringUtils.isNotBlank(brandName)) {
            try {
                Brand brand = brandService.getBrand(brandName.trim().toLowerCase(), companyId.intValue());
                if (brand != null) {
                    brandResult = new BrandResult();
                    brandResult.setId(brand.getId());
                    brandResult.setName(brand.getName());
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
        return brandResult;
    }

    @Override
    public AreaSuggestResponse areaSuggest(SuggestRequest suggestRequest) {

        long start = System.currentTimeMillis();
        AreaSuggestResponse response = new AreaSuggestResponse();
        String address = suggestRequest.getInput();
        Integer companyId = suggestRequest.getCompanyId();
        if (org.apache.commons.lang3.StringUtils.isBlank(address) || companyId == null) {
            return response;
        }
        String map_url_config = configService.get("map_url", IndexConstants.MAP_URL, suggestRequest.getCompanyId());
        try {
            long mapStart = System.currentTimeMillis();
            GeoInfo geoInfo = MapService.getGeoInfo(address, map_url_config);
            long cost = System.currentTimeMillis() - mapStart;
            response.setMapCost(cost);
//            logger.info("MapService.getGeoInfo cost : "+cost + " ms");

            if (geoInfo != null) {
                // 高德地图返回的直辖市的province结尾是"市" 北京市
                String province = geoInfo.getProvince();
                if (org.apache.commons.lang3.StringUtils.isNotBlank(province)
                        && province.endsWith("市")) {
                    province = province.substring(0, province.length() - 1);
                }
                response.getAreaResult().add(convert(province, companyId));
                response.getAreaResult().add(convert(geoInfo.getCity(), companyId));
                response.getAreaResult().add(convert(geoInfo.getDistricts(), companyId));

                /*List<String> districtList = geoInfo.getDistrict();
                if(CollectionUtils.isNotEmpty(districtList)){
                    response.getAreaResult().add(convert(geoInfo.getDistrict().get(0),companyId));
                }*/
            }

        } catch (Exception e) {
            response.setMapCost(Long.MAX_VALUE);
            logger.error("MapService.getGeoInfo error : " + e.getMessage(), e);
        }
        response.setCost(System.currentTimeMillis() - start);
        return response;
    }

    @Override
    public GeoPathResponse geoPathSearch(GeoPathRequest geoPathRequest) {
        GeoPathResponse geoPathResponse = new GeoPathResponse();
        String address = geoPathRequest.getAddress();
        Integer companyId = geoPathRequest.getCompanyId();
        Long merchantId = geoPathRequest.getMerchantId();
        Point point = geoPathRequest.getPoint();
        if (companyId == null
                || merchantId == null) {
            return geoPathResponse;
        }
        if (StringUtils.isBlank(address)
                && null == point) {

        }
        String map_working_url_config = configService.get("map_working_url", IndexConstants.MAP_WORKING_URL, companyId);
        String map_url_config = configService.get("map_url", IndexConstants.MAP_URL, companyId);

        Point origin;
        Point destination;
        try {

//            origin = searchMerchant(merchantId,companyId);
            origin = getMerchantPoint(merchantId, companyId);
            if (point == null && org.apache.commons.lang3.StringUtils.isNotBlank(address)) {
                destination = MapService.getPoint(address, map_url_config);
            } else {
                destination = point;
            }


            if (origin != null && destination != null) {
                GeoPathInfo geoPathInfo = MapService.geoGeoPathInfo(origin, destination, map_working_url_config);
                if (geoPathInfo != null) {
                    GeoPath geoPath = new GeoPath();
                    BeanUtils.copyProperties(geoPath, geoPathInfo);
                    geoPathResponse.getGeoPaths().add(geoPath);

                }
            }

        } catch (Exception e) {
            logger.error("MapService.getPoint error : " + e.getMessage(), e);

        }


        return geoPathResponse;
    }

    @Override
    public Map<GeoPathRequest, GeoPathResponse> multiGeoPathSearch(List<GeoPathRequest> geoPathRequestList) {
        final Map<GeoPathRequest, GeoPathResponse> responseMap = new HashMap<>();
        if (CollectionUtils.isEmpty(geoPathRequestList)) {
            return responseMap;
        }
        List<Future> futures = new ArrayList<>();
        for (final GeoPathRequest geoPathRequest : geoPathRequestList) {
            futures.add(es.submit(new Callable() {
                @Override
                public Object call() throws Exception {
                    responseMap.put(geoPathRequest, geoPathSearch(geoPathRequest));
                    return null;
                }
            }));
        }
        if (CollectionUtils.isNotEmpty(futures)) {
            for (Future future : futures) {
                try {
                    future.get();
                } catch (Throwable e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
        return responseMap;
    }

    @Override
    public Map<Long, Boolean> checkMerchantProductSaleArea(List<Long> mpIds, Long areaCode) throws Exception {
        try {
            Long companyId = SystemContext.getCompanyId();
            if (companyId == null) {
                throw new Exception("通过SystemContext获companyId为null!");
            }
            Map<Long, Boolean> retMap = new HashMap<Long, Boolean>();
            calCheckSaleArea(mpIds, retMap, companyId, areaCode);
            return retMap;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
    }

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

    private void calCheckSaleArea(List<Long> mpIds, Map<Long, Boolean> retMap, Long companyId, Long areaCode) throws Exception {
        Map<Long, Set<SaleAreasCover>> areaCodesMap = merchantProductSaleAreaService.queryMerProSaleAreaCoverByMpIds(mpIds, companyId.intValue());
        Map<Long, com.odianyun.search.whale.api.model.MerchantProduct> merchantProductMap = searchByIdHandler.searchById(mpIds, companyId.intValue());
        Map<Long, List<Long>> supplierMerchantProductRel = merchantProductSaleAreaService.querySupplierMerchantProductRelByMpIds(mpIds, companyId.intValue());
        List<Long> areaCodes = areaService.getAllParentAreaCode(areaCode, companyId.intValue());
        Set<Long> areaCodeSet = new HashSet<Long>(areaCodes);
        areaCodeSet.add(areaCode);
        List<Long> merchantMpIds = new ArrayList<Long>();

        for (Long mpId : mpIds) {
            Set<SaleAreasCover> coverSet = areaCodesMap.get(mpId);
            if (coverSet == null) {
                coverSet = new HashSet<>();
            }
            // 获取商家和供应商级别的销售区域
            List<Long> supplierIdList = supplierMerchantProductRel.get(mpId);
            MerchantProduct merchantProduct = merchantProductMap.get(mpId);
            Set<SaleAreasCover> merchantSaleAreasCover = merchantProductSaleAreaService.queryMerProSaleAreaByMerchantIds(supplierIdList, companyId.intValue());
            coverSet.addAll(merchantSaleAreasCover);

            // 通过商家类目查找是否设置销售区域
            if (supplierIdList != null && supplierIdList.size() > 0 && merchantProduct != null && merchantProduct.getCategoryId() != null) {
                Set<SaleAreasCover> merchantCategorySaleAreasCover = merchantProductSaleAreaService.queryMerchantCategorySaleAreasByMerchantCategory(supplierIdList, merchantProduct.getCategoryId(), companyId.intValue());
                coverSet.addAll(merchantCategorySaleAreasCover);
            }

            if (CollectionUtils.isNotEmpty(coverSet)) {
                retMap.put(mpId, checkSaleArea(coverSet, areaCodeSet));
            } else {//需要查询默认销售区域的
                merchantMpIds.add(mpId);
            }
        }
        if (CollectionUtils.isNotEmpty(merchantMpIds)) {
            Map<Long, Long> mpId2MerchantId = merchantProductService.getMerchantIdByMPId(merchantMpIds, companyId.intValue());
            if (mpId2MerchantId == null) {
                mpId2MerchantId = new HashMap<Long, Long>();
            }
            Map<Long, Set<SaleAreasCover>> defaultCovers = new HashMap<Long, Set<SaleAreasCover>>();
            if (mpId2MerchantId.size() > 0) {
                Set<Long> merchantIds = new HashSet<Long>(mpId2MerchantId.values());
                defaultCovers = merchantProductSaleAreaService.queryDefaultSaleCodeByMerchantIds(merchantIds, companyId.intValue());
            }
            for (Long id : merchantMpIds) {
                Long merchantId = mpId2MerchantId.get(id);
                if (merchantId != null) {
                    Set<SaleAreasCover> coverSet = defaultCovers.get(merchantId);
                    if (CollectionUtils.isNotEmpty(coverSet)) {
                        retMap.put(id, checkSaleArea(coverSet, areaCodeSet));
                    } else {
                        retMap.put(id, true);//没有设置销售区域  也没有默认销售区域
                    }
                } else {
                    retMap.put(id, false); //没有对应的商家, 返回false
                }

            }
        }
    }

    private Boolean checkSaleArea(Set<SaleAreasCover> coverSet, Set<Long> areaCodes) throws Exception {
        for (SaleAreasCover sa : coverSet) {
            if (areaCodes.contains(sa.getAreaCode())) {
                return true;
            }
        }
        return false;

    }

    /*private Boolean checkSaleArea(Long mpid,String codes,Long areaCode,Long companyId) throws Exception{
        if(null == codes || codes.equals("-1")){
            List<Long> mpids = new ArrayList<Long>();
            mpids.add(mpid);
            List<MerchantProduct> mpList = merchantProductService.getMerchantProductList(mpids,companyId.intValue());
            if(null == mpList || mpList.size()==0) {
                return false;
            }else{
                MerchantProduct merchantProduct = mpList.get(0);
                String defaultAreaCodes = merchantProductSaleAreaService.queryDefaultSaleCodeByMerchantId(merchantProduct.getMerchant_id(),companyId.intValue());
                if(defaultAreaCodes.equals("-1")){
                    return false;
                }else {
                    codes = defaultAreaCodes;
                }
            }

        }
        String[] codeList = codes.split(" ");
        for(String code : codeList){
            if(areaCode.equals(Long.valueOf(code))){
                return true;
            }
        }
        List<Long> parentCodeList = areaService.getAllParentAreaCode(areaCode,companyId.intValue());
        if(null != parentCodeList && CollectionUtils.isNotEmpty(parentCodeList)){
            for(Long code : parentCodeList){
                if(areaCode == code){
                    return true;
                }
            }
        }
        return false;
    }*/

    private Point getMerchantPoint(Long merchantId, Integer companyId) throws Exception {
        List<POI> poiList = poiService.getShopPOIById(merchantId, companyId);
        if (CollectionUtils.isNotEmpty(poiList)) {
            POI poi = poiList.get(0);
            return new Point(poi.getLongitude(), poi.getLatitude());
        }
        return null;
    }

    private Point searchMerchant(Long merchantId, Integer companyId) throws UnknownHostException {
        ESSearchRequest esSearchRequest = new ESSearchRequest(IndexNameManager.getGeoIndexName(),
                MerchantAreaIndexContants.index_type);
        BoolQueryBuilder boolQuery = new BoolQueryBuilder();
        boolQuery.must(new TermQueryBuilder(MerchantAreaIndexFieldContants.COMPANYID, companyId));
        boolQuery.must(new TermQueryBuilder(MerchantAreaIndexFieldContants.MERCHANTID, merchantId));
        esSearchRequest.setQueryBuilder(boolQuery);
        List<String> fields = new ArrayList<>();
        fields.add(MerchantAreaIndexFieldContants.LOCATION);
        esSearchRequest.setFields(fields);

        SearchResponse esSearchResponse = ESService.search(esSearchRequest);

        SearchHit[] searchHitArray = esSearchResponse.getHits().getHits();
        if (searchHitArray != null && searchHitArray.length > 0) {
            SearchHit hit = searchHitArray[0];
            SearchHitField point = hit.field("location");
            if (point != null) {
                return PointConverter.convert(point);
            }
        }

        return null;
    }

    private AreaResult convert(String name, Integer companyId) throws Exception {
        if (org.apache.commons.lang3.StringUtils.isBlank(name)) {
            return new AreaResult();
        }
        Area province = areaService.getAreaByName(name, companyId);
        return convert(province);
    }

    private AreaResult convert(Area area) {
        AreaResult result = new AreaResult();
        if (area == null) {
            return result;
        }
        result.setAreaCode(area.getCode());
        result.setAreaName(area.getName());
        return result;
    }


}
