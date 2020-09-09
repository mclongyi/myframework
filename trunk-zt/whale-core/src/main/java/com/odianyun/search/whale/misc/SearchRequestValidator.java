package com.odianyun.search.whale.misc;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.odianyun.search.whale.api.common.SearchException;
import com.odianyun.search.whale.api.model.req.SearchRequest;
import com.odianyun.search.whale.manager.IKSegmentManager;

public class SearchRequestValidator {
	
	static Logger logger = Logger.getLogger(SearchRequestValidator.class);
	
	private static final int MAX_KEYWORD_LENGTH = 50;
	
	public static final String special_keyword="*****";
	
	
	public static boolean validate(SearchRequest searchRequest) throws SearchException{
		try{
			String keyword = searchRequest.getKeyword();
	     	if(StringUtils.isNotBlank(keyword)){
	     		if(keyword.length() > MAX_KEYWORD_LENGTH){
	     			keyword = keyword.substring(0, MAX_KEYWORD_LENGTH);
	     			searchRequest.setKeyword(keyword);
	     		}
	     	}
			if(StringUtils.isBlank(searchRequest.getKeyword()) &&
					CollectionUtils.isEmpty(searchRequest.getCategoryIds())
					&& CollectionUtils.isEmpty(searchRequest.getNavCategoryIds())
					&& CollectionUtils.isEmpty(searchRequest.getBrandIds())
					&& CollectionUtils.isEmpty(searchRequest.getPromotionIdList())
					&& CollectionUtils.isEmpty(searchRequest.getPromotionTypeList())
					&& CollectionUtils.isEmpty(searchRequest.getEanNos())
					&& CollectionUtils.isEmpty(searchRequest.getTypes())){
				Integer type = searchRequest.getType();
				if(null == type || type == 1 || type == 0){
					logger.error("searchRequest keyword and NavCategoryIds is null");
					return false;
				}
				
			}
		
			if(StringUtils.isNotBlank(searchRequest.getKeyword())){

				if(special_keyword.equals(searchRequest.getKeyword())){
					searchRequest.setAggregation(false);
					searchRequest.setZeroResponseHandler(false);
					searchRequest.setRecommendWordsHandler(false);
					return true;
				}
				List<String> words=IKSegmentManager.segment(searchRequest.getKeyword());
				if(CollectionUtils.isEmpty(words)){
					logger.error(searchRequest.getKeyword() +" is Illegal character");
					return false;
				}
			}
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			return false;
		}
		return true;
	}

}
