package com.odianyun.search.whale.api.service;

import com.odianyun.search.whale.api.common.PointConstants;
import com.odianyun.search.whale.api.common.SearchException;
import com.odianyun.search.whale.api.model.MerchantProduct;
import com.odianyun.search.whale.api.model.SearchResponse;
import com.odianyun.search.whale.api.model.geo.Point;
import com.odianyun.search.whale.api.model.req.*;
import com.odianyun.search.whale.api.model.resp.*;
import com.odianyun.search.whale.api.model.series.SeriesRequest;
import com.odianyun.search.whale.api.model.series.SeriesResponse;
import com.odianyun.search.whale.index.api.common.SearchHistorySender;
import com.odianyun.search.whale.index.api.model.req.HistoryType;
import com.odianyun.search.whale.index.api.model.req.HistoryWriteRequest;
import com.odianyun.soa.InputDTO;
import com.odianyun.soa.OutputDTO;
import com.odianyun.soa.SoaUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;

public class SearchCacheService implements SearchService{
	private static final Logger LOGGER = Logger.getLogger(SearchCacheService.class);

	private SearchService searchService;
	private String clientName;
	private String IP;

	private static final int DEFAULT_FREQ = 1;
	
	private static final int MAX_KEYWORD_LENGTH = 50;
	
//	private HistoryLogService historyLogService;

	private final static String keywordAll="*****";

	private static int batchMaxSize=200;

	private static int batchParamSize=10;


	public SearchCacheService(SearchService searchService, String clientName){
		this.searchService = searchService;
		this.clientName = clientName;
	}
	/*public SearchCacheService(SearchService searchService, String clientName){
		this(searchService,null,clientName);
	}
	
	public SearchCacheService(SearchService searchService, HistoryLogService historyLogService, String clientName){
		this.searchService = searchService;
		this.historyLogService = historyLogService;
		this.clientName = clientName;
		//this.IP = NetUtil.getLocalIP();
	}*/
	
	private void setRequestCommonData(BaseSearchRequest request) {
		request.getClientInfo().setPoolName(this.clientName);
	}
	

	@Override
	public SearchResponse search(SearchRequest searchRequest) throws SearchException {
		validateSearchRequest(searchRequest);
		setRequestCommonData(searchRequest);
		SearchResponse searchResponse = searchService.search(searchRequest);
		logSearchHistory(searchRequest,searchResponse);
		return searchResponse;
	}

	@Override
	public HotSearchResponse hotSearch(HotSearchRequest searchRequest) throws SearchException {
		List<Long> merchantIdList = searchRequest.getMerchantIdList();
		if(CollectionUtils.isNotEmpty(merchantIdList) && merchantIdList.size() > 200){
			throw new SearchException("merchantIdList's size is more than  200 !!!!");
		}
		if(null == searchRequest.getPoint()){
			throw new SearchException("Address and Point is null ");
		}
		Point point = searchRequest.getPoint();
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

		return searchService.hotSearch(searchRequest);
	}

	@Override
	public SearchResponse searchProducts(SearchRequest searchRequest) throws SearchException {
		validateSearchRequest(searchRequest);
		setRequestCommonData(searchRequest);
		SearchResponse searchResponse = searchService.searchProducts(searchRequest);
		logSearchHistory(searchRequest,searchResponse);
		return searchResponse;
	}


	@Override
	public SearchResponse shopSearch(ShopSearchRequest shopRequest) throws SearchException {
		validateShopRequest(shopRequest);
		setRequestCommonData(shopRequest);
		SearchResponse searchResponse = searchService.shopSearch(shopRequest);
		logSearchHistory(shopRequest,searchResponse);
		return searchResponse;

	}
	
	private void validateShopRequest(ShopSearchRequest shopRequest) {
		if(shopRequest.getCompanyId()==null){
     		throw new SearchException("companyId is null");
     	}
		if(CollectionUtils.isEmpty(shopRequest.getMerchantIdList())){
			throw new SearchException("merchantId is null or 0");
		}
	}

