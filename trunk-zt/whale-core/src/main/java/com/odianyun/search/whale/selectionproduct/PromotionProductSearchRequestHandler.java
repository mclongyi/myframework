package com.odianyun.search.whale.selectionproduct;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.odianyun.search.whale.api.model.PriceRange;
import com.odianyun.search.whale.api.model.selectionproduct.TypeOfProduct;
import com.odianyun.search.whale.api.model.selectionproduct.TypeOfProductFilter;
import com.odianyun.search.whale.req.builder.CombineQueryBuilder;
import com.odianyun.search.whale.req.builder.TypeOfProductQueryBuilder;
import org.apache.commons.collections.CollectionUtils;
import org.elasticsearch.common.lang3.StringUtils;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;

import com.odianyun.search.whale.api.model.ManagementType;
import com.odianyun.search.whale.api.model.req.SortType;
import com.odianyun.search.whale.api.model.selectionproduct.PromotionProductSearchRequest;
import com.odianyun.search.whale.common.RequestFieldsBuilder;
import com.odianyun.search.whale.common.query.KeywordQueryBuilder;
import com.odianyun.search.whale.es.request.ESSearchRequest;
import com.odianyun.search.whale.index.api.common.IndexConstants;
import com.odianyun.search.whale.index.api.common.IndexFieldConstants;

public class PromotionProductSearchRequestHandler {
	
