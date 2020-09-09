package com.odianyun.search.whale.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.odianyun.search.whale.api.model.req.BaseSearchRequest;
import com.odianyun.search.whale.index.api.common.SearchHistorySender;
import com.odianyun.search.whale.index.api.model.req.HistoryType;
import com.odianyun.search.whale.index.api.model.req.HistoryWriteRequest;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.odianyun.search.whale.api.model.SearchResponse;
import com.odianyun.search.whale.api.model.req.ShopSearchRequest;
import com.odianyun.search.whale.api.model.req.SortType;
import com.odianyun.search.whale.api.service.SearchService;
import com.odianyun.search.whale.common.util.GsonUtil;
import com.odianyun.search.whale.index.api.common.IndexFieldConstants;

@Controller
public class RestShopSearchService {
	
	static Logger logger = Logger.getLogger(RestShopSearchService.class);
	
	@Autowired
	SearchService searchService;

	private final static String keywordAll="*****";
	private static final int DEFAULT_FREQ = 1;


	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/shopproduct.json")
	public String search(String keyword, String categoryIdSearch, String attrValueId_search,
								 Long merchantId, String brandIdSearch, String coverProvinceId, String sortSpec, Integer start, Integer count, Integer companyId, Long userId) throws Exception{
		ShopSearchRequest searchRequest=new ShopSearchRequest(companyId,merchantId);
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
	    	searchRequest.setMerchantCategoryIds(cids);   	
	    }
	    if(StringUtils.isNotEmpty(attrValueId_search)){
	    	searchRequest.setAttrValueIds(parseLong(attrValueId_search));
	    }
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
	    	
	    }
	    if(companyId!=null){
	    	searchRequest.setCompanyId(companyId);
	    }
		if (userId != null) {
			searchRequest.setUserId(userId.toString());
		}
	    SearchResponse searchResponse=searchService.shopSearch(searchRequest);
		logSearchHistory(searchRequest, searchResponse);
		return GsonUtil.getGson().toJson(searchResponse);
	}

	private void logSearchHistory(BaseSearchRequest searchRequest, SearchResponse baseResponse){
		logSearchHistory(searchRequest,baseResponse, HistoryType.MERCHANT);
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
}
