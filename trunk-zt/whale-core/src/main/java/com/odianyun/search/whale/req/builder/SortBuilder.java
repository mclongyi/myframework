package com.odianyun.search.whale.req.builder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.odianyun.search.whale.data.service.ConfigService;
import org.apache.commons.collections.CollectionUtils;
/**
 * 判断排序的类型,默认使用创建时间排序;
 */
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.ScoreSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;

import com.odianyun.search.whale.api.model.req.BaseSearchRequest;
import com.odianyun.search.whale.api.model.req.SortType;
import com.odianyun.search.whale.es.request.ESSearchRequest;
import com.odianyun.search.whale.index.api.common.IndexFieldConstants;
import org.springframework.beans.factory.annotation.Autowired;

public class SortBuilder implements RequestBuilder{

	@Autowired
	ConfigService configService;

	public void build(ESSearchRequest esSearchRequest,BaseSearchRequest searchRequest){

		List<SortType> sortTypeList = searchRequest.getSortTypeList();
		List<org.elasticsearch.search.sort.SortBuilder> sortBuilderList = new ArrayList
				<org.elasticsearch.search.sort.SortBuilder>() ;

		/*默认情况走综合排序*/
		/*sortBuilderList.add(new FieldSortBuilder(IndexFieldConstants.HAS_PIC).order(SortOrder.DESC));//无图置底
		sortBuilderList.add(new FieldSortBuilder(IndexFieldConstants.STOCK).order(SortOrder.DESC));//无库存沉底
		*/
		sortBuilderList.add(new FieldSortBuilder(IndexFieldConstants.COMPOSITE_SORT).order(SortOrder.DESC));//无库存沉底

		if(CollectionUtils.isEmpty(sortTypeList)){
			sortBuilderList.add(new ScoreSortBuilder().order(SortOrder.DESC));//文本相关性
			//是否考虑季节相关性
			boolean IS_SEASON_WEIGHT = configService.getBool("is_season_weight",false,searchRequest.getCompanyId());
			if(IS_SEASON_WEIGHT){
				sortBuilderList.add(new FieldSortBuilder(IndexFieldConstants.SEASON_WEIGHT).order(SortOrder.DESC));
			}
			sortBuilderList.add(new FieldSortBuilder(IndexFieldConstants.VOLUME4SALE).order(SortOrder.DESC));//销量排序
		}else {
			sortBuilderList.addAll(sorterBuild(sortTypeList));

		}
		esSearchRequest.setSortBuilderList(sortBuilderList);

	}

	public static List<org.elasticsearch.search.sort.SortBuilder> sorterBuild(List<SortType> sortTypeList) {
		List<org.elasticsearch.search.sort.SortBuilder> sortBuilderList = new ArrayList
				<org.elasticsearch.search.sort.SortBuilder>() ;
		if(CollectionUtils.isEmpty(sortTypeList)){
			return sortBuilderList;
		}
		for (SortType sortType : sortTypeList) {
			if (sortType != null) {
				org.elasticsearch.search.sort.SortBuilder sortBuilder = null;
				if (sortType == SortType.create_time_asc) {
					sortBuilder = new FieldSortBuilder(IndexFieldConstants.CREATE_TIME).order(SortOrder.ASC);
				} else if (sortType == SortType.create_time_desc) {
					sortBuilder = new FieldSortBuilder(IndexFieldConstants.CREATE_TIME).order(SortOrder.DESC);
				} else if (sortType == SortType.price_asc) {
					sortBuilder = new FieldSortBuilder(IndexFieldConstants.PRICE).order(SortOrder.ASC);
				} else if (sortType == SortType.price_desc) {
					sortBuilder = new FieldSortBuilder(IndexFieldConstants.PRICE).order(SortOrder.DESC);
				}else if (sortType == SortType.org_price_asc) {
					sortBuilder = new FieldSortBuilder(IndexFieldConstants.ORG_PRICE).order(SortOrder.ASC);
				} else if (sortType == SortType.org_price_desc) {
					sortBuilder = new FieldSortBuilder(IndexFieldConstants.ORG_PRICE).order(SortOrder.DESC);
				}else if (sortType == SortType.volume4sale_asc) {
					sortBuilder = new FieldSortBuilder(IndexFieldConstants.VOLUME4SALE).order(SortOrder.ASC);
				} else if (sortType == SortType.volume4sale_desc) {
					sortBuilder = new FieldSortBuilder(IndexFieldConstants.VOLUME4SALE).order(SortOrder.DESC);
				} else if (sortType == SortType.rate_asc) {
					sortBuilder = new FieldSortBuilder(IndexFieldConstants.RATE).order(SortOrder.ASC);
				} else if (sortType == SortType.rate_desc) {
					sortBuilder = new FieldSortBuilder(IndexFieldConstants.RATE).order(SortOrder.DESC);
				} else if (sortType == SortType.rating_count_asc) {
					sortBuilder = new FieldSortBuilder(IndexFieldConstants.RATING_COUNT).order(SortOrder.ASC);
				} else if (sortType == SortType.rating_count_desc) {
					sortBuilder = new FieldSortBuilder(IndexFieldConstants.RATING_COUNT).order(SortOrder.DESC);
				} else if (sortType == SortType.positive_rate_asc) {
					sortBuilder = new FieldSortBuilder(IndexFieldConstants.POSITIVE_RATE).order(SortOrder.ASC);
				} else if (sortType == SortType.positive_rate_desc) {
					sortBuilder = new FieldSortBuilder(IndexFieldConstants.POSITIVE_RATE).order(SortOrder.DESC);
				} else if (sortType == SortType.real_volume4sale_asc) {
					sortBuilder = new FieldSortBuilder(IndexFieldConstants.REAL_VOLUME4SALE).order(SortOrder.ASC);
				} else if (sortType == SortType.real_volume4sale_desc) {
					sortBuilder = new FieldSortBuilder(IndexFieldConstants.REAL_VOLUME4SALE).order(SortOrder.DESC);
				} else if (sortType == SortType.first_shelf_time_asc){
					sortBuilder = new FieldSortBuilder(IndexFieldConstants.FIRST_SHELF_TIME).order(SortOrder.ASC).missing("2015-01-01 00:00:00");
				} else if (sortType == SortType.first_shelf_time_desc){
					sortBuilder = new FieldSortBuilder(IndexFieldConstants.FIRST_SHELF_TIME).order(SortOrder.DESC).missing("2015-01-01 00:00:00");
				}else if (sortType == SortType.commodityCommission_asc) {
					sortBuilder = new FieldSortBuilder(IndexFieldConstants.COMMODITYCOMMISSION).order(SortOrder.ASC).missing(0);
				} else if (sortType == SortType.commodityCommission_desc) {
					sortBuilder = new FieldSortBuilder(IndexFieldConstants.COMMODITYCOMMISSION).order(SortOrder.DESC).missing(0);
				} else if (sortType == SortType.point_price_asc){
					sortBuilder = new FieldSortBuilder(IndexFieldConstants.POINTS_PRICE).order(SortOrder.ASC).missing(Integer.MAX_VALUE);
				} else if (sortType == SortType.point_price_desc){
					sortBuilder = new FieldSortBuilder(IndexFieldConstants.POINTS_PRICE).order(SortOrder.DESC).missing(0);
				}
				if(sortBuilder!=null) {
					sortBuilderList.add(sortBuilder);
				}
			}
		}
		return sortBuilderList;

	}

}