	private void validateSearchRequest(SearchRequest searchRequest) {
     	if(searchRequest.getCompanyId()==null){
     		throw new SearchException("companyId is null");
     	}
     	if(CollectionUtils.isNotEmpty(searchRequest.getExcludeMpIds()) && searchRequest.getExcludeMpIds().size() > 50){
     		throw new SearchException("excludeMpIds' size is more than  50 !!!!");
     	}
		if(CollectionUtils.isNotEmpty(searchRequest.getPromotionIdList()) && searchRequest.getPromotionIdList().size() > batchParamSize){
			throw new SearchException("promotionIdList' size is more than "+batchParamSize +"!!!!");
		}

     	String keyword = searchRequest.getKeyword();
     	if(StringUtils.isNotBlank(keyword)){
     		if(keyword.length() > MAX_KEYWORD_LENGTH){
     			keyword = keyword.substring(0, MAX_KEYWORD_LENGTH);
     			searchRequest.setKeyword(keyword);
     		}
     	}
		if(StringUtils.isBlank(searchRequest.getKeyword())
				&& CollectionUtils.isEmpty(searchRequest.getCategoryIds())
				&& CollectionUtils.isEmpty(searchRequest.getNavCategoryIds())
				&& CollectionUtils.isEmpty(searchRequest.getBrandIds())
				&& CollectionUtils.isEmpty(searchRequest.getPromotionIdList())
				&& CollectionUtils.isEmpty(searchRequest.getEanNos())
				&& CollectionUtils.isEmpty(searchRequest.getPromotionTypeList())
				&& CollectionUtils.isEmpty(searchRequest.getTypes())){
			// 以上条件都为空的时候   如果merchantProductType为空或者是普通商品 抛异常 否则 放行！！！
			/*MerchantProductType merchantProductType = searchRequest.getMerchantProductType();
			if(null == merchantProductType || MerchantProductType.NORMAL.equals(merchantProductType) ){
				throw new SearchException("searchRequest keyword and NavCategoryIds and categoryIds and BrandIds is null");
			}*/
			Integer type = searchRequest.getType();
			//普通商品或者没有指定  type=1是普通商品
			if(null == type || 1 == type || 0 == type){
				throw new SearchException("searchRequest keyword and NavCategoryIds and categoryIds and BrandIds is null");
			}
		}


	}

	@Override
	public Map<Long, MerchantProduct> searchById(List<Long> mpIds,Integer companyId)
			throws SearchException {
		if(companyId==null){
			throw new SearchException("companyId is null");
		}
		if(CollectionUtils.isEmpty(mpIds)){
			throw new SearchException("mpIds is empty");
		}
		if(mpIds.size()>batchMaxSize){
			throw new SearchException("mpIds size more than " +batchMaxSize );
		}
		return searchService.searchById(mpIds,companyId);
	}

	@Override
	public Map<Long, MerchantProduct> searchByIdNew(SearchByIdRequest searchByIdRequest)
			throws SearchException {
		List<Long> mpIds = searchByIdRequest.getMpIds();
		Integer companyId = searchByIdRequest.getCompanyId();
		if(companyId==null){
			throw new SearchException("companyId is null");
		}
		if(CollectionUtils.isEmpty(mpIds)){
			throw new SearchException("mpIds is empty");
		}
		if(mpIds.size()>batchMaxSize){
			throw new SearchException("mpIds size more than " +batchMaxSize );
		}
		return searchService.searchByIdNew(searchByIdRequest);
	}

	@Override
	public Map<String, MerchantProduct> searchByMpCodes(List<String> mpCodes, Integer companyId) {
		if(companyId==null){
			throw new SearchException("companyId is null");
		}
		if(CollectionUtils.isEmpty(mpCodes)){
			throw new SearchException("mpCodes is empty");
		}
		if(mpCodes.size()>batchMaxSize){
			throw new SearchException("mpCodes size more than " +batchMaxSize );
		}
		return searchService.searchByMpCodes(mpCodes,companyId);
	}

	@Override
	public SeriesResponse searchBySeriesRequest(SeriesRequest seriesRequest)
			throws SearchException {
		if(seriesRequest.getCompanyId()==null){
     		throw new SearchException("companyId is null");
     	}
		return searchService.searchBySeriesRequest(seriesRequest);
	}

	@Override
	public PromotionSearchResponse promotionSearch(PromotionSearchRequest searchRequest) throws SearchException {
		if(searchRequest.getCompanyId() == null){
			throw new SearchException("companyId is null");
		}
		if(searchRequest.getBrandId() == null || CollectionUtils.isEmpty(searchRequest.getPromotionTypeList())){
			throw new SearchException("brandId is null || promotionType is null");
		}
		return searchService.promotionSearch(searchRequest);
	}

