package com.odianyun.search.whale.geo.req;

import com.odianyun.search.whale.api.model.req.MerchantFilterType;
import com.odianyun.search.whale.common.MapService;
import com.odianyun.search.whale.common.query.KeywordQueryBuilder;
import com.odianyun.search.whale.index.api.common.IndexConstants;
import com.odianyun.search.whale.shop.req.ShopListSortBuilder;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.elasticsearch.common.geo.ShapeRelation;
import org.elasticsearch.common.geo.builders.PointBuilder;
import org.elasticsearch.common.geo.builders.ShapeBuilder;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.common.unit.DistanceUnit.Distance;
import org.elasticsearch.index.query.*;
import org.springframework.beans.factory.annotation.Autowired;

import com.odianyun.search.whale.api.model.geo.GeoDistanceSearchRequest;
import com.odianyun.search.whale.api.model.geo.GeoSearchRequest;
import com.odianyun.search.whale.api.model.geo.Point;
import com.odianyun.search.whale.data.service.ConfigService;
import com.odianyun.search.whale.es.request.ESSearchRequest;
import com.odianyun.search.whale.index.api.common.IndexFieldConstants;
import com.odianyun.search.whale.index.api.common.MerchantAreaIndexContants;
import com.odianyun.search.whale.index.api.common.MerchantAreaIndexFieldContants;

import java.util.Date;
import java.util.List;

public class GeoQueryBuilder implements GeoRequestBuilder{
	
	static Logger logger = Logger.getLogger(GeoQueryBuilder.class);
	
	public static final String GEO_FILTER_NAME="o2o";

	static final int DEFAULT_MERCHANT_FLAG = 1;
	
	public static String map_url_config = "http://restapi.amap.com/v3/geocode/geo?key=6fc0b8f1d1095f96afcba6da65ebaeaf";
	
	@Autowired
	ConfigService configService;

	@Override
	public void build(ESSearchRequest esSearchRequest,
			GeoSearchRequest geoSearchRequest) {
		int companyId=geoSearchRequest.getCompanyId();
		map_url_config = configService.get("map_url", IndexConstants.MAP_URL,companyId);
		Point point=geoSearchRequest.getPoint();
		Double longitude=null;
		Double latitude=null;
		if(point!=null){
			longitude=point.getLongitude();
			latitude=point.getLatitude();
		}
		String address=geoSearchRequest.getAddress();
		if((longitude==null || latitude==null)&&address!=null){
			//String map_url="http://restapi.amap.com/v3/geocode/geo?key="+map_key+"&address="+address;
			try {
				Point newPoint = MapService.getPoint(address,map_url_config);
				longitude = newPoint.getLongitude();
				latitude = newPoint.getLatitude();
				geoSearchRequest.setPoint(newPoint);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				throw new RuntimeException(e);
			}
		}
		BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
		boolQueryBuilder.must(new TermQueryBuilder(MerchantAreaIndexFieldContants.MERCHANT_FLAG,DEFAULT_MERCHANT_FLAG));
		TermQueryBuilder termQuery=null;
	    if(geoSearchRequest.getCompanyId()!=null){
	    	termQuery= new TermQueryBuilder(IndexFieldConstants.COMPANYID,geoSearchRequest.getCompanyId());
			boolQueryBuilder.must(termQuery);
	    }

		String keyword = geoSearchRequest.getKeyword();
		if(StringUtils.isNotBlank(keyword)){
			boolQueryBuilder.must(buildKeywordQuery(keyword,esSearchRequest));
		}
		String merchantCode = geoSearchRequest.getMerchantCode();
		if(StringUtils.isNotBlank(merchantCode)){
			boolQueryBuilder.must(new TermQueryBuilder(MerchantAreaIndexFieldContants.MERCHANT_CODE,merchantCode));
		}
	    //外卖新加 merchantType
		logger.info("0MerchantType==>>>>"+geoSearchRequest.getMerchantType());
	    if(geoSearchRequest.getMerchantType() != null) {
	    	logger.info("1MerchantType==>>>>"+geoSearchRequest.getMerchantType());
			boolQueryBuilder.must(new TermQueryBuilder(IndexFieldConstants.MERCHANTTYPE,geoSearchRequest.getMerchantType()));
	    }

		List<MerchantFilterType> filterList = geoSearchRequest.getFilterList();
		if(CollectionUtils.isNotEmpty(filterList)){
			BoolQueryBuilder filterQuery = buildFilterQuery(filterList);
			if(filterQuery != null && filterQuery.hasClauses()){
				boolQueryBuilder.must(filterQuery);
			}
		}

		if(geoSearchRequest instanceof GeoDistanceSearchRequest){
			GeoDistanceSearchRequest geoDistanceSearchRequest=(GeoDistanceSearchRequest) geoSearchRequest;
			String from=null;
			String to=null;
			if(geoDistanceSearchRequest.getDistance().getDistance()!=null){
				from="0km";
				to=geoDistanceSearchRequest.getDistance().getDistance()+"km";
			}
			if(geoDistanceSearchRequest.getDistance().getFrom()!=null &&
					geoDistanceSearchRequest.getDistance().getTo()!=null){
				from=geoDistanceSearchRequest.getDistance().getFrom()+"km";
				to=geoDistanceSearchRequest.getDistance().getTo()+"km";
			}
			GeoDistanceRangeFilterBuilder geoDistanceRangeFilterBuilder=new GeoDistanceRangeFilterBuilder(MerchantAreaIndexContants.index_type
					+"."+MerchantAreaIndexFieldContants.LOCATION);
			geoDistanceRangeFilterBuilder.point(latitude,longitude).from(from).to(to);
			FilteredQueryBuilder filteredQueryBuilder=new FilteredQueryBuilder(boolQueryBuilder,geoDistanceRangeFilterBuilder);
			esSearchRequest.setQueryBuilder(filteredQueryBuilder);
		}else{
			PointBuilder pointBuilder=ShapeBuilder.newPoint(longitude.doubleValue(), latitude.doubleValue());
			GeoShapeFilterBuilder geoShapeFilterBuilder=new GeoShapeFilterBuilder(MerchantAreaIndexFieldContants.POLYGON,pointBuilder,ShapeRelation.INTERSECTS);			
			FilteredQueryBuilder filteredQueryBuilder=new FilteredQueryBuilder(boolQueryBuilder,geoShapeFilterBuilder);
			esSearchRequest.setQueryBuilder(filteredQueryBuilder);
		}
		
		
	}

