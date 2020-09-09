package com.odianyun.search.whale.geo;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.odianyun.search.whale.common.IndexNameManager;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.geo.builders.PointBuilder;
import org.elasticsearch.common.geo.builders.ShapeBuilder;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.FilteredQueryBuilder;
import org.elasticsearch.index.query.GeoDistanceRangeFilterBuilder;
import org.elasticsearch.index.query.GeoShapeQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;

import com.odianyun.search.whale.api.common.SearchException;
import com.odianyun.search.whale.api.model.geo.GeoDistanceSearchRequest;
import com.odianyun.search.whale.api.model.geo.Point;
import com.odianyun.search.whale.api.model.geo.GeoDistanceSearchRequest.Distance;
import com.odianyun.search.whale.es.api.ESService;
import com.odianyun.search.whale.es.request.ESSearchRequest;
import com.odianyun.search.whale.index.api.common.MerchantAreaIndexContants;
import com.odianyun.search.whale.index.api.common.MerchantAreaIndexFieldContants;

public class CheckIntersectionHandler {
	
	static Logger logger = Logger.getLogger(CheckIntersectionHandler.class);
	
	static ExecutorService es=new ThreadPoolExecutor(20, 30, 60,TimeUnit.SECONDS,new ArrayBlockingQueue<Runnable>(100));
	
	public static Map<Point,Boolean> check(final Client client,final Long merchantId,List<Point> points,final Integer companyId){
		Map<Point,Boolean> ret=new HashMap<Point,Boolean>();
		if(CollectionUtils.isNotEmpty(points)){
			CompletionService<Boolean> completionService=new ExecutorCompletionService<Boolean>(es);
			Map<Future<Boolean>,Point> pending=new HashMap<Future<Boolean>,Point>();
			for(final Point point:points){
				pending.put(completionService.submit(new Callable<Boolean>() {

					@Override
					public Boolean call() throws Exception {
						logger.error("point: "+point.toString() + " merchantId: "+merchantId);
						/*
						PointBuilder pointBuilder=ShapeBuilder.newPoint(point.getLongitude().doubleValue(), point.getLatitude().doubleValue());
						GeoShapeQueryBuilder geoShapeQueryBuilder=new GeoShapeQueryBuilder(MerchantAreaIndexFieldContants.POLYGON,pointBuilder);
						BoolQueryBuilder boolQueryBuilder=new BoolQueryBuilder();
						TermQueryBuilder termQuery= new TermQueryBuilder(MerchantAreaIndexFieldContants.COMPANYID,companyId);
						TermQueryBuilder merchantIdTermQuery= new TermQueryBuilder(MerchantAreaIndexFieldContants.MERCHANTID,merchantId);
						boolQueryBuilder.must(termQuery);
						boolQueryBuilder.must(merchantIdTermQuery);
						boolQueryBuilder.must(geoShapeQueryBuilder);	
						ESSearchRequest esSearchRequest=new ESSearchRequest(IndexNameManager.getGeoIndexName(), MerchantAreaIndexContants.index_type);
						esSearchRequest.setQueryBuilder(boolQueryBuilder);
						SearchResponse searchResponse=ESService.search(client,esSearchRequest);
						return searchResponse!=null&&searchResponse.getHits()!=null&&searchResponse.getHits().getTotalHits()>0?true:false;
						*/
						return isPointDistance(client, merchantId, point, companyId);
					}
					
				}),point);
			}
			while(pending.size()>0){
				try{
					Future<Boolean> future = completionService.take();
					Boolean bool=future.get(); 
					Point point=pending.remove(future);
					ret.put(point, bool);
				}catch(Exception e){
					logger.error(e.getMessage(), e);
					throw new SearchException("CheckIntersectionHandler check faild",e);
				}
			}
		}
		return ret;
	}
	