	@Override
	public PromotionTypeSearchResponse promotionTypeSearch(PromotionTypeSearchRequest searchRequest) throws SearchException {
		if(searchRequest.getCompanyId() == null){
			throw new SearchException("companyId is null");
		}
		if(searchRequest.getBrandId() == null){
			throw new SearchException("brandId is null ");
		}
		return searchService.promotionTypeSearch(searchRequest);
	}

	@Override
	public SearchResponse pointMpSearch(PointSearchRequest searchRequest) throws SearchException {
		validateSearchRequest(searchRequest);
		setRequestCommonData(searchRequest);
		SearchResponse searchResponse = searchService.pointMpSearch(searchRequest);
		logSearchHistory(searchRequest,searchResponse,HistoryType.POINT);
		return searchResponse;
	}


	//====================================包装成标准soa模式===================================================

	@Override
	public OutputDTO<SearchResponse> searchStandard(InputDTO<SearchRequest> inputDTO) throws SearchException {
//		LOGGER.info("soa 调用入参 ,"+inputDTO.getData());

		SearchResponse search;
		try {
			search = search(inputDTO.getData());
		} catch (Exception e) {
			LOGGER.error("soa fail {}", e);
			return SoaUtil.resultError(e.getMessage());
		}
//		LOGGER.info("soa 调用出参 ,"+search);
		return SoaUtil.resultSucess(search);
	}

	@Override
	public OutputDTO<HotSearchResponse> hotSearchStandard(InputDTO<HotSearchRequest> inputDTO) throws SearchException {
//		LOGGER.info("soa 调用入参 ,"+inputDTO.getData());

		HotSearchResponse response;
		try {
			response = hotSearch(inputDTO.getData());
		} catch (Exception e) {
			LOGGER.error("soa fail {}", e);
			return SoaUtil.resultError(e.getMessage());
		}
//		LOGGER.info("soa 调用出参 ,"+response);
		return SoaUtil.resultSucess(response);
	}

	@Override
	public OutputDTO<SearchResponse> searchProductsStandard(InputDTO<SearchRequest> inputDTO) throws SearchException {
//		LOGGER.info("soa 调用入参 ,"+inputDTO.getData());

		SearchResponse response;
		try {
			response = searchProducts(inputDTO.getData());
		} catch (Exception e) {
			LOGGER.error("soa fail {}", e);
			return SoaUtil.resultError(e.getMessage());
		}
//		LOGGER.info("soa 调用出参 ,"+response);
		return SoaUtil.resultSucess(response);
	}

	@Override
	public OutputDTO<SearchResponse> shopSearchStandard(InputDTO<ShopSearchRequest> inputDTO) throws SearchException {
//		LOGGER.info("soa 调用入参 ,"+inputDTO.getData());

		SearchResponse response;
		try {
			response = shopSearch(inputDTO.getData());
		} catch (Exception e) {
			LOGGER.error("soa fail {}", e);
			return SoaUtil.resultError(e.getMessage());
		}
//		LOGGER.info("soa 调用出参 ,"+response);
		return SoaUtil.resultSucess(response);
	}

	@Override
	public OutputDTO<SearchByIdResponse> searchByIdStandard(InputDTO<SearchByIdRequest> inputDTO) throws SearchException {
//		LOGGER.info("soa 调用入参 ,"+inputDTO.getData());
		SearchByIdResponse searchByIdResponse = new SearchByIdResponse();

		Map<Long, MerchantProduct> merchantProductMap = null;
		try {
			SearchByIdRequest inputDTOData = inputDTO.getData();
			if (inputDTOData!=null){
				merchantProductMap = searchById(inputDTOData.getMpIds(),30);
			}
		} catch (Exception e) {
			LOGGER.error("soa fail {}", e);
			return SoaUtil.resultError(e.getMessage());
		}
//		LOGGER.info("soa 调用出参 ,"+merchantProductMap);

		searchByIdResponse.setMerchantProducts(merchantProductMap);
		return SoaUtil.resultSucess(searchByIdResponse);
	}

