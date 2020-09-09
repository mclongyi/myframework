package com.odianyun.search.whale.server;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.odianyun.architecture.caddy.SystemContext;
import com.odianyun.search.whale.api.model.*;
import com.odianyun.search.whale.api.model.geo.*;
import com.odianyun.search.whale.api.model.req.*;
import com.odianyun.search.whale.api.model.resp.AreaSuggestResponse;
import com.odianyun.search.whale.api.model.resp.HotSearchResponse;
import com.odianyun.search.whale.api.model.resp.ShopSearchResponse;
import com.odianyun.search.whale.api.model.selectionproduct.*;
import com.odianyun.search.whale.api.service.SearchBusinessService;
import com.odianyun.search.whale.api.service.SearchClient;
import com.odianyun.search.whale.api.service.ShopService;
import com.odianyun.search.whale.cache.SearchCacheImpl;
import com.odianyun.search.whale.common.IndexNameManager;
import com.odianyun.search.whale.common.cache.ICache;
import com.odianyun.search.whale.common.cache.ocache.BaseCache;
import com.odianyun.search.whale.index.api.common.SearchHistorySender;
import com.odianyun.search.whale.index.api.model.req.HistoryType;
import com.odianyun.search.whale.index.api.model.req.HistoryWriteRequest;
import com.odianyun.search.whale.rest.model.constant.SearchConstants;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.odianyun.search.whale.api.service.SearchService;
import com.odianyun.search.whale.common.util.GsonUtil;
import com.odianyun.search.whale.common.util.HttpClientUtil;
import com.odianyun.search.whale.data.manager.CompanyDBCacheManager;
import com.odianyun.search.whale.es.api.ESService;
import com.odianyun.search.whale.es.request.ESSearchRequest;
import com.odianyun.search.whale.index.api.common.IndexConstants;
import com.odianyun.search.whale.index.api.common.IndexFieldConstants;


@Controller
public class RestSearchService {

	static Logger logger = Logger.getLogger(RestSearchService.class);

	@Autowired
	SearchHandler searchHandler;
	@Autowired
	WhaleServer whaleServer;
	@Autowired
	SearchBusinessServer searchBusinessServer;