	public static Boolean isPointDistance(final Client client,final Long merchantId, Point point,final Integer companyId) {
		Distance distance = new Distance(2D);
		GeoDistanceSearchRequest geoDistanceSearchRequest= new GeoDistanceSearchRequest(point, companyId, distance);
		String from=null;
		String to=null;
		if(geoDistanceSearchRequest.getDistance().getDistance()!=null){
			from="0km";
			to=geoDistanceSearchRequest.getDistance().getDistance()+"km";
		}
		GeoDistanceRangeFilterBuilder geoDistanceRangeFilterBuilder=new GeoDistanceRangeFilterBuilder(MerchantAreaIndexContants.index_type
				+"."+MerchantAreaIndexFieldContants.LOCATION);
		geoDistanceRangeFilterBuilder.point(point.getLatitude().doubleValue(),point.getLongitude().doubleValue()).from(from).to(to);
		BoolQueryBuilder boolQueryBuilder=new BoolQueryBuilder();
		
		TermQueryBuilder termQuery= new TermQueryBuilder(MerchantAreaIndexFieldContants.COMPANYID,companyId);
		TermQueryBuilder merchantIdTermQuery= new TermQueryBuilder(MerchantAreaIndexFieldContants.MERCHANTID,merchantId);
		boolQueryBuilder.must(termQuery);
		boolQueryBuilder.must(merchantIdTermQuery);
		
		FilteredQueryBuilder filteredQueryBuilder=new FilteredQueryBuilder(boolQueryBuilder, geoDistanceRangeFilterBuilder);
		

		ESSearchRequest esSearchRequest=new ESSearchRequest(IndexNameManager.getGeoIndexName(), MerchantAreaIndexContants.index_type);
		esSearchRequest.setQueryBuilder(filteredQueryBuilder);
		SearchResponse searchResponse = null;
		try {
			searchResponse = ESService.search(client,esSearchRequest);
		} catch (ElasticsearchException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return searchResponse!=null&&searchResponse.getHits()!=null&&searchResponse.getHits().getTotalHits()>0?true:false;
	}

	public static Map<Long,Boolean> check2(final Client client,final List<Long> merchantIds,final Point point,final Integer companyId){
		Map<Long,Boolean> ret=new HashMap<Long,Boolean>();
		if(CollectionUtils.isNotEmpty(merchantIds) && point!=null){
			CompletionService<Boolean> completionService=new ExecutorCompletionService<Boolean>(es);
			Map<Future<Boolean>,Long> pending=new HashMap<Future<Boolean>,Long>();
			for(final Long merchantId:merchantIds){
				pending.put(completionService.submit(new Callable<Boolean>() {

					@Override
					public Boolean call() throws Exception {
						PointBuilder pointBuilder=ShapeBuilder.newPoint(point.getLongitude().doubleValue(), point.getLatitude().doubleValue());
						GeoShapeQueryBuilder geoShapeQueryBuilder=new GeoShapeQueryBuilder(MerchantAreaIndexFieldContants.POLYGON,pointBuilder);
						BoolQueryBuilder boolQueryBuilder=new BoolQueryBuilder();
						TermQueryBuilder termQuery= new TermQueryBuilder(MerchantAreaIndexFieldContants.COMPANYID,companyId);
						TermQueryBuilder merchantIdTermQuery= new TermQueryBuilder(MerchantAreaIndexFieldContants.MERCHANTID,merchantId);
						boolQueryBuilder.must(termQuery);
						boolQueryBuilder.must(merchantIdTermQuery);
						boolQueryBuilder.must(geoShapeQueryBuilder);
						ESSearchRequest esSearchRequest=new ESSearchRequest(IndexNameManager.getGeoIndexName(), MerchantAreaIndexContants.index_type);
						esSearchRequest.setQueryBuilder(boolQueryBuilder);
						SearchResponse searchResponse=ESService.search(client,esSearchRequest);
						return searchResponse!=null&&searchResponse.getHits()!=null&&searchResponse.getHits().getTotalHits()>0?true:false;
					}

				}),merchantId);
			}
			while(pending.size()>0){
				try{
					Future<Boolean> future = completionService.take();
					Boolean bool=future.get();
					Long merchantId=pending.remove(future);
					ret.put(merchantId, bool);
				}catch(Exception e){
					logger.error(e.getMessage(), e);
					throw new SearchException("CheckIntersectionHandler check faild",e);
				}
			}
		}
		return ret;
	}

}