	@Override
	public OutputDTO<SearchByIdResponse> searchByIdNewStandard(InputDTO<SearchByIdRequest> inputDTO) throws SearchException {
//		LOGGER.info("soa 调用入参 ,"+inputDTO.getData());
		SearchByIdResponse searchByIdResponse = new SearchByIdResponse();

		Map<Long, MerchantProduct> merchantProductMap;
		try {
			merchantProductMap = searchByIdNew(inputDTO.getData());
		} catch (Exception e) {
			LOGGER.error("soa fail {}", e);
			return SoaUtil.resultError(e.getMessage());
		}
//		LOGGER.info("soa 调用出参 ,"+merchantProductMap);

		searchByIdResponse.setMerchantProducts(merchantProductMap);
		return SoaUtil.resultSucess(searchByIdResponse);
	}

	@Override
	public OutputDTO<SearchByCodeResponse> searchByMpCodesStandard(InputDTO<SearchByCodeRequest> inputDTO) {
//		LOGGER.info("soa 调用入参 ,"+inputDTO.getData());
		SearchByCodeResponse response = new SearchByCodeResponse();

		Map<String, MerchantProduct> searchByMpCodes;
		try {
			SearchByCodeRequest data = inputDTO.getData();
			searchByMpCodes = searchByMpCodes(data.getMpCodes(), data.getCompanyId());
		} catch (Exception e) {
			LOGGER.error("soa fail {}", e);
			return SoaUtil.resultError(e.getMessage());
		}
//		LOGGER.info("soa 调用出参 ,"+searchByMpCodes);
		response.setMerchantProducts(searchByMpCodes);
		return SoaUtil.resultSucess(response);
	}

	@Override
	public OutputDTO<SeriesResponse> searchBySeriesRequestStandard(InputDTO<SeriesRequest> inputDTO) throws SearchException {
//		LOGGER.info("soa 调用入参 ,"+inputDTO.getData());
		SeriesResponse response;
		try {
			response = searchBySeriesRequest(inputDTO.getData());
		} catch (Exception e) {
			LOGGER.error("soa fail {}", e);
			return SoaUtil.resultError(e.getMessage());
		}
//		LOGGER.info("soa 调用出参 ,"+response);
		return SoaUtil.resultSucess(response);
	}

	@Override
	public OutputDTO<PromotionSearchResponse> promotionSearchStandard(InputDTO<PromotionSearchRequest> inputDTO) throws SearchException {
//		LOGGER.info("soa 调用入参 ,"+inputDTO.getData());
		PromotionSearchResponse response;
		try {
			response = promotionSearch(inputDTO.getData());
		} catch (Exception e) {
			LOGGER.error("soa fail {}", e);
			return SoaUtil.resultError(e.getMessage());
		}
//		LOGGER.info("soa 调用出参 ,"+response);
		return SoaUtil.resultSucess(response);
	}

	@Override
	public OutputDTO<PromotionTypeSearchResponse> promotionTypeSearchStandard(InputDTO<PromotionTypeSearchRequest> inputDTO) throws SearchException {
//		LOGGER.info("soa 调用入参 ,"+inputDTO.getData());
		PromotionTypeSearchResponse response;
		try {
			response = promotionTypeSearch(inputDTO.getData());
		} catch (Exception e) {
			LOGGER.error("soa fail {}", e);
			return SoaUtil.resultError(e.getMessage());
		}
//		LOGGER.info("soa 调用出参 ,"+response);
		return SoaUtil.resultSucess(response);
	}

	@Override
	public OutputDTO<SearchResponse> pointMpSearchStandard(InputDTO<PointSearchRequest> inputDTO) throws SearchException {
//		LOGGER.info("soa 调用入参 ,"+inputDTO.getData());

		SearchResponse search;
		try {
			search = pointMpSearch(inputDTO.getData());
		} catch (Exception e) {
			LOGGER.error("soa fail {}", e);
			return SoaUtil.resultError(e.getMessage());
		}
//		LOGGER.info("soa 调用出参 ,"+search);
		return SoaUtil.resultSucess(search);
	}


	private void logSearchHistory(BaseSearchRequest searchRequest, SearchResponse baseResponse){
		if (searchRequest.getMerchantId() != null && searchRequest.getMerchantId() != 0) {
			logSearchHistory(searchRequest,baseResponse,HistoryType.MERCHANT);
		} else {
			logSearchHistory(searchRequest,baseResponse,HistoryType.SEARCH);
		}
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
			LOGGER.warn(e.getMessage());
		}
	}

}