	private QueryBuilder buildKeywordQuery(String keyword, ESSearchRequest esSearchRequest) {
		QueryBuilder childQueryBuilder = esSearchRequest.getQueryBuilder();
		BoolQueryBuilder keywordQuery = QueryBuilders.boolQuery();
		keywordQuery.should(KeywordQueryBuilder.buildKeywordQuery(keyword,MerchantAreaIndexFieldContants.TAG_WORDS));
		if(childQueryBuilder != null){
			keywordQuery.should(childQueryBuilder);
		}
		return keywordQuery;
	}

	private BoolQueryBuilder buildFilterQuery(List<MerchantFilterType> filterList) {
		BoolQueryBuilder filterQuery = QueryBuilders.boolQuery();
		for(MerchantFilterType filterType : filterList){
			switch (filterType) {
				case OUT_OF_BUSINESS:
					filterQuery.must(QueryBuilders.termQuery(MerchantAreaIndexFieldContants.BUSINESS_STATE, 1));
					String now = ShopListSortBuilder.sdf.format(new Date());
					FilterBuilder start = FilterBuilders.rangeFilter(MerchantAreaIndexContants.BUSINESS_TIME_START).lte(now);
					FilterBuilder end = FilterBuilders.rangeFilter(MerchantAreaIndexContants.BUSINESS_TIME_END).gte(now);
					FilterBuilder filter = FilterBuilders.boolFilter().must(start).must(end);
					filterQuery.must(QueryBuilders.nestedQuery(MerchantAreaIndexContants.BUSINESS_TIME, filter));
					break;
				case HAS_IN_SITE_SERVICE:
					filterQuery.must(QueryBuilders.termQuery(MerchantAreaIndexFieldContants.HAS_IN_SITE_SERVICE, 1));
					break;
				default:
					break;
			}
		}

		return filterQuery;
	}


	public static void main(String[] args){
		GeoDistanceFilterBuilder builder=new GeoDistanceFilterBuilder("");
		builder.distance(69, DistanceUnit.KILOMETERS);
		System.out.println(Distance.parseDistance("33km"));
	}
	

}
