//package com.odianyun.search.whale.rest.action;
//
//import com.odianyun.architecture.caddy.SystemContext;
//import com.odianyun.frontier.global.web.ApiBaseController;
//import com.odianyun.frontier.global.web.JsonResult;
//import com.odianyun.search.whale.api.model.SearchResponse;
//import com.odianyun.search.whale.api.model.req.ShopSearchRequest;
//import com.odianyun.search.whale.api.model.resp.AreaResult;
//import com.odianyun.search.whale.api.model.resp.AreaSuggestResponse;
//import com.odianyun.search.whale.rest.model.vo.merchant.MerchantMpCountVO;
//import com.odianyun.search.whale.server.SearchBusinessServer;
//import com.odianyun.search.whale.server.WhaleServer;
//import com.odianyun.search.whale.shop.ShopServer;
//import org.apache.log4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by Fu Yifan on 2017/11/20.
// */
//@Controller
//@RequestMapping(value = "/merchant")
//public class MerchantSearchReadAction extends ApiBaseController {
//    private static Logger log = Logger.getLogger(MerchantSearchReadAction.class);
//
//    @Autowired
//    private WhaleServer whaleServer;
//    @Autowired
//    SearchBusinessServer searchBusinessServer;
//
//    /**
//     * 门店商品数量
//     * @param merchantId
//     * @param areaCode
//     * @return
//     */
//    @RequestMapping(value = "/merchantMpCount")
//    public @ResponseBody
//    JsonResult getMerchantMpCount(@RequestParam(required = true) Long merchantId, @RequestParam(required = false) Long areaCode) {
//        MerchantMpCountVO result = new MerchantMpCountVO();
//        Long companyId = SystemContext.getCompanyId();
//        if (merchantId == null || companyId == null) {
//            return returnError(null, "门店id未传");
//        }
//        result.setMerchantId(merchantId);
//        ShopSearchRequest searchRequest = new ShopSearchRequest(companyId.intValue(), merchantId);
//        if (areaCode != null) {
//            List<Long> saleAreaCodeList = new ArrayList<>();
//            saleAreaCodeList.add(areaCode);
//            searchRequest.setSaleAreaCode(saleAreaCodeList);
//        }
//        try {
//            SearchResponse searchResponse = whaleServer.shopSearch(searchRequest);
//            if (searchRequest != null) {
//                result.setMpNum(searchResponse.totalHit);
//            }
//            searchRequest.setIsNew(1);
//            searchResponse = whaleServer.shopSearch(searchRequest);
//            if (searchRequest != null) {
//                result.setNewMpNum(searchResponse.totalHit);
//            }
//        } catch (Exception e) {
//            log.error("获取门店商品数量异常", e);
//        }
//        return returnSuccess(result);
//    }
//
//    /**
//     * 服务城市列表
//     * @param areaLevel
//     * @param areaCode
//     * @return
//     */
//    @RequestMapping(value = "/serviceAreaList")
//    public @ResponseBody JsonResult getserviceAreaList(@RequestParam(required = false) Integer areaLevel, @RequestParam(required = false) Long areaCode) {
//        List<AreaResult> result = new ArrayList<>();
//        try {
//            AreaSuggestResponse areaSuggestResponse = searchBusinessServer.merchantServiceArea(areaCode, areaLevel);
//            if (areaSuggestResponse != null && areaSuggestResponse.getAreaResult() != null) {
//                result =  areaSuggestResponse.getAreaResult();
//            }
//        } catch (Exception e) {
//            log.error("获取服务城市列表异常", e);
//        }
//        return returnSuccess(result);
//    }
//
//    /**
//     * 获取门店列表
//     * @param input
//     * @param user
//     * @return
//    @RequestMapping(value = "/queryMerchantShopList")
//    public @ResponseBody JsonResult queryMerchantShopList(MerchantShopInputVO input, @LoginContext(required = false) UserCache user) {
//        MerchantShopVO result = new MerchantShopVO();
//        Long companyId = SystemContext.getCompanyId();
//        Long userId = null;
//        if (user != null && user.getId() != null) {
//            userId = user.getId();
//        }
//        Integer pageNum = input.getPageNum() == null ? 1 : input.getPageNum();
//        Integer pageSize = input.getPageSize() == null ? 10 : input.getPageSize();
//        Point point = new Point(input.getLongitude(), input.getLatitude());
//        Integer inputCompany = companyId.intValue();
//        ShopListSearchRequest searchRequest = new ShopListSearchRequest(point, inputCompany);
//        //控制是否拿热销商品
//        searchRequest.setAdditionalHotProduct(false);
//        searchRequest.setKeyword(input.getKeyword());
//        searchRequest.setMerchantCode(input.getMerchantCode());
//        searchRequest.setStart((pageNum - 1) * pageSize);
//        searchRequest.setCount(pageSize);
//        if (userId != null) {
//            searchRequest.setUserId(userId.toString());
//        }
//        ShopSearchResponse searchResponse = shopServer.search(searchRequest);
//
//        return returnSuccess(searchResponse);
//    }*/
//}
