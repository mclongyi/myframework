package com.odianyun.search.whale.req.builder;


import com.google.gson.Gson;
import com.odianyun.search.whale.api.model.ManagementType;
import com.odianyun.search.whale.api.model.PriceRange;
import com.odianyun.search.whale.api.model.req.BaseSearchRequest;
import com.odianyun.search.whale.api.model.selectionproduct.TypeOfProduct;
import com.odianyun.search.whale.api.model.selectionproduct.TypeOfProductFilter;
import com.odianyun.search.whale.common.query.KeywordQueryBuilder;
import com.odianyun.search.whale.data.model.Area;
import com.odianyun.search.whale.data.service.AreaService;
import com.odianyun.search.whale.data.service.ConfigService;
import com.odianyun.search.whale.es.request.ESSearchRequest;
import com.odianyun.search.whale.index.api.common.IndexFieldConstants;
import com.odianyun.search.whale.index.api.common.MerchantAreaIndexContants;
import com.odianyun.search.whale.index.api.common.MerchantAreaIndexFieldContants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.elasticsearch.common.geo.ShapeRelation;
import org.elasticsearch.common.geo.builders.PointBuilder;
import org.elasticsearch.common.geo.builders.ShapeBuilder;
import org.elasticsearch.index.query.*;
import org.elasticsearch.index.query.support.QueryInnerHitBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;


public abstract class BaseQueryStrBuilder implements RequestBuilder {

	@Autowired
	ConfigService configService;

	@Autowired
	AreaService areaService;
	
	public static boolean  IS_COMBINE_DISPLAY = true;
	
	static Logger logger = Logger.getLogger(BaseQueryStrBuilder.class);
	