	static final String CLIENT_NAME = "searchRestFul";

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/searchByIds")
	public Object shopListSearch(@RequestParam(required = false) String mpIds, @RequestParam(required = false) Integer companyId) {
		Map<Long, MerchantProduct> map = SearchClient.getSearchService(SearchConstants.POOL_NAME).searchById(parseLong(mpIds), companyId);
		return map;
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/promotionSelectionSearch")
	public Object promotionSelectionSearch(@RequestParam(required = false) Long merchantId, @RequestParam(required = false) Integer start,
										   @RequestParam(required = false) Integer count,@RequestParam(required = false) String types,
										   @RequestParam(required = false) Integer companyId) {
		PromotionProductSearchRequest promotionProductSearchRequest = new PromotionProductSearchRequest(companyId, Arrays.asList(merchantId));
		promotionProductSearchRequest.setStart(start);
		promotionProductSearchRequest.setCount(count);
		List<Integer> typeList = new ArrayList<>();
		if (types != null) {
			String[] typeArray = types.split(",");
			for (String typeString : typeArray) {
				Integer type = Integer.parseInt(typeString);
				typeList.add(type);
			}
			promotionProductSearchRequest.setTypes(typeList);
		}
		promotionProductSearchRequest.setManagementState(ManagementType.ON_SHELF);
		promotionProductSearchRequest.setCombine(false);
		promotionProductSearchRequest.getTypeOfProductFilter().removeType(Arrays.asList(TypeOfProduct.COMBINE));
		logger.info(GsonUtil.getGson().toJson(promotionProductSearchRequest));
		PromotionProductSearchResponse promotionProductSearchResponse = SearchClient.getSelectionProductSearchService(SearchConstants.POOL_NAME).promotionSelectionSearch(promotionProductSearchRequest);
		return promotionProductSearchResponse;
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/shopListSearch")
	public String shopListSearch(@RequestParam(value = "point",required = false) String point,
								 @RequestParam(value = "address",required = false) String address,
								 @RequestParam(value = "keyword",required = false) String keyword,
								 @RequestParam(value = "merchantCode",required = false) String merchantCode,
								 @RequestParam(value = "merchantType",required = false) String merchantType,
								 @RequestParam(value = "num",required = false) Integer num,
								 @RequestParam(value = "companyId",required = true) Integer companyId,
								 @RequestParam(value = "start",required = false) Integer start,
								 @RequestParam(value = "count",required = false) Integer count,
								 @RequestParam(value = "isAdditionalHotProduct",required = false) Boolean isAdditionalHotProduct) throws Exception{
		if(StringUtils.isBlank(point) && StringUtils.isBlank(address)){
			return "point && address is null";
		}

		Point poi = null;
		ShopListSearchRequest shopListSearchRequest;
		if(StringUtils.isNotBlank(point)){
			poi = convert(point);
			if(poi == null ){
				return "illegal point , please use poit like 121.600543,31.199196";
			}
			shopListSearchRequest = new ShopListSearchRequest(poi,companyId);
		}else{
			shopListSearchRequest = new ShopListSearchRequest(address,companyId);
		}
		if(org.apache.commons.lang3.StringUtils.isNotBlank(keyword)){
			isAdditionalHotProduct = false;
			shopListSearchRequest.setKeyword(keyword);
		}
		if(start != null){
			shopListSearchRequest.setStart(start);
		}
		if(count != null){
			shopListSearchRequest.setCount(count);
		}
		if(num != null){
			shopListSearchRequest.setNum(num);
		}
		if(org.apache.commons.lang3.StringUtils.isNotBlank(merchantCode)){
			shopListSearchRequest.setMerchantCode(merchantCode);
		}
		if(org.apache.commons.lang3.StringUtils.isNotBlank(merchantType)){
			shopListSearchRequest.setMerchantType(merchantType);
		}
		shopListSearchRequest.setAdditionalHotProduct(isAdditionalHotProduct);
		ShopService shopService = SearchClient.getShopService(CLIENT_NAME);
		ShopSearchResponse shopSearchResponse = shopService.search(shopListSearchRequest);
		return GsonUtil.getGson().toJson(shopSearchResponse);
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/hotSearch")
	public String hotSearch(@RequestParam(value = "point", required = true) String point,
									   @RequestParam(value = "companyId", required = true) Integer companyId,
									   @RequestParam(value = "start", required = false) Integer start,
									   @RequestParam(value = "count", required = false) Integer count) throws Exception{
		HotSearchRequest hotSearchRequest = new HotSearchRequest(companyId);
		if(StringUtils.isNotBlank(point)){
			Point poi = convert(point);
			if(poi == null ){
				return "illegal point , please use poit like 121.600543,31.199196";
			}
			hotSearchRequest.setPoint(poi);

		}
		if(start != null){
			hotSearchRequest.setStart(start);
		}
		if(count != null){
			hotSearchRequest.setCount(count);
		}
		SearchService searchService = SearchClient.getSearchService(CLIENT_NAME);
		HotSearchResponse hotSearchResponse = searchService.hotSearch(hotSearchRequest);
		return GsonUtil.getGson().toJson(hotSearchResponse);
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/getBrand")
	public BrandResult getBrand(@RequestParam(value = "brandName", required = true) String brandName,
								@RequestParam(value = "companyId", required = false) Integer companyId) throws Exception{
		SearchBusinessService searchBusinessService = SearchClient.getSearchBusinessService(CLIENT_NAME);

		BrandResult brandResult = searchBusinessService.getBrand(brandName);
		return brandResult;
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/checkSaleArea")
	public Map<Long, Boolean> checkSaleArea(@RequestParam(value = "mpIds", required = true) String mpIds, @RequestParam(value = "areaCode", required = true) Long areaCode,
											@RequestParam(value = "companyId", required = false) Long companyId) throws Exception{
//		SearchBusinessService searchBusinessService = SearchClient.getSearchBusinessService(CLIENT_NAME);
		SystemContext.setCompanyId(companyId);
		List<Long> mpList = new ArrayList<Long>();
		String[] ids = mpIds.split(",");
		for(int i=0;i<ids.length;i++){
			mpList.add(Long.valueOf(ids[i]));
		}
		Map<Long,Boolean>  resultMap = searchBusinessServer.checkMerchantProductSaleArea(mpList,areaCode);
		return resultMap;
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/geoPathSearch")
	public String geoPathSearch(@RequestParam(value = "address", required = false) String address,
										 @RequestParam(value = "point", required = false) String point,
										 @RequestParam(value = "companyId", required = true) Integer companyId,
										 @RequestParam(value = "merchantId", required = true) Long merchantId) throws Exception{
		GeoPathRequest geoPathRequest = new GeoPathRequest();
		if(StringUtils.isNotBlank(point)){
			geoPathRequest.setPoint(convert(point));
		}
		geoPathRequest.setAddress(address);
		geoPathRequest.setCompanyId(companyId);
		geoPathRequest.setMerchantId(merchantId);
		SearchBusinessService searchBusinessService = SearchClient.getSearchBusinessService(CLIENT_NAME);
		GeoPathResponse geoPathResponse = searchBusinessService.geoPathSearch(geoPathRequest);
		return GsonUtil.getGson().toJson(geoPathResponse);
	}

	private Point convert(String point) {
		String[] array = point.split(",");
		if(array == null || array.length !=2){
			return null;
		}
		Double lon = Double.valueOf(array[0].trim());
		Double lat = Double.valueOf(array[1].trim());
		return new Point(lon,lat);
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/areaSuggest")
	public String areaSuggest(@RequestParam(value = "address", required = true) String address,
										   @RequestParam(value = "companyId", required = true) Integer companyId) throws Exception{
		SuggestRequest suggestRequest = new SuggestRequest(companyId,address);
		SearchBusinessService searchBusinessService = SearchClient.getSearchBusinessService(CLIENT_NAME);
		AreaSuggestResponse areaSuggestResponse = searchBusinessService.areaSuggest(suggestRequest);
		return GsonUtil.getGson().toJson(areaSuggestResponse);
	}


	@RequestMapping(method = RequestMethod.GET, value = "/product.json")
	@ResponseBody
	public String search(String keyword, String categoryIdSearch, Integer categoryType, String attrValueId_search,
						 Long merchantId, String brandIdSearch, String coverProvinceId, String sortSpec, Integer start, Integer count, Integer companyId, String filterType,
						 Integer isTodayNew, Integer isNew, String eanNos, Long saleAreaCode, Integer isDistributionMp, String promotionTypes, String types, Long userId, Double longitude, Double latitude) throws Exception{
		SearchRequest searchRequest=new SearchRequest(companyId);

		if(longitude != null && latitude != null){
			searchRequest.setPoint(new Point(longitude, latitude));
		}

		if(start!=null){
			searchRequest.setStart(start);
		}
		if(count!=null){
			searchRequest.setCount(count);
		}
		if(StringUtils.isNotEmpty(keyword)){
			searchRequest.setKeyword(keyword);
		}
		if(StringUtils.isNotEmpty(categoryIdSearch)){
			List<Long> cids=parseLong(categoryIdSearch);
			if(categoryType!=null && categoryType==2){
				searchRequest.setNavCategoryIds(cids);
			}else{
				searchRequest.setCategoryIds(cids);
			}

		}
		if(StringUtils.isNotEmpty(attrValueId_search)){
			searchRequest.setAttrValueIds(parseLong(attrValueId_search));
		}
//	    if(merchantId!=null){
//	    	searchRequest.setMerchantId(merchantId);
//	    }
		if(StringUtils.isNotEmpty(brandIdSearch)){
			searchRequest.setBrandIds(parseLong(brandIdSearch));
		}
		if(StringUtils.isNotEmpty(coverProvinceId)){
			searchRequest.setCoverProvinceIds(parseInt(coverProvinceId));
		}
		if(StringUtils.isNotEmpty(sortSpec)){
			String sortField=sortSpec.substring(0, sortSpec.length()-2);
			int sortType=Integer.valueOf(sortSpec.substring(sortSpec.length()-1));
			if(sortField.equals(IndexFieldConstants.CREATE_TIME)){
				searchRequest.setSortType(sortType==0?SortType.create_time_asc:SortType.create_time_desc);
			}
			if(sortField.equals(IndexFieldConstants.PRICE)){
				searchRequest.setSortType(sortType==0?SortType.price_asc:SortType.price_desc);
			}
			if (sortField.equals(IndexFieldConstants.VOLUME4SALE)) {
				searchRequest.setSortType(sortType==0?SortType.volume4sale_asc:SortType.volume4sale_desc);
			}
			if (sortField.equals(IndexFieldConstants.RATE)) {
				searchRequest.setSortType(sortType==0?SortType.rate_asc:SortType.rate_desc);
			}
			if (sortField.equals(IndexFieldConstants.RATING_COUNT)) {
				searchRequest.setSortType(sortType==0?SortType.rating_count_asc:SortType.rating_count_desc);
			}
			if (sortField.equals(IndexFieldConstants.POSITIVE_RATE)) {
				searchRequest.setSortType(sortType==0?SortType.positive_rate_asc:SortType.positive_rate_desc);
			}
			if (sortField.equals(IndexFieldConstants.FIRST_SHELF_TIME)) {
				searchRequest.setSortType(sortType==0?SortType.first_shelf_time_asc:SortType.first_shelf_time_desc);
			}
			if (sortField.equals(IndexFieldConstants.COMMODITYCOMMISSION)) {
				searchRequest.setSortType(sortType==0?SortType.commodityCommission_asc:SortType.commodityCommission_desc);
			}
			if(searchRequest.getSortType() != null){
				List<SortType> sortTypeList = new ArrayList<>();
				sortTypeList.add(searchRequest.getSortType());
				searchRequest.setSortTypeList(sortTypeList);
			}
		}
		if(companyId!=null){
			searchRequest.setCompanyId(companyId);
		}
		if(filterType!=null){
			String[] filterTypeString = filterType.split(",");
			List<FilterType> filterTypes = new ArrayList<FilterType>();
			for(String str : filterTypeString){
				if(str.equals("IS_NEW")){
					filterTypes.add(FilterType.IS_NEW);
				}else if(str.equals("HAS_STOCK")){
					filterTypes.add(FilterType.HAS_STOCK);
				}else if(str.equals("SELF_SUPPORT")){
					filterTypes.add(FilterType.SELF_SUPPORT);
				}
			}
			searchRequest.setFilterTypes(filterTypes);
		}
		if(StringUtils.isNotBlank(eanNos)){
			String[] eanArray=eanNos.split(",");
			searchRequest.setEanNos(Arrays.asList(eanArray));
		}
		if (StringUtils.isNotBlank(promotionTypes)) {
			List<Integer> promotionTypeList = new ArrayList<>();
			String[] promotionTypeArray = promotionTypes.split(",");
			for (String promotionTypeString : promotionTypeArray) {
				Integer promotionType = Integer.parseInt(promotionTypeString);
				promotionTypeList.add(promotionType);
			}

			searchRequest.setPromotionTypeList(promotionTypeList);
		}
		if (StringUtils.isNotBlank(types)) {
			List<Integer> typeList = new ArrayList<>();
			String[] typeArray = types.split(",");
			for (String typeString : typeArray) {
				Integer type = Integer.parseInt(typeString);
				typeList.add(type);
			}

			searchRequest.setTypes(typeList);
		}
		if(saleAreaCode!=null){
			List<Long> saleAreaCodes = new ArrayList<Long>();
			saleAreaCodes.add(saleAreaCode);
		}
		SearchService searchService = SearchClient.getSearchService(CLIENT_NAME);

	    /*if(isTodayNew != null){
	    	searchRequest.setIsTodayNew(isTodayNew);
		}*/
		if(isNew!=null){
			searchRequest.setIsNew(isNew);
		}
		if(isDistributionMp!=null){
			searchRequest.setDistributionMp(isDistributionMp==1?true:false);
		}
		if (userId != null) {
			searchRequest.setUserId(userId.toString());
		}
		if (merchantId != null) {
			searchRequest.setMerchantId(merchantId);
		}
		SearchResponse searchResponse=whaleServer.search(searchRequest);
		return GsonUtil.getGson().toJson(searchResponse);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/pointProduct.json")
	@ResponseBody
	public String pointsSearch(String keyword, String categoryIdSearch, Integer categoryType, String attrValueId_search,
						 Long merchantId, String brandIdSearch, String coverProvinceId, String sortSpec, Integer start, Integer count, Integer companyId, String filterType,
						 Integer isTodayNew, Integer isNew, String eanNos, Long saleAreaCode, Integer isDistributionMp, String promotionTypes, String types, Long userId) throws Exception{
		PointSearchRequest searchRequest=new PointSearchRequest(companyId);

		if(start!=null){
			searchRequest.setStart(start);
		}
		if(count!=null){
			searchRequest.setCount(count);
		}
		if(StringUtils.isNotEmpty(keyword)){
			searchRequest.setKeyword(keyword);
		}
		if(StringUtils.isNotEmpty(categoryIdSearch)){
			List<Long> cids=parseLong(categoryIdSearch);
			if(categoryType!=null && categoryType==2){
				searchRequest.setNavCategoryIds(cids);
			}else{
				searchRequest.setCategoryIds(cids);
			}

		}
		if(StringUtils.isNotEmpty(attrValueId_search)){
			searchRequest.setAttrValueIds(parseLong(attrValueId_search));
		}
//	    if(merchantId!=null){
//	    	searchRequest.setMerchantId(merchantId);
//	    }
		if(StringUtils.isNotEmpty(brandIdSearch)){
			searchRequest.setBrandIds(parseLong(brandIdSearch));
		}
		if(StringUtils.isNotEmpty(coverProvinceId)){
			searchRequest.setCoverProvinceIds(parseInt(coverProvinceId));
		}
		if(StringUtils.isNotEmpty(sortSpec)){
			String sortField=sortSpec.substring(0, sortSpec.length()-2);
			int sortType=Integer.valueOf(sortSpec.substring(sortSpec.length()-1));
			if(sortField.equals(IndexFieldConstants.CREATE_TIME)){
				searchRequest.setSortType(sortType==0?SortType.create_time_asc:SortType.create_time_desc);
			}
			if(sortField.equals(IndexFieldConstants.PRICE)){
				searchRequest.setSortType(sortType==0?SortType.price_asc:SortType.price_desc);
			}
			if (sortField.equals(IndexFieldConstants.VOLUME4SALE)) {
				searchRequest.setSortType(sortType==0?SortType.volume4sale_asc:SortType.volume4sale_desc);
			}
			if (sortField.equals(IndexFieldConstants.RATE)) {
				searchRequest.setSortType(sortType==0?SortType.rate_asc:SortType.rate_desc);
			}
			if (sortField.equals(IndexFieldConstants.RATING_COUNT)) {
				searchRequest.setSortType(sortType==0?SortType.rating_count_asc:SortType.rating_count_desc);
			}
			if (sortField.equals(IndexFieldConstants.POSITIVE_RATE)) {
				searchRequest.setSortType(sortType==0?SortType.positive_rate_asc:SortType.positive_rate_desc);
			}
			if (sortField.equals(IndexFieldConstants.FIRST_SHELF_TIME)) {
				searchRequest.setSortType(sortType==0?SortType.first_shelf_time_asc:SortType.first_shelf_time_desc);
			}
			if (sortField.equals(IndexFieldConstants.COMMODITYCOMMISSION)) {
				searchRequest.setSortType(sortType==0?SortType.commodityCommission_asc:SortType.commodityCommission_desc);
			}

		}
		if(companyId!=null){
			searchRequest.setCompanyId(companyId);
		}
		if(filterType!=null){
			String[] filterTypeString = filterType.split(",");
			List<FilterType> filterTypes = new ArrayList<FilterType>();
			for(String str : filterTypeString){
				if(str.equals("IS_NEW")){
					filterTypes.add(FilterType.IS_NEW);
				}else if(str.equals("HAS_STOCK")){
					filterTypes.add(FilterType.HAS_STOCK);
				}else if(str.equals("SELF_SUPPORT")){
					filterTypes.add(FilterType.SELF_SUPPORT);
				}
			}
			searchRequest.setFilterTypes(filterTypes);
		}
		if(StringUtils.isNotBlank(eanNos)){
			String[] eanArray=eanNos.split(",");
			searchRequest.setEanNos(Arrays.asList(eanArray));
		}
		if (StringUtils.isNotBlank(promotionTypes)) {
			List<Integer> promotionTypeList = new ArrayList<>();
			String[] promotionTypeArray = promotionTypes.split(",");
			for (String promotionTypeString : promotionTypeArray) {
				Integer promotionType = Integer.parseInt(promotionTypeString);
				promotionTypeList.add(promotionType);
			}

			searchRequest.setPromotionTypeList(promotionTypeList);
		}
		if (StringUtils.isNotBlank(types)) {
			List<Integer> typeList = new ArrayList<>();
			String[] typeArray = types.split(",");
			for (String typeString : typeArray) {
				Integer type = Integer.parseInt(typeString);
				typeList.add(type);
			}

			searchRequest.setTypes(typeList);
		}
		if(saleAreaCode!=null){
			List<Long> saleAreaCodes = new ArrayList<Long>();
			saleAreaCodes.add(saleAreaCode);
			searchRequest.setSaleAreaCode(saleAreaCodes);
		}
		SearchService searchService = SearchClient.getSearchService(CLIENT_NAME);

	    /*if(isTodayNew != null){
	    	searchRequest.setIsTodayNew(isTodayNew);
		}*/
		if(isNew!=null){
			searchRequest.setIsNew(isNew);
		}
		if(isDistributionMp!=null){
			searchRequest.setDistributionMp(isDistributionMp==1?true:false);
		}
		if (userId != null) {
			searchRequest.setUserId(userId.toString());
		}
		if (merchantId != null) {
			searchRequest.setMerchantId(merchantId);
		}
		SearchResponse searchResponse=SearchClient.getSearchService(SearchConstants.POOL_NAME).pointMpSearch(searchRequest);
//		logSearchHistory(searchRequest, searchResponse, HistoryType.POINT);
		return GsonUtil.getGson().toJson(searchResponse);
	}

	private static List<Long> parseLong(String params){
		List<String> idStrs=Arrays.asList(params.split(","));
    	List<Long> ids=new ArrayList<Long>();
    	for(String id:idStrs){
    		ids.add(Long.valueOf(id));
    	}
    	return ids;
	}

	private static List<Integer> parseInt(String params){
		List<String> idStrs=Arrays.asList(params.split(","));
    	List<Integer> ids=new ArrayList<Integer>();
    	for(String id:idStrs){
    		ids.add(Integer.valueOf(id));
    	}
    	return ids;
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/promotion_keyword.json")
	public String search(String code, String ean, String category, String brand, String merchantId, Integer companyId) throws Exception{
		SearchResponse searchResponse=new SearchResponse();
		List<String> keywords=new ArrayList<String>();
		StringBuffer query_str=new StringBuffer();
	    if(StringUtils.isNotEmpty(code)){
	    	keywords.add(code.toLowerCase());
	    }
	    if(StringUtils.isNotEmpty(ean)){
	    	keywords.add(ean.toLowerCase());
	    }
	    for(String keyword:keywords){
	    	String[] keywordArray=keyword.split(";");
	    	query_str.append("+(");
	    	for(String k:keywordArray){
	    		query_str.append(IndexFieldConstants.TAG_WORDS+":"+k+" ");
	    	}
	    	query_str.append(") ");
	    }

	    if(StringUtils.isNotEmpty(category)){
	    	String[] categoryArray=category.split(";");
	    	query_str.append("+(");
	    	for(String c:categoryArray){
	    		query_str.append(IndexFieldConstants.CATEGORYID_SEARCH+":"+c+" ");
	    	}
	    	query_str.append(") ");
	    }

	    if(StringUtils.isNotEmpty(brand)){
	    	String[] brandArray=brand.split(";");
	    	query_str.append("+(");
	    	for(String b:brandArray){
	    		query_str.append(IndexFieldConstants.BRANDID_SEARCH+":"+b+" ");
	    	}
	    	query_str.append(") ");
	    }

	    if(StringUtils.isNotEmpty(merchantId)){
	    	String[] merchantIdArray=merchantId.split(";");
	    	query_str.append("+(");
	    	for(String m:merchantIdArray){
	    		query_str.append(IndexFieldConstants.MERCHANTID+":"+m+" ");
	    	}
	    	query_str.append(") ");
	    }
	    if(companyId!=null){
	    	query_str.append("+(");
	    	query_str.append(IndexFieldConstants.COMPANYID+":"+companyId);
	    	query_str.append(") ");
	    }
	    QueryStringQueryBuilder queryBuilder=new QueryStringQueryBuilder(query_str.toString());
	    ESSearchRequest esSearchRequest=new ESSearchRequest(IndexNameManager.getIndexName(), IndexConstants.index_type, queryBuilder);
	    esSearchRequest.setStart(0);
	    esSearchRequest.setCount(2000);
	    org.elasticsearch.action.search.SearchResponse esSearchResponse=ESService.search(esSearchRequest);
	    List<Long> merchantProductIds=new ArrayList<Long>();
	    searchResponse.setMerchantProductIds(merchantProductIds);
	    SearchHits searchHits=esSearchResponse.getHits();
	    searchResponse.setTotalHit(searchHits.getTotalHits());
	    SearchHit[] searchHitArray=searchHits.getHits();
	    for(SearchHit hit:searchHitArray){
	    	merchantProductIds.add(Long.valueOf(hit.getId()));
	    }
	    return GsonUtil.getGson().toJson(searchResponse);
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/promotion_keyword2.json")
	public String search2(String code, String ean, String category, String brand, Integer companyId) throws Exception{
		SearchResponse searchResponse=new SearchResponse();
		BoolQueryBuilder boolQueryBuilder=new BoolQueryBuilder();
		List<String> keywords=new ArrayList<String>();
		StringBuffer query_str=new StringBuffer();
	    if(StringUtils.isNotEmpty(code)){
	    	keywords.add(code.toLowerCase());
	    }
	    if(StringUtils.isNotEmpty(ean)){
	    	keywords.add(ean.toLowerCase());
	    }
	    if(keywords.size()>0){
	    	BoolQueryBuilder boolQueryBuilder1=new BoolQueryBuilder();
		    for(String keyword:keywords){
		    	MultiMatchQueryBuilder keywordQueryBuilder = QueryBuilders.multiMatchQuery(keyword);
				// 设置搜索的字段和权重
				keywordQueryBuilder.field(IndexFieldConstants.TAG_WORDS,1.0f);
				// 设置搜索关键词的关系为AND
				keywordQueryBuilder.operator(MatchQueryBuilder.Operator.AND);
				boolQueryBuilder1.should(keywordQueryBuilder);
		    }
		    boolQueryBuilder.must(boolQueryBuilder1);
	    }

	    if(StringUtils.isNotEmpty(category)){
	    	String[] categoryArray=category.split(";");
	    	BoolQueryBuilder boolQueryBuilder2=new BoolQueryBuilder();
	    	for(String c:categoryArray){
	    		boolQueryBuilder2.should(new TermQueryBuilder(IndexFieldConstants.CATEGORYID_SEARCH,c));
	    	}
	    	boolQueryBuilder.must(boolQueryBuilder2);
	    }

	    if(StringUtils.isNotEmpty(brand)){
	    	String[] brandArray=brand.split(";");
	    	BoolQueryBuilder boolQueryBuilder3=new BoolQueryBuilder();
	    	for(String b:brandArray){
	    		boolQueryBuilder3.should(new TermQueryBuilder(IndexFieldConstants.BRANDID_SEARCH,b));
	    	}
	    	boolQueryBuilder.must(boolQueryBuilder3);
	    }
	    if(companyId!=null){
	    	boolQueryBuilder.must(new TermQueryBuilder(IndexFieldConstants.COMPANYID,companyId));
	    }
	    QueryStringQueryBuilder queryBuilder=new QueryStringQueryBuilder(query_str.toString());
	    ESSearchRequest esSearchRequest=new ESSearchRequest(IndexNameManager.getIndexName(), IndexConstants.index_type, queryBuilder);
	    esSearchRequest.setStart(0);
	    esSearchRequest.setCount(Integer.MAX_VALUE);
	    org.elasticsearch.action.search.SearchResponse esSearchResponse=ESService.search(esSearchRequest);
	    List<Long> merchantProductIds=new ArrayList<Long>();
	    searchResponse.setMerchantProductIds(merchantProductIds);
	    SearchHits searchHits=esSearchResponse.getHits();
	    searchResponse.setTotalHit(searchHits.getTotalHits());
	    SearchHit[] searchHitArray=searchHits.getHits();
	    for(SearchHit hit:searchHitArray){
	    	merchantProductIds.add(Long.valueOf(hit.getId()));
	    }
	    return GsonUtil.getGson().toJson(searchResponse);
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/reloadCache.json")
	public String reloadCache(@RequestParam(value = "name",required = false) String name,
			@RequestParam(value = "reloadAll",required = false,defaultValue="false") Boolean reloadAll,
			@RequestParam(value = "companyId",required = true) int companyId) throws Exception{
		if(reloadAll != null && reloadAll == true){
			CompanyDBCacheManager.instance.reloadAll();
		}else{
			if(StringUtils.isNotEmpty(name)){
				String[] names = name.split(",");
				for(String dbName : names){
					CompanyDBCacheManager.instance.reload(dbName,companyId);
				}
			}
		}
		return HttpClientUtil.http_resp_successful;
	}

	/**
	 * 根据一个地址定位查询覆盖该地址的商家列表
	 * @param address
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/locationMerchantByAddress")
	public List<Merchant> locationMerchantByAddress(@RequestParam(value = "address",required = true) String address,
			@RequestParam(value = "companyId",required = false, defaultValue="4") int companyId) throws Exception {
		if(StringUtils.isBlank(address)){
			throw new Exception("address is blank");
		}
		GeoSearchRequest geoSearchRequest=new GeoSearchRequest(address,companyId);
		GeoSearchService geoSearchService = SearchClient.getGeoSearchService(CLIENT_NAME);

		GeoSearchResponse geoSearchResponse=geoSearchService.search(geoSearchRequest);
		return geoSearchResponse.getMerchants();
	}
	
	/**
	 * 根据一个经纬度查询覆盖该地址的商家列表
	 * @param longitude
	 * @param latitude
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/locationMerchantByPoint")
	public List<Merchant> locationMerchantByPoint(@RequestParam(value = "longitude",required = true) double longitude,
			@RequestParam(value = "latitude",required = true) double latitude,
			@RequestParam(value = "companyId",required = false, defaultValue="4") int companyId) throws Exception {
		GeoSearchRequest geoSearchRequest=new GeoSearchRequest(new Point(longitude,latitude),companyId);
		GeoSearchService geoSearchService = SearchClient.getGeoSearchService(CLIENT_NAME);

		GeoSearchResponse geoSearchResponse=geoSearchService.search(geoSearchRequest);
		return geoSearchResponse.getMerchants();
	}

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/enableZeroResult")
    public String enableZeroResult() throws Exception{
        searchHandler.setEnableZeroResult(true);
        return "enableZeroResult=" + searchHandler.isEnableZeroResult();
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/disableZeroResult")
    public String disableZeroResult() throws Exception{
        searchHandler.setEnableZeroResult(false);
        return "enableZeroResult=" + searchHandler.isEnableZeroResult();
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/enableHotWordRecomend")
    public String enableHotWordRecomend() throws Exception{
        searchHandler.setEnableHotWordRecomend(true);
        return "enableHotWordRecomend=" + searchHandler.isEnableHotWordRecomend();
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/disableHotWordRecomend")
    public String disableHotWordRecomend() throws Exception{
        searchHandler.setEnableHotWordRecomend(false);
        return "enableHotWordRecomend=" + searchHandler.isEnableHotWordRecomend();
    }

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/openCache")
	public String openCache() throws Exception{
		SearchCacheImpl.instance.openCache();
		return HttpClientUtil.http_resp_successful;
	}


	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/closeCache")
	public String closeCache() throws Exception{
		SearchCacheImpl.instance.closeCache();
		return HttpClientUtil.http_resp_successful;
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/cacheStats")
	public String cacheStats() throws Exception{
		return SearchCacheImpl.instance.getCacheStats().toString();
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/removeCache")
	public String removeCache(String key) throws Exception{
		SearchCacheImpl.instance.remove(key);
		return HttpClientUtil.http_resp_successful;
	}

//	@Autowired
//	private SelectionProductSearchService selectionProductSearchService;
//	@ResponseBody
//	@RequestMapping(method = RequestMethod.GET, value = "/selectionSearch2")
//	public String selectionSearch2() throws Exception{
//		SelectionProductSearchRequest searchRequest = new SelectionProductSearchRequest(51);
//		searchRequest.setCombine(true);
//		searchRequest.setDistributionMp(false);
//		searchRequest.setManagementState(ManagementType.ON_SHELF);
//		searchRequest.setCount(10);
//		return selectionProductSearchService.selectionSearch2(searchRequest).toString();
//	}

	private final static String keywordAll="*****";
	private static final int DEFAULT_FREQ = 1;

	private void logSearchHistory(BaseSearchRequest searchRequest, SearchResponse baseResponse){
		logSearchHistory(searchRequest,baseResponse, HistoryType.SEARCH);
	}

	private void logSearchHistory(BaseSearchRequest searchRequest, SearchResponse baseResponse, HistoryType historyType) {

		/*if(historyLogService == null || baseResponse == null) {
             return;
		}*/

		if (StringUtils.isBlank(searchRequest.getKeyword()) || searchRequest.getKeyword().equals(keywordAll) ) {
			return;
		}
		try{
			final HistoryWriteRequest request = new HistoryWriteRequest(searchRequest.getCompanyId(), searchRequest.getUserId(), searchRequest.getKeyword().trim());
			request.setMerchantId(searchRequest.getMerchantId());
			request.setFrequency(DEFAULT_FREQ);
			request.setType(historyType);
			int total = (int) baseResponse.getTotalHit();
			request.setResultCount(total);
			SearchHistorySender.sendHistory(request);

		}catch(Throwable e){
			logger.warn(e.getMessage());
		}
	}

}
