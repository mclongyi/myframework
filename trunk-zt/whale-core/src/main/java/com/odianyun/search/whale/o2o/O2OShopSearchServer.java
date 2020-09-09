package com.odianyun.search.whale.o2o;

import java.util.List;
import java.util.Map;

import com.odianyun.search.whale.api.model.MerchantProduct;
import com.odianyun.search.whale.api.model.req.SearchByCodeRequest;
import com.odianyun.search.whale.api.model.resp.SearchByCodeResponse;
import com.odianyun.search.whale.common.IndexNameManager;
import com.odianyun.soa.InputDTO;
import com.odianyun.soa.OutputDTO;
import com.odianyun.soa.SoaUtil;
import org.apache.log4j.Logger;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.springframework.beans.factory.annotation.Autowired;

import com.odianyun.search.whale.api.common.SearchException;
import com.odianyun.search.whale.api.model.o2o.O2OShopSearchRequest;
import com.odianyun.search.whale.api.model.o2o.O2OShopSearchResponse;
import com.odianyun.search.whale.api.model.o2o.O2OShopSearchService;
import com.odianyun.search.whale.data.saas.model.CompanyAppType;
import com.odianyun.search.whale.data.saas.model.ESClusterConfig;
import com.odianyun.search.whale.data.saas.service.CompanyRoutingService;
import com.odianyun.search.whale.es.api.ESClient;
import com.odianyun.search.whale.es.api.ESService;
import com.odianyun.search.whale.es.request.ESSearchRequest;
import com.odianyun.search.whale.index.api.common.IndexConstants;
import com.odianyun.search.whale.o2o.req.O2OShopSearchRequestBuilder;
import com.odianyun.search.whale.o2o.resp.O2OShopSearchResponseHandler;

public class O2OShopSearchServer implements O2OShopSearchService {

	static Logger logger = Logger.getLogger(O2OShopSearchServer.class);

	List<O2OShopSearchRequestBuilder> o2OShopSearchRequestBuilders;

	List<O2OShopSearchResponseHandler> o2OShopSearchResponseHandlers;

	@Override
	public O2OShopSearchResponse shopSearch(O2OShopSearchRequest o2oShopSearchRequest) throws SearchException {
		O2OShopSearchResponse o2OShopSearchResponse = new O2OShopSearchResponse();
		o2OShopSearchResponse.setCompanyId(o2oShopSearchRequest.getCompanyId());

		try {

			ESSearchRequest esSearchRequest = new ESSearchRequest(IndexNameManager.getIndexName(),
					IndexConstants.index_type);
			for (O2OShopSearchRequestBuilder o2OShopSearchRequestBuilder : o2OShopSearchRequestBuilders) {
				o2OShopSearchRequestBuilder.build(esSearchRequest, o2oShopSearchRequest);
			}

			SearchResponse searchResponse = ESService.search(esSearchRequest);

			for (O2OShopSearchResponseHandler o2OShopSearchResponseHandler : o2OShopSearchResponseHandlers) {
				o2OShopSearchResponseHandler.handle(searchResponse, o2OShopSearchResponse);
			}

		} catch (SearchException e) {
			logger.error(e.getMessage(), e);
			// throw e;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			// throw new SearchException(e.getMessage(), e);
		}

		return o2OShopSearchResponse;
	}

	@Override
	public OutputDTO<O2OShopSearchResponse> shopSearchStandard(InputDTO<O2OShopSearchRequest> inputDTO) throws SearchException {
//		logger.info("soa 调用入参 ,"+inputDTO.getData());

		O2OShopSearchResponse response;
		try {
			 response = shopSearch(inputDTO.getData());
		} catch (Exception e) {
			logger.error("soa fail {},"+inputDTO, e);
			return SoaUtil.resultError(e.getMessage());
		}
//		logger.info("soa 调用出参 ,"+response);
		return SoaUtil.resultSucess(response);
	}

	public List<O2OShopSearchRequestBuilder> getO2OShopSearchRequestBuilders() {
		return o2OShopSearchRequestBuilders;
	}

	public void setO2OShopSearchRequestBuilders(List<O2OShopSearchRequestBuilder> o2oShopSearchRequestBuilders) {
		o2OShopSearchRequestBuilders = o2oShopSearchRequestBuilders;
	}

	public List<O2OShopSearchResponseHandler> getO2OShopSearchResponseHandlers() {
		return o2OShopSearchResponseHandlers;
	}

	public void setO2OShopSearchResponseHandlers(List<O2OShopSearchResponseHandler> o2oShopSearchResponseHandlers) {
		o2OShopSearchResponseHandlers = o2oShopSearchResponseHandlers;
	}

}