	public void build(ESSearchRequest esSearchRequest, BaseSearchRequest searchRequest){
		BoolQueryBuilder boolQueryBuilder=new BoolQueryBuilder();
		BoolQueryBuilder aggregationBoolQueryBuilder = new BoolQueryBuilder();
		QueryBuilder keywordQueryBuilder=buildKeywordQuery(aggregationBoolQueryBuilder, searchRequest);
		if(keywordQueryBuilder!=null){
			boolQueryBuilder.must(keywordQueryBuilder);
			aggregationBoolQueryBuilder.must(keywordQueryBuilder);
		}

		//判断是否有经纬度
		calcGeoQuery(esSearchRequest, searchRequest, boolQueryBuilder);

		List<Integer> coverProvinceIds=searchRequest.getCoverProvinceIds();
		if(CollectionUtils.isNotEmpty(coverProvinceIds)){
			BoolQueryBuilder coverProvinceIdQueryBuilder=new BoolQueryBuilder();
			for(Integer coverProvinceId:coverProvinceIds){
				coverProvinceIdQueryBuilder.should(new TermQueryBuilder(IndexFieldConstants.COVER_PROVINCE_ID,String.valueOf(coverProvinceId)));
			}
			boolQueryBuilder.must(coverProvinceIdQueryBuilder);
			aggregationBoolQueryBuilder.must(coverProvinceIdQueryBuilder);
		}
		Integer companyId =searchRequest.getCompanyId();
		List<Long> saleAreaCodes = searchRequest.getSaleAreaCode();
		if(CollectionUtils.isNotEmpty(saleAreaCodes)){
			BoolQueryBuilder saleAreaCodesBuilder = new BoolQueryBuilder();
			try{
				for(Long code : saleAreaCodes){
					List<Long> parentCodes = areaService.getAllParentAreaCode(code,companyId);
					if(parentCodes!=null && CollectionUtils.isNotEmpty(parentCodes)){
						for(Long pa : parentCodes){
							saleAreaCodesBuilder.should(new TermQueryBuilder(IndexFieldConstants.SALE_AREA_CODES,String.valueOf(pa)));
						}
					}
					/*Area parArea = areaService.getParentArea(code,companyId);
					if(null!=parArea){
						saleAreaCodesBuilder.should(new TermQueryBuilder(IndexFieldConstants.SEARCH_AREA_CODES,String.valueOf(parArea.getCode())));
					}*/
					saleAreaCodesBuilder.should(new TermQueryBuilder(IndexFieldConstants.SEARCH_AREA_CODES,String.valueOf(code)));
					saleAreaCodesBuilder.should(new TermQueryBuilder(IndexFieldConstants.SALE_AREA_CODES,String.valueOf(code)));
					//特殊销售区域 -1  暂时注释
					saleAreaCodesBuilder.should(new TermQueryBuilder(IndexFieldConstants.SEARCH_AREA_CODES,"-1"));
					saleAreaCodesBuilder.should(new TermQueryBuilder(IndexFieldConstants.SALE_AREA_CODES,"-1"));
				}
			}catch (Exception e){
				logger.error(e.getMessage(),e);
				e.printStackTrace();
			}

			boolQueryBuilder.must(saleAreaCodesBuilder);
			aggregationBoolQueryBuilder.must(saleAreaCodesBuilder);
		}
		if(companyId!=null){
//			boolQueryBuilder.must(new TermQueryBuilder(IndexFieldConstants.COMPANYID,companyId));
			TermQueryBuilder termQuery= new TermQueryBuilder(IndexFieldConstants.COMPANYID,companyId);
			boolQueryBuilder.must(termQuery);
			aggregationBoolQueryBuilder.must(termQuery);
		}
		
//		String json = GsonUtil.getGson().toJson(boolQueryBuilder); 
//		BoolQueryBuilder aggregationBoolQueryBuilder=GsonUtil.getGson().fromJson(json, BoolQueryBuilder.class);
		
		List<Long> categoryIds=searchRequest.getCategoryIds();
		if(CollectionUtils.isNotEmpty(categoryIds)){
			BoolQueryBuilder categoryIdQueryBuilder=new BoolQueryBuilder();
			for(Long categoryId:categoryIds){
				categoryIdQueryBuilder.should(new TermQueryBuilder(IndexFieldConstants.CATEGORYID_SEARCH,String.valueOf(categoryId)));
			}
			boolQueryBuilder.must(categoryIdQueryBuilder);
		}
		List<Long> attrValueIds=searchRequest.getAttrValueIds();
		if(CollectionUtils.isNotEmpty(attrValueIds)){
			BoolQueryBuilder attrValueIdQueryBuilder=new BoolQueryBuilder();
			for(Long attrValueId:attrValueIds){
				attrValueIdQueryBuilder.should(new TermQueryBuilder(IndexFieldConstants.ATTRVALUEID_SEARCH,String.valueOf(attrValueId)));
			}
			boolQueryBuilder.must(attrValueIdQueryBuilder);
		}
		
		Map<Long, List<Long>> attrItemValueMap = searchRequest.getAttrItemValuesMap();
		if(attrItemValueMap != null && attrItemValueMap.size() > 0) {
			BoolQueryBuilder attrItemQueryBuilder = new BoolQueryBuilder();
			for (List<Long> attrValues : attrItemValueMap.values()) {
				BoolQueryBuilder subAttrItemQueryBuilder = new BoolQueryBuilder();
				for (Long attrValue : attrValues) {
					subAttrItemQueryBuilder.should(new TermQueryBuilder(
							IndexFieldConstants.ATTRVALUEID_SEARCH, String.valueOf(attrValue)));
				}
				attrItemQueryBuilder.must(subAttrItemQueryBuilder);
			}
			boolQueryBuilder.must(attrItemQueryBuilder);
		}
		
		List<Long> brandIds=searchRequest.getBrandIds();
		if(CollectionUtils.isNotEmpty(brandIds)){
			BoolQueryBuilder brandIdQueryBuilder=new BoolQueryBuilder();
			for(Long brandId:brandIds){
				brandIdQueryBuilder.should(new TermQueryBuilder(IndexFieldConstants.BRANDID_SEARCH,String.valueOf(brandId)));
			}
			boolQueryBuilder.must(brandIdQueryBuilder);
		}
		PriceRange priceRange=searchRequest.getPriceRange();
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

		PriceRange orgPriceRange=searchRequest.getOriginalPriceRange();
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

		ManagementType managementType = searchRequest.getManagementState();
		if(!ManagementType.VERIFIED.equals(managementType)){
			TermQueryBuilder termQueryBuilder = new TermQueryBuilder(IndexFieldConstants.MANAGEMENT_STATE, managementType.getCode());
			boolQueryBuilder.must(termQueryBuilder);
			aggregationBoolQueryBuilder.must(termQueryBuilder);
		}

		List<Long> promotionIdList = searchRequest.getPromotionIdList();
		if(CollectionUtils.isNotEmpty(promotionIdList)){
			BoolQueryBuilder promotionIdBuilder = new BoolQueryBuilder();
			for(Long promotionId : promotionIdList){
				promotionIdBuilder.should(new TermQueryBuilder(IndexFieldConstants.PROMOTIOM_ID_SEARCH, promotionId));
			}
			boolQueryBuilder.must(promotionIdBuilder);
			aggregationBoolQueryBuilder.must(promotionIdBuilder);
		}
		// 促销类型条件
		List<Integer> promotionTypeList = searchRequest.getPromotionTypeList();
		if (CollectionUtils.isNotEmpty(promotionTypeList)) {
			BoolQueryBuilder promotionTypeBuilder = new BoolQueryBuilder();
			for (Integer promotionType : promotionTypeList) {
				promotionTypeBuilder.should(new TermQueryBuilder(IndexFieldConstants.PROMOTIOM_TYPE_SEARCH, promotionType));
			}
			boolQueryBuilder.must(promotionTypeBuilder);
			aggregationBoolQueryBuilder.must(promotionTypeBuilder);
		}
		//添加ean码条件
		List<String> eanList = searchRequest.getEanNos();
		if(CollectionUtils.isNotEmpty(eanList)){
			BoolQueryBuilder eanNoBuilder = new BoolQueryBuilder();
			for (String ean:eanList) {
				eanNoBuilder.should(new TermQueryBuilder(IndexFieldConstants.EAN_NO,ean));
			}
			boolQueryBuilder.must(eanNoBuilder);
			aggregationBoolQueryBuilder.must(eanNoBuilder);
		}
		//是否新品
		Integer isNew = searchRequest.getIsNew();
		if(isNew != null && isNew == 1){
			TermQueryBuilder isNewQueryBuilder = new TermQueryBuilder(IndexFieldConstants.ISNEW,1);
			boolQueryBuilder.must(isNewQueryBuilder);
			aggregationBoolQueryBuilder.must(isNewQueryBuilder);
		}

		//是否今日新品
		/*Integer isTodayNew = searchRequest.getIsTodayNew();
		if(isTodayNew!=null && isTodayNew==1){
			Integer isTodayNewNum = configService.getInt("is_today_new_hour",24,companyId);
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date now = new Date();
			RangeQueryBuilder rangeQueryBuilder = new RangeQueryBuilder(IndexFieldConstants.FIRST_SHELF_TIME);
			rangeQueryBuilder.from(df.format(new Date(now.getTime()-(isTodayNewNum*60*60*1000)))).to(df.format(now));
			boolQueryBuilder.must(rangeQueryBuilder);
			aggregationBoolQueryBuilder.must(rangeQueryBuilder);
		}*/
        //是否过滤分销商品
		Boolean isDistributionMp=searchRequest.getDistributionMp();
		if(isDistributionMp!=null && isDistributionMp==true){
			boolQueryBuilder.must(new TermQueryBuilder(IndexFieldConstants.IS_DISTRIBUTION_MP,1));
			aggregationBoolQueryBuilder.must(new TermQueryBuilder(IndexFieldConstants.IS_DISTRIBUTION_MP,1));
		}
		List<Long> merchantIdList=searchRequest.getMerchantIdList();
		if(CollectionUtils.isNotEmpty(merchantIdList)){
			BoolQueryBuilder merchantIdQueryBuilder = new BoolQueryBuilder();
			for(Long merchantId : merchantIdList){
				merchantIdQueryBuilder.should(new TermQueryBuilder(IndexFieldConstants.MERCHANTID,String.valueOf(merchantId)));
			}
			boolQueryBuilder.must(merchantIdQueryBuilder);
			aggregationBoolQueryBuilder.must(merchantIdQueryBuilder);
		}
		IS_COMBINE_DISPLAY = configService.getBool("is_combine_display",true,companyId);
		//QueryBuilder boolFilterBuilder= CombineQueryBuilder.buildCombineQuery();
		//boolQueryBuilder.must(boolFilterBuilder);
		QueryBuilder typeOfProductFilterBuilder=new BoolQueryBuilder();
		TypeOfProductFilter typeOfProductFilter = searchRequest.getTypeOfProductFilter();
		if(typeOfProductFilter != null){
			List<TypeOfProduct> typeList = typeOfProductFilter.getTypeOfProductList();
			typeOfProductFilterBuilder=TypeOfProductQueryBuilder.buildTypeQuery(typeList);
		}
		boolQueryBuilder.must(typeOfProductFilterBuilder);
		aggregationBoolQueryBuilder.must(typeOfProductFilterBuilder);

		calcExcludeQuery(searchRequest,boolQueryBuilder,aggregationBoolQueryBuilder);

		calcTopMerchantProductQuery(searchRequest,boolQueryBuilder,aggregationBoolQueryBuilder);

		buildOtherQuery(boolQueryBuilder, aggregationBoolQueryBuilder,searchRequest);
		//aggregationBoolQueryBuilder 和 searchQueryBuilder 逻辑不一样
		calcAggregationBoolQuery(esSearchRequest,searchRequest,aggregationBoolQueryBuilder);

		esSearchRequest.setQueryBuilder(boolQueryBuilder);
	}