	public static ESSearchRequest handle(PromotionProductSearchRequest promotionProductSearchRequest,String indexAliasName){
		ESSearchRequest esSearchRequest=new ESSearchRequest(indexAliasName, IndexConstants.index_type);
		esSearchRequest.setMaxStart(20000);
		BoolQueryBuilder boolQueryBuilder=new BoolQueryBuilder();
		Integer companyId = promotionProductSearchRequest.getCompanyId();
		if(companyId != null && !companyId.equals(0)){
			TermQueryBuilder termQuery= new TermQueryBuilder(IndexFieldConstants.COMPANYID,companyId);
			boolQueryBuilder.must(termQuery);
		}
		ManagementType managementType = promotionProductSearchRequest.getManagementState();
		if(!ManagementType.VERIFIED.equals(managementType)){
			boolQueryBuilder.must(new TermQueryBuilder(IndexFieldConstants.MANAGEMENT_STATE, managementType.getCode()));
		}

		//商家类型过滤和选择
		List<Integer> merchantTypes = promotionProductSearchRequest.getMerchantType();
		if(CollectionUtils.isNotEmpty(merchantTypes)){
			BoolQueryBuilder typesBoolQueryBuilder=new BoolQueryBuilder();
			for(Integer merchantType:merchantTypes){
				typesBoolQueryBuilder.should(new TermQueryBuilder(IndexFieldConstants.MERCHANTTYPE, merchantType));
			}
			boolQueryBuilder.must(typesBoolQueryBuilder);
		}
		List<Integer> excludeMerchantTypes = promotionProductSearchRequest.getExcludeMerchantType();
		if(CollectionUtils.isNotEmpty(excludeMerchantTypes)){
			BoolQueryBuilder excludeTypesBoolQueryBuilder=new BoolQueryBuilder();
			for(Integer type:excludeMerchantTypes){
				excludeTypesBoolQueryBuilder.mustNot(new TermQueryBuilder(IndexFieldConstants.MERCHANTTYPE, type));
			}
			boolQueryBuilder.must(excludeTypesBoolQueryBuilder);
		}

//		boolQueryBuilder.must(CombineQueryBuilder.buildCombineQuery(promotionProductSearchRequest.isCombine()));

		TypeOfProductFilter typeOfProductFilter = promotionProductSearchRequest.getTypeOfProductFilter();
		if(typeOfProductFilter != null){
			List<TypeOfProduct> typeList = typeOfProductFilter.getTypeOfProductList();
			boolQueryBuilder.must(TypeOfProductQueryBuilder.buildTypeQuery(typeList));
		}


		List<String> codes = promotionProductSearchRequest.getCodes();
		List<String> eans = promotionProductSearchRequest.getEans(); 
		String mpName = promotionProductSearchRequest.getMerchantProductName();
		if(StringUtils.isNotBlank(mpName)){
			boolQueryBuilder.must(KeywordQueryBuilder.buildKeywordQueryForMerchantProductName(mpName));
		}
		String brandName = promotionProductSearchRequest.getBrandName();
		if(StringUtils.isNotBlank(brandName)){
			boolQueryBuilder.must(KeywordQueryBuilder.buildKeywordQuery(brandName, IndexFieldConstants.BRANDNAME_SEARCH));
		}
		
		String categoryName = promotionProductSearchRequest.getCategoryName();
		if(StringUtils.isNotBlank(categoryName)){
			boolQueryBuilder.must(KeywordQueryBuilder.buildKeywordQuery(categoryName, IndexFieldConstants.CATEGORYNAME_SEARCH));
		}
		
		List<Long> excludeBrandIdList = promotionProductSearchRequest.getExcludeBrandIdList();
		if(CollectionUtils.isNotEmpty(excludeBrandIdList)){
			for(Long excludeBrandId : excludeBrandIdList){
				boolQueryBuilder.mustNot(new TermQueryBuilder(IndexFieldConstants.BRANDID_SEARCH,excludeBrandId));
			}
		}
		
		List<Long> excludeCategoryIdList = promotionProductSearchRequest.getExcludeCategoryIdList();
		if(CollectionUtils.isNotEmpty(excludeCategoryIdList)){
			for(Long excludeCategoryId : excludeCategoryIdList){
				boolQueryBuilder.mustNot(new TermQueryBuilder(IndexFieldConstants.CATEGORYID_SEARCH,excludeCategoryId));
			}
		}
		
		if(CollectionUtils.isNotEmpty(codes)){
			BoolQueryBuilder codesBoolQueryBuilder = new BoolQueryBuilder();
			for(String code : codes){
				codesBoolQueryBuilder.should(new TermQueryBuilder(IndexFieldConstants.CODE,code));
			}
			boolQueryBuilder.must(codesBoolQueryBuilder);
		}
		if(CollectionUtils.isNotEmpty(eans)){
			BoolQueryBuilder eansBoolQueryBuilder = new BoolQueryBuilder();
			for(String ean : eans){
				eansBoolQueryBuilder.should(new TermQueryBuilder(IndexFieldConstants.EAN_NO,ean));
			}
			boolQueryBuilder.must(eansBoolQueryBuilder);
		}
		List<String> productCodes = promotionProductSearchRequest.getProductCodes();
		if(CollectionUtils.isNotEmpty(productCodes)){
			BoolQueryBuilder codesBoolQueryBuilder = new BoolQueryBuilder();
			for(String code : productCodes){
				codesBoolQueryBuilder.should(new TermQueryBuilder(IndexFieldConstants.PRODUCT_CODE,code));
			}
			boolQueryBuilder.must(codesBoolQueryBuilder);
		}
		
		List<Long> categoryIds=promotionProductSearchRequest.getCategoryIds();
		if(CollectionUtils.isNotEmpty(categoryIds)){
			BoolQueryBuilder categoryIdBoolQueryBuilder=new BoolQueryBuilder();
			for(Long categoryId:categoryIds){
				categoryIdBoolQueryBuilder.should(new TermQueryBuilder(IndexFieldConstants.CATEGORYID_SEARCH,categoryId));
			}
			boolQueryBuilder.must(categoryIdBoolQueryBuilder);
		}
		
		List<Long> brandIds=promotionProductSearchRequest.getBrandIds();
		if(CollectionUtils.isNotEmpty(brandIds)){
			BoolQueryBuilder brandIdsBoolQueryBuilder=new BoolQueryBuilder();
			for(Long brandId:brandIds){
				TermQueryBuilder brandIdTermQuery= new TermQueryBuilder(IndexFieldConstants.BRANDID_SEARCH,brandId);
				brandIdsBoolQueryBuilder.should(brandIdTermQuery);
			}
			boolQueryBuilder.must(brandIdsBoolQueryBuilder);
		}
		
		List<Long> merchantIds=promotionProductSearchRequest.getMerchantIds();
		if(CollectionUtils.isNotEmpty(merchantIds)){
			BoolQueryBuilder merchantIdsBoolQueryBuilder=new BoolQueryBuilder();
			for(Long merchantId:merchantIds){
				TermQueryBuilder merchantIdTermQuery= new TermQueryBuilder(IndexFieldConstants.MERCHANTID,merchantId);
				merchantIdsBoolQueryBuilder.should(merchantIdTermQuery);
			}
			boolQueryBuilder.must(merchantIdsBoolQueryBuilder);
		}
		Long parentMerchantId = promotionProductSearchRequest.getParentMerchantId();
		if(parentMerchantId!=null){
			boolQueryBuilder.must(new TermQueryBuilder(IndexFieldConstants.PARENT_MERCHANT_ID,parentMerchantId));
		}
		List<Long> subMerchantIds = promotionProductSearchRequest.getSubMerchantIds();
		if(CollectionUtils.isNotEmpty(subMerchantIds)){
			BoolQueryBuilder subMerchantIdBoolQueryBuilder = new BoolQueryBuilder();
			for(Long id : subMerchantIds){
				subMerchantIdBoolQueryBuilder.should(new TermQueryBuilder(IndexFieldConstants.SUB_MERCHANT_IDS,id));
			}
			boolQueryBuilder.must(subMerchantIdBoolQueryBuilder);
		}
		List<String> thirdCodes = promotionProductSearchRequest.getThirdCodes();
		if(CollectionUtils.isNotEmpty(thirdCodes)){
			BoolQueryBuilder thirdCodeBoolQueryBuilder = new BoolQueryBuilder();
			for(String code:thirdCodes){
				thirdCodeBoolQueryBuilder.should(new TermQueryBuilder(IndexFieldConstants.THIRD_CODE,code));
			}
			boolQueryBuilder.must(thirdCodeBoolQueryBuilder);
		}
		List<String> merchantNames = promotionProductSearchRequest.getMerchantNames();
		if(CollectionUtils.isNotEmpty(merchantNames)){
			BoolQueryBuilder merchantNamesBoolQueryBuilder=new BoolQueryBuilder();
			for(String merchantName : merchantNames){
				QueryBuilder merchantNameTermQuery=KeywordQueryBuilder.buildKeywordQueryForMerchantName(merchantName);
				merchantNamesBoolQueryBuilder.should(merchantNameTermQuery);
			}
			boolQueryBuilder.must(merchantNamesBoolQueryBuilder);
		}

		List<Long> productIdList = promotionProductSearchRequest.getProductIdList();
		if(CollectionUtils.isNotEmpty(productIdList)){
			BoolQueryBuilder productIdBoolQueryBuilder=new BoolQueryBuilder();
			for(Long productId : productIdList){
				QueryBuilder productIdTermQuery=new TermQueryBuilder(IndexFieldConstants.PRODUCT_ID,productId);
				productIdBoolQueryBuilder.should(productIdTermQuery);
			}
			boolQueryBuilder.must(productIdBoolQueryBuilder);
		}

		List<Long> mpIdList = promotionProductSearchRequest.getMerchantProductIdList();
		if(CollectionUtils.isNotEmpty(mpIdList)){
			BoolQueryBuilder mpIdBoolQueryBuilder=new BoolQueryBuilder();
			for(Long mpId : mpIdList){
				QueryBuilder mpIdTermQuery=new TermQueryBuilder(IndexFieldConstants.ID,mpId);
				mpIdBoolQueryBuilder.should(mpIdTermQuery);
			}
			boolQueryBuilder.must(mpIdBoolQueryBuilder);
		}
		Boolean isDistributionMp=promotionProductSearchRequest.getDistributionMp();
		if(isDistributionMp!=null && isDistributionMp==true){
			boolQueryBuilder.must(new TermQueryBuilder(IndexFieldConstants.IS_DISTRIBUTION_MP,1));
		}

		PriceRange priceRange=promotionProductSearchRequest.getPriceRange();
		if(priceRange!=null && (priceRange.getMinPrice() != null || priceRange.getMaxPrice()!=null)){
			RangeQueryBuilder rangeQueryBuilder=new RangeQueryBuilder(IndexFieldConstants.PRICE);
			if(priceRange.getMinPrice()!=null){
				rangeQueryBuilder.from(priceRange.getMinPrice());
			}
			if(priceRange.getMaxPrice()!=null){
				rangeQueryBuilder.to(priceRange.getMaxPrice());
			}
			boolQueryBuilder.must(rangeQueryBuilder);
		}
		List<Integer> types = promotionProductSearchRequest.getTypes();
		if(CollectionUtils.isNotEmpty(types)){
			BoolQueryBuilder typesBoolQueryBuilder=new BoolQueryBuilder();
			for(Integer type:types){
				typesBoolQueryBuilder.should(new TermQueryBuilder(IndexFieldConstants.TYPE, type));
			}
			boolQueryBuilder.must(typesBoolQueryBuilder);
		}
		List<Integer> excludeTypes = promotionProductSearchRequest.getExcludeTypes();
		if(CollectionUtils.isNotEmpty(excludeTypes)){
			BoolQueryBuilder excludeTypesBoolQueryBuilder=new BoolQueryBuilder();
			for(Integer type:excludeTypes){
				excludeTypesBoolQueryBuilder.mustNot(new TermQueryBuilder(IndexFieldConstants.TYPE, type));
			}
			boolQueryBuilder.must(excludeTypesBoolQueryBuilder);
		}
		PriceRange orgPriceRange=promotionProductSearchRequest.getOriginalPriceRange();
		if(orgPriceRange!=null && (orgPriceRange.getMinPrice()!=null || orgPriceRange.getMaxPrice() != null)){
			RangeQueryBuilder orgPriceRangeQueryBuilder=new RangeQueryBuilder(IndexFieldConstants.ORG_PRICE);
			if(orgPriceRange.getMinPrice()!=null){
				orgPriceRangeQueryBuilder.from(orgPriceRange.getMinPrice());
			}
			if(orgPriceRange.getMaxPrice()!=null){
				orgPriceRangeQueryBuilder.to(orgPriceRange.getMaxPrice());
			}
			boolQueryBuilder.must(orgPriceRangeQueryBuilder);
		}

		esSearchRequest.setQueryBuilder(boolQueryBuilder);
		
		sortBuilder(esSearchRequest,promotionProductSearchRequest);
		
		esSearchRequest.setStart(promotionProductSearchRequest.getStart());
		esSearchRequest.setCount(promotionProductSearchRequest.getCount());
        RequestFieldsBuilder.build(esSearchRequest);
		esSearchRequest.getFields().add(IndexFieldConstants.TYPE_OF_PRODUCT);
		return esSearchRequest;
	}
	
	private static void sortBuilder(ESSearchRequest esSearchRequest,PromotionProductSearchRequest promotionProductSearchRequest){
		List<SortType> sortTypeList = promotionProductSearchRequest.getSortTypeList();
		List<org.elasticsearch.search.sort.SortBuilder> sortBuilderList = new ArrayList
				<org.elasticsearch.search.sort.SortBuilder>() ;
		if(CollectionUtils.isNotEmpty(sortTypeList)){
			sortBuilderList=com.odianyun.search.whale.req.builder.SortBuilder.sorterBuild(sortTypeList);
		}else{
			sortBuilderList.add(SortBuilders.fieldSort(IndexFieldConstants.PRODUCT_ID).order(SortOrder.ASC));
			sortBuilderList.add(SortBuilders.fieldSort(IndexFieldConstants.ID).order(SortOrder.ASC));
		}
		if(CollectionUtils.isNotEmpty(sortBuilderList)){
			esSearchRequest.setSortBuilderList(sortBuilderList);
		}
	}

}
