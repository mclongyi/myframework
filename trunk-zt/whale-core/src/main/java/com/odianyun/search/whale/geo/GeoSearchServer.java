package com.odianyun.search.whale.geo;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.odianyun.search.whale.api.model.req.CheckIntersectionRequest;
import com.odianyun.search.whale.api.model.resp.CheckIntersectionResponse;
import com.odianyun.search.whale.common.IndexNameManager;
import com.odianyun.soa.InputDTO;
import com.odianyun.soa.OutputDTO;
import com.odianyun.soa.SoaUtil;
import org.apache.log4j.Logger;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.springframework.beans.factory.annotation.Autowired;

import com.odianyun.search.whale.api.common.SearchException;
import com.odianyun.search.whale.api.model.geo.GeoSearchRequest;
import com.odianyun.search.whale.api.model.geo.GeoSearchResponse;
import com.odianyun.search.whale.api.model.geo.GeoSearchService;
import com.odianyun.search.whale.api.model.geo.Point;
import com.odianyun.search.whale.data.saas.model.CompanyAppType;
import com.odianyun.search.whale.data.saas.model.ESClusterConfig;
import com.odianyun.search.whale.data.saas.service.CompanyRoutingService;
import com.odianyun.search.whale.es.api.ESClient;
import com.odianyun.search.whale.es.api.ESService;
import com.odianyun.search.whale.es.request.ESSearchRequest;
import com.odianyun.search.whale.geo.req.GeoRequestBuilder;
import com.odianyun.search.whale.geo.resp.GeoResponseHandler;
import com.odianyun.search.whale.index.api.common.MerchantAreaIndexContants;

public class GeoSearchServer implements GeoSearchService{
	
	static Logger logger = Logger.getLogger(GeoSearchServer.class);
	
	List<GeoRequestBuilder> geoRequestBuilders;
	
	List<GeoResponseHandler> geoResponseHandlers;
	
	@Autowired
	AreaCodeConvertor areaCodeConvertor;

	@Override
	public GeoSearchResponse search(GeoSearchRequest geoSearchRequest) {
		GeoSearchResponse geoSearchResponse=new GeoSearchResponse();
		int companyId=geoSearchRequest.getCompanyId();

		try{
			geoSearchResponse.setCompanyId(companyId);
			ESSearchRequest esSearchRequest =new ESSearchRequest(IndexNameManager.getGeoIndexName(),
					 MerchantAreaIndexContants.index_type);
			for(GeoRequestBuilder geoRequestBuilder: geoRequestBuilders){
				geoRequestBuilder.build(esSearchRequest, geoSearchRequest);
			}
			SearchResponse esSearchResponse = ESService.search(esSearchRequest);
			for(GeoResponseHandler geoResponseHandler:geoResponseHandlers){
				geoResponseHandler.handle(esSearchResponse, geoSearchResponse);
			}
			if(geoSearchRequest.getArea()!=null){
				geoSearchResponse.setAreaCode(areaCodeConvertor.convert(geoSearchRequest.getArea(),companyId));
			}
		}catch(Exception e){
			logger.error(e.getMessage(), e);
		}
		return geoSearchResponse;
	}

	public List<GeoRequestBuilder> getGeoRequestBuilders() {
		return geoRequestBuilders;
	}

	public void setGeoRequestBuilders(List<GeoRequestBuilder> geoRequestBuilders) {
		this.geoRequestBuilders = geoRequestBuilders;
	}

	public List<GeoResponseHandler> getGeoResponseHandlers() {
		return geoResponseHandlers;
	}

	public void setGeoResponseHandlers(List<GeoResponseHandler> geoResponseHandlers) {
		this.geoResponseHandlers = geoResponseHandlers;
	}

	@Override
	public Map<Point, Boolean> checkIntersection(Long merchantId,
			List<Point> points,Integer companyId) throws SearchException {
		Map<Point,Boolean> ret=new HashMap<Point,Boolean>();
		Client client = null;
		try {
			client = ESClient.getClient();
			ret = CheckIntersectionHandler.check(client,merchantId, points, companyId);
		} catch (ElasticsearchException e) {
			logger.error(e.getMessage(), e);
		} catch (UnknownHostException e) {
			logger.error(e.getMessage(), e);
		}
		return ret;
	}

	/**
	 * 检查这些商家的覆盖范围是否包含指定的点
	 *
	 * @param merchantIds
	 * @param point
	 * @param companyId
	 * @return <MerchantId,覆盖范围是否包含指定的点>
	 * @throws SearchException
	 */
	@Override
	public Map<Long, Boolean> checkIntersection2(List<Long> merchantIds, Point point, Integer companyId) throws SearchException {
		Map<Long,Boolean> ret=new HashMap<Long,Boolean>();
		Client client = null;
		try {
			client = ESClient.getClient();
			ret = CheckIntersectionHandler.check2(client,merchantIds, point, companyId);
		} catch (ElasticsearchException e) {
			logger.error(e.getMessage(), e);
		} catch (UnknownHostException e) {
			logger.error(e.getMessage(), e);
		}
		return ret;
	}

	//-----------------------------SOA包装-------------------------------

	@Override
	public OutputDTO<GeoSearchResponse> searchStandard(InputDTO<GeoSearchRequest> inputDTO) throws SearchException {
//		logger.info("soa 调用入参 ,"+inputDTO.getData());
		GeoSearchResponse response;
		try {
			response = search(inputDTO.getData());
		} catch (Exception e) {
			logger.error("soa fail {}"+inputDTO, e);
			return SoaUtil.resultError(e.getMessage());
		}
//		logger.info("soa 调用出参 ,"+response);
		return SoaUtil.resultSucess(response);
	}

	@Override
	public OutputDTO<CheckIntersectionResponse> checkIntersectionStandard(InputDTO<CheckIntersectionRequest> inputDTO) throws SearchException {
//		logger.info("soa 调用入参 ,"+inputDTO.getData());
		CheckIntersectionResponse response = new CheckIntersectionResponse();
		try {
			CheckIntersectionRequest inputDTOData = inputDTO.getData();
			if (inputDTOData!=null){
				Map<Point, Boolean> pointBooleanMap = checkIntersection(inputDTOData.getMerchantId(), inputDTOData.getPoints(), 30);
				response.setPointBooleanMap(pointBooleanMap);
			}

		} catch (Exception e) {
			logger.error("soa fail {}"+inputDTO, e);
			return SoaUtil.resultError(e.getMessage());
		}
//		logger.info("soa 调用出参 ,"+response);
		return SoaUtil.resultSucess(response);
	}

	@Override
	public OutputDTO<CheckIntersectionResponse> checkIntersection2Standard(InputDTO<CheckIntersectionRequest> inputDTO) throws SearchException {
//		logger.info("soa 调用入参 ,"+inputDTO.getData());
		CheckIntersectionResponse response = new CheckIntersectionResponse();
		try {
			CheckIntersectionRequest inputDTOData = inputDTO.getData();
			if (inputDTOData!=null){
				Map<Long, Boolean> longBooleanMap = checkIntersection2(inputDTOData.getMerchantIds(), inputDTOData.getPoint(), 30);
				response.setLongBooleanMap(longBooleanMap);
			}

		} catch (Exception e) {
			logger.error("soa fail {}"+inputDTO, e);
			return SoaUtil.resultError(e.getMessage());
		}
//		logger.info("soa 调用出参 ,"+response);
		return SoaUtil.resultSucess(response);
	}


}