	private void calcTopMerchantProductQuery(BaseSearchRequest searchRequest, BoolQueryBuilder boolQueryBuilder, BoolQueryBuilder aggregationBoolQueryBuilder) {
		List<Long> topMerchantProductIdList = searchRequest.getTopMerchantProductIdList();
		if(CollectionUtils.isNotEmpty(topMerchantProductIdList)){
			for(Long mpId : topMerchantProductIdList){
				boolQueryBuilder.should(new TermQueryBuilder(IndexFieldConstants.ID,mpId).boost(Float.MAX_VALUE));
			}
		}
	}

	protected void calcExcludeQuery(BaseSearchRequest searchRequest,BoolQueryBuilder boolQueryBuilder,BoolQueryBuilder aggregationBoolQueryBuilder){
		List<Long> excludeMerchantIds=searchRequest.getExcludeMerchantIds();
		if(CollectionUtils.isNotEmpty(excludeMerchantIds)){
			for(Long merchantId:excludeMerchantIds){
				boolQueryBuilder.mustNot(new TermQueryBuilder(IndexFieldConstants.MERCHANTID,merchantId));
				aggregationBoolQueryBuilder.mustNot(new TermQueryBuilder(IndexFieldConstants.MERCHANTID,merchantId));
			}
		}
		List<Long> excludeNavCategoryIds=searchRequest.getExcludeNavCategoryIds();
		if(CollectionUtils.isNotEmpty(excludeNavCategoryIds)){
			for(Long navCategoryId:excludeNavCategoryIds){
				boolQueryBuilder.mustNot(new TermQueryBuilder(IndexFieldConstants.NAVCATEGORYID_SEARCH,navCategoryId));
				aggregationBoolQueryBuilder.mustNot(new TermQueryBuilder(IndexFieldConstants.NAVCATEGORYID_SEARCH,navCategoryId));
			}
		}
		List<Long> excludeCategoryIds=searchRequest.getExcludeCategoryIds();
		if(CollectionUtils.isNotEmpty(excludeCategoryIds)){
			for(Long categoryId:excludeCategoryIds){
				boolQueryBuilder.mustNot(new TermQueryBuilder(IndexFieldConstants.CATEGORYID_SEARCH,categoryId));
				aggregationBoolQueryBuilder.mustNot(new TermQueryBuilder(IndexFieldConstants.CATEGORYID_SEARCH,categoryId));
			}
		}
		List<Long> excludeBrandIds=searchRequest.getExcludeBrandIds();
		if(CollectionUtils.isNotEmpty(excludeBrandIds)){
			for(Long brandId:excludeBrandIds){
				boolQueryBuilder.mustNot(new TermQueryBuilder(IndexFieldConstants.BRANDID_SEARCH,brandId));
				aggregationBoolQueryBuilder.mustNot(new TermQueryBuilder(IndexFieldConstants.BRANDID_SEARCH,brandId));
			}
		}

	}

