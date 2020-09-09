package com.odianyun.search.whale.req.builder;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;

import com.odianyun.search.whale.api.model.req.BaseSearchRequest;
import com.odianyun.search.whale.api.model.req.SearchRequest;
import com.odianyun.search.whale.es.request.ESSearchRequest;
import com.odianyun.search.whale.index.api.common.IndexFieldConstants;

public class SearchQueryStrBuilder extends BaseQueryStrBuilder {
	private RequestScoreBuilder requestScoreBuilder;
	@Override
	protected void buildOtherQuery(BoolQueryBuilder boolQueryBuilder,BoolQueryBuilder aggregationBoolQueryBuilder,
			BaseSearchRequest baseSearchRequest) {
		SearchRequest searchRequest =(SearchRequest) baseSearchRequest;
		List<Long> navCategoryIds=searchRequest.getNavCategoryIds();
		if(CollectionUtils.isNotEmpty(navCategoryIds)){
			BoolQueryBuilder navCategoryIdQueryBuilder=new BoolQueryBuilder();
			for(Long navCategoryId:navCategoryIds){
				navCategoryIdQueryBuilder.should(new TermQueryBuilder(IndexFieldConstants.NAVCATEGORYID_SEARCH,String.valueOf(navCategoryId)));
			}
			boolQueryBuilder.must(navCategoryIdQueryBuilder);
			aggregationBoolQueryBuilder.must(navCategoryIdQueryBuilder);
		}
	}

	@Override
	protected void calcAggregationBoolQuery(ESSearchRequest esSearchRequest, BaseSearchRequest baseSearchRequest,BoolQueryBuilder aggregationBoolQueryBuilder){
		//aggregationBoolQueryBuilder 和 searchQueryBuilder 逻辑不一样
		boolean isOpenAggregationBoolQuery = configService.getBool("is_open_aggregation_query",false,baseSearchRequest.getCompanyId());
		SearchRequest searchRequest =(SearchRequest) baseSearchRequest;
		if(isOpenAggregationBoolQuery){
			if(StringUtils.isNotBlank(searchRequest.getKeyword()) || CollectionUtils.isNotEmpty(searchRequest.getNavCategoryIds())){
				esSearchRequest.setAggregationQueryBuilder(aggregationBoolQueryBuilder);
			}
		}

	}

	@Override
	public void build(ESSearchRequest esSearchRequest, BaseSearchRequest searchRequest) {
		super.build(esSearchRequest, searchRequest);
		this.requestScoreBuilder.alterScoreBySingleTokenForCate(esSearchRequest,searchRequest);
	}

	public RequestScoreBuilder getRequestScoreBuilder() {
		return requestScoreBuilder;
	}

	public void setRequestScoreBuilder(RequestScoreBuilder requestScoreBuilder) {
		this.requestScoreBuilder = requestScoreBuilder;
	}

}
