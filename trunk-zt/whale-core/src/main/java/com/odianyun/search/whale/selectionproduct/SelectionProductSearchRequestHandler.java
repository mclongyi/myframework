package com.odianyun.search.whale.selectionproduct;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.odianyun.search.whale.req.builder.CombineQueryBuilder;
import com.odianyun.search.whale.server.WhaleServer;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;

import com.odianyun.search.whale.api.model.ManagementType;
import com.odianyun.search.whale.api.model.PriceRange;
import com.odianyun.search.whale.api.model.req.SortType;
import com.odianyun.search.whale.api.model.selectionproduct.SelectionProductSearchRequest;
import com.odianyun.search.whale.common.RequestFieldsBuilder;
import com.odianyun.search.whale.common.query.KeywordQueryBuilder;
import com.odianyun.search.whale.es.request.ESSearchRequest;
import com.odianyun.search.whale.index.api.common.IndexConstants;
import com.odianyun.search.whale.index.api.common.IndexFieldConstants;
import com.odianyun.search.whale.req.builder.BaseQueryStrBuilder;

public class SelectionProductSearchRequestHandler {
	static Logger logger = Logger.getLogger(SelectionProductSearchRequestHandler.class);
	
	public static ESSearchRequest handle(SelectionProductSearchRequest selectionProductSearchRequest,String indexAliasName){
		ESSearchRequest esSearchRequest=new ESSearchRequest(indexAliasName, IndexConstants.index_type);
		esSearchRequest.setMaxStart(20000);
		BoolQueryBuilder boolQueryBuilder=new BoolQueryBuilder();
		Integer companyId=selectionProductSearchRequest.getCompanyId();
		if(companyId != null && !companyId.equals(0)){
			TermQueryBuilder termQuery= new TermQueryBuilder(IndexFieldConstants.COMPANYID,companyId);
			boolQueryBuilder.must(termQuery);
		}
		ManagementType managementType = selectionProductSearchRequest.getManagementState();
		if(!ManagementType.VERIFIED.equals(managementType)){
			boolQueryBuilder.must(new TermQueryBuilder(IndexFieldConstants.MANAGEMENT_STATE, managementType.getCode()));
		}
		String keyword=selectionProductSearchRequest.getKeyword();
		if(StringUtils.isNotBlank(keyword)){
			boolQueryBuilder.must(KeywordQueryBuilder.buildKeywordQuery(keyword));
		}
		List<Long> categoryIds=selectionProductSearchRequest.getCategoryIds();
		if(CollectionUtils.isNotEmpty(categoryIds)){
			BoolQueryBuilder categoryIdBoolQueryBuilder=new BoolQueryBuilder();
			for(Long categoryId:categoryIds){
				categoryIdBoolQueryBuilder.should(new TermQueryBuilder(IndexFieldConstants.CATEGORYID_SEARCH,categoryId));
			}
			boolQueryBuilder.must(categoryIdBoolQueryBuilder);
		}
		PriceRange priceRange=selectionProductSearchRequest.getPriceRange();
		if(priceRange!=null && (priceRange.getMinPrice()!=null || priceRange.getMaxPrice()!=null)){
			RangeQueryBuilder rangeQueryBuilder=new RangeQueryBuilder(IndexFieldConstants.PRICE);
			if(priceRange.getMinPrice()!=null){
				rangeQueryBuilder.from(priceRange.getMinPrice());
			}
			if(priceRange.getMaxPrice()!=null){
				rangeQueryBuilder.to(priceRange.getMaxPrice());
			}
			boolQueryBuilder.must(rangeQueryBuilder);
		}
		List<String> areaCodes=selectionProductSearchRequest.getAreaCodes();
		if(CollectionUtils.isNotEmpty(areaCodes)){
			BoolQueryBuilder areaCodesBoolQueryBuilder=new BoolQueryBuilder();
			for(String areaCode:areaCodes){
				TermQueryBuilder areaCodeTermQuery= new TermQueryBuilder(IndexFieldConstants.AREACODE_BELONG,areaCode);
				areaCodesBoolQueryBuilder.should(areaCodeTermQuery);
			}
			boolQueryBuilder.must(areaCodesBoolQueryBuilder);
		}
		List<Long> brandIds=selectionProductSearchRequest.getBrandIds();
		if(CollectionUtils.isNotEmpty(brandIds)){
			BoolQueryBuilder brandIdsBoolQueryBuilder=new BoolQueryBuilder();
			for(Long brandId:brandIds){
				TermQueryBuilder brandIdTermQuery= new TermQueryBuilder(IndexFieldConstants.BRANDID_SEARCH,brandId);
				brandIdsBoolQueryBuilder.should(brandIdTermQuery);
			}
			boolQueryBuilder.must(brandIdsBoolQueryBuilder);
		}
		List<Long> merchantIds=selectionProductSearchRequest.getMerchantIds();
		if(CollectionUtils.isNotEmpty(merchantIds)){
			BoolQueryBuilder merchantIdsBoolQueryBuilder=new BoolQueryBuilder();
			for(Long merchantId:merchantIds){
				TermQueryBuilder merchantIdTermQuery= new TermQueryBuilder(IndexFieldConstants.MERCHANTID,merchantId);
				merchantIdsBoolQueryBuilder.should(merchantIdTermQuery);
			}
			boolQueryBuilder.must(merchantIdsBoolQueryBuilder);
		}
		List<String> codes = selectionProductSearchRequest.getCodes();
		if(CollectionUtils.isNotEmpty(codes)){
			BoolQueryBuilder codesBoolQueryBuilder = new BoolQueryBuilder();
			for(String code : codes){
				TermQueryBuilder codeTermQuery = new TermQueryBuilder(IndexFieldConstants.CODE,code);
				codesBoolQueryBuilder.should(codeTermQuery);
			}
			boolQueryBuilder.must(codesBoolQueryBuilder);
		}
		String merchantName = selectionProductSearchRequest.getMerchantName();
		if(StringUtils.isNotBlank(merchantName)){
			boolQueryBuilder.must(KeywordQueryBuilder.buildKeywordQueryForMerchantName(merchantName));
		}
		List<Long> refIds = selectionProductSearchRequest.getRefIds();
		if(CollectionUtils.isNotEmpty(refIds)) {
			BoolQueryBuilder refIdBoolQueryBuilder = new BoolQueryBuilder();
			for (Long id : refIds) {
				refIdBoolQueryBuilder.should(new TermQueryBuilder(IndexFieldConstants.REFID_SEARCH, id));
			}
			boolQueryBuilder.must(refIdBoolQueryBuilder);
		}
		boolQueryBuilder.must(CombineQueryBuilder.buildCombineQuery(selectionProductSearchRequest.isCombine()));

		List<String> productCodes=selectionProductSearchRequest.getProductCodes();
		if(CollectionUtils.isNotEmpty(productCodes)){
			BoolQueryBuilder productCodesBoolQueryBuilder = new BoolQueryBuilder();
			for(String productCode : productCodes){
				TermQueryBuilder codeTermQuery = new TermQueryBuilder(IndexFieldConstants.PRODUCT_CODE,productCode);
				productCodesBoolQueryBuilder.should(codeTermQuery);
			}
			boolQueryBuilder.must(productCodesBoolQueryBuilder);
		}
		Long parentMerchantId = selectionProductSearchRequest.getParentMerchantId();
		if(parentMerchantId!=null){
			boolQueryBuilder.must(new TermQueryBuilder(IndexFieldConstants.PARENT_MERCHANT_ID,parentMerchantId));
		}
		List<Long> subMerchantIds = selectionProductSearchRequest.getSubMerchantIds();
		if(CollectionUtils.isNotEmpty(subMerchantIds)){
			BoolQueryBuilder subMerchantIdBoolQueryBuilder = new BoolQueryBuilder();
			for(Long id : subMerchantIds){
				subMerchantIdBoolQueryBuilder.should(new TermQueryBuilder(IndexFieldConstants.SUB_MERCHANT_IDS,id));
			}
			boolQueryBuilder.must(subMerchantIdBoolQueryBuilder);
		}
		List<String> thirdCodes = selectionProductSearchRequest.getThirdCodes();
		logger.info("-------------------------thirdCodes:"+ JSONObject.toJSONString(thirdCodes));
		if(CollectionUtils.isNotEmpty(thirdCodes)){
			BoolQueryBuilder thirdCodeBoolQueryBuilder = new BoolQueryBuilder();
			for(String code:thirdCodes){
				thirdCodeBoolQueryBuilder.should(new TermQueryBuilder(IndexFieldConstants.THIRD_CODE,code));
			}
			boolQueryBuilder.must(thirdCodeBoolQueryBuilder);
			logger.info("-------------------------thirdCodeBoolQueryBuilder:"+ JSONObject.toJSONString(thirdCodeBoolQueryBuilder));
		}
		Boolean isDistributionMp=selectionProductSearchRequest.getDistributionMp();
		if(isDistributionMp!=null && isDistributionMp==true){
			boolQueryBuilder.must(new TermQueryBuilder(IndexFieldConstants.IS_DISTRIBUTION_MP,1));
		}
		List<Long> merchantCategoryIds=selectionProductSearchRequest.getMerchantCategoryIds();
		if(CollectionUtils.isNotEmpty(merchantCategoryIds)){
			BoolQueryBuilder merchantCategoryBoolQueryBuilder = new BoolQueryBuilder();
			for(Long merchantCategoryId:merchantCategoryIds){
				TermQueryBuilder merchantCategoryTermQuery = new TermQueryBuilder(IndexFieldConstants.MERCHANTCATEGORYID_SEARCH,merchantCategoryId);
				merchantCategoryBoolQueryBuilder.should(merchantCategoryTermQuery);
			}
			boolQueryBuilder.must(merchantCategoryBoolQueryBuilder);
		}

		List<Integer> types = selectionProductSearchRequest.getTypes();
		if(CollectionUtils.isNotEmpty(types)){
			BoolQueryBuilder typesBoolQueryBuilder=new BoolQueryBuilder();
			for(Integer type:types){
				typesBoolQueryBuilder.should(new TermQueryBuilder(IndexFieldConstants.TYPE, type));
			}
			boolQueryBuilder.must(typesBoolQueryBuilder);
		}
		List<Integer> excludeTypes = selectionProductSearchRequest.getExcludeTypes();
		if(CollectionUtils.isNotEmpty(excludeTypes)){
			BoolQueryBuilder excludeTypesBoolQueryBuilder=new BoolQueryBuilder();
			for(Integer type:excludeTypes){
				excludeTypesBoolQueryBuilder.mustNot(new TermQueryBuilder(IndexFieldConstants.TYPE, type));
			}
			boolQueryBuilder.must(excludeTypesBoolQueryBuilder);
		}

		List<Integer> merchantTypes = selectionProductSearchRequest.getMerchantType();
		if(CollectionUtils.isNotEmpty(merchantTypes)){
			BoolQueryBuilder typesBoolQueryBuilder=new BoolQueryBuilder();
			for(Integer merchantType:merchantTypes){
				typesBoolQueryBuilder.should(new TermQueryBuilder(IndexFieldConstants.MERCHANTTYPE, merchantType));
			}
			boolQueryBuilder.must(typesBoolQueryBuilder);
		}
		List<Integer> excludeMerchantTypes = selectionProductSearchRequest.getExcludeMerchantType();
		if(CollectionUtils.isNotEmpty(excludeMerchantTypes)){
			BoolQueryBuilder excludeTypesBoolQueryBuilder=new BoolQueryBuilder();
			for(Integer type:excludeMerchantTypes){
				excludeTypesBoolQueryBuilder.mustNot(new TermQueryBuilder(IndexFieldConstants.MERCHANTTYPE, type));
			}
			boolQueryBuilder.must(excludeTypesBoolQueryBuilder);
		}

		PriceRange orgPriceRange=selectionProductSearchRequest.getOriginalPriceRange();
		if(orgPriceRange!=null){
			RangeQueryBuilder orgPriceRangeQueryBuilder=new RangeQueryBuilder(IndexFieldConstants.ORG_PRICE);
			if(orgPriceRange.getMinPrice()!=null){
				orgPriceRangeQueryBuilder.from(orgPriceRange.getMinPrice());
			}
			if(orgPriceRange.getMaxPrice()!=null){
				orgPriceRangeQueryBuilder.to(orgPriceRange.getMaxPrice());
			}
			boolQueryBuilder.must(orgPriceRangeQueryBuilder);
		}
		sortBuilder(esSearchRequest,selectionProductSearchRequest);
		esSearchRequest.setQueryBuilder(boolQueryBuilder);
		esSearchRequest.setStart(0);
		esSearchRequest.setCount(2000);
        RequestFieldsBuilder.build(esSearchRequest);
		return esSearchRequest;
	}
	
	public static ESSearchRequest handle2(SelectionProductSearchRequest selectionProductSearchRequest,String indexAliasName){
		ESSearchRequest esSearchRequest = handle(selectionProductSearchRequest,indexAliasName);
		esSearchRequest.setStart(selectionProductSearchRequest.getStart());
		esSearchRequest.setCount(selectionProductSearchRequest.getCount());
		return esSearchRequest;
	}
	
	private static void sortBuilder(ESSearchRequest esSearchRequest,SelectionProductSearchRequest selectionProductSearchRequest){
		List<SortType> sortTypeList = selectionProductSearchRequest.getSortTypeList();
		if(CollectionUtils.isNotEmpty(sortTypeList)){
			List<org.elasticsearch.search.sort.SortBuilder> sortBuilderList = com.odianyun.search.whale.req.builder.SortBuilder.sorterBuild(sortTypeList);
			if(CollectionUtils.isNotEmpty(sortBuilderList)){
				esSearchRequest.setSortBuilderList(sortBuilderList);
			}
		}

	}

}