	private void calcGeoQuery(ESSearchRequest esSearchRequest, BaseSearchRequest searchRequest, BoolQueryBuilder boolQueryBuilder){
		//查看经纬度
		if(searchRequest.getPoint() != null){
			PointBuilder pointBuilder= ShapeBuilder.newPoint(searchRequest.getPoint().getLongitude().doubleValue(), searchRequest.getPoint().getLatitude().doubleValue());
			GeoShapeFilterBuilder geoShapeFilterBuilder=new GeoShapeFilterBuilder(MerchantAreaIndexFieldContants.POLYGON,pointBuilder, ShapeRelation.INTERSECTS);
			FilteredQueryBuilder filteredQueryBuilder=new FilteredQueryBuilder(null,geoShapeFilterBuilder);
			QueryBuilder parentQuery = QueryBuilders.hasParentQuery(MerchantAreaIndexContants.index_type, filteredQueryBuilder);
			boolQueryBuilder.should(parentQuery);
		}
	}

	protected void calcAggregationBoolQuery(ESSearchRequest esSearchRequest, BaseSearchRequest searchRequest,BoolQueryBuilder aggregationBoolQueryBuilder){

	}

    //子类可以build不同的Query，重写该方法
	protected void buildOtherQuery(BoolQueryBuilder boolQueryBuilder, BoolQueryBuilder aggregationBoolQueryBuilder,BaseSearchRequest baseSearchRequest){
		
	}
	
	//关键词QueryBuilder，子类可以重写该方法
	protected QueryBuilder buildKeywordQuery(BoolQueryBuilder boolQueryBuilder, BaseSearchRequest baseSearchRequest) {
		//得到输入的关键字 并进行分词
//		return KeywordQueryBuilder.buildKeywordQuery(baseSearchRequest.getKeyword());
		return KeywordQueryBuilder.buildKeywordQueryBySmartType(baseSearchRequest);
	}

}
