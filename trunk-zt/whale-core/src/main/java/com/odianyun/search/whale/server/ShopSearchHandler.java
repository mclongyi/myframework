package com.odianyun.search.whale.server;

import java.util.Date;
import java.util.List;

import com.odianyun.search.whale.common.IndexNameManager;
import org.apache.log4j.Logger;
import org.elasticsearch.client.Client;
import org.springframework.beans.factory.annotation.Autowired;

import com.odianyun.search.whale.api.common.SearchException;
import com.odianyun.search.whale.api.model.SearchResponse;
import com.odianyun.search.whale.api.model.req.ShopSearchRequest;
import com.odianyun.search.whale.data.saas.model.CompanyAppType;
import com.odianyun.search.whale.data.saas.model.ESClusterConfig;
import com.odianyun.search.whale.data.saas.service.CompanyRoutingService;
import com.odianyun.search.whale.es.api.ESClient;
import com.odianyun.search.whale.es.api.ESService;
import com.odianyun.search.whale.es.request.ESSearchRequest;
import com.odianyun.search.whale.index.api.common.IndexConstants;
import com.odianyun.search.whale.misc.ShopSearchRequestValidator;
import com.odianyun.search.whale.req.builder.RequestBuilder;
import com.odianyun.search.whale.resp.handler.ResponseHandler;

public class ShopSearchHandler {

	static Logger logger = Logger.getLogger(ShopSearchHandler.class);

	List<RequestBuilder> shopRequestBuilders;

	List<ResponseHandler> shopResponseHandlers;

	// @Autowired
	// CompanyRoutingService companyRoutingService;

	public SearchResponse handle(ShopSearchRequest shopRequest) throws SearchException {
		SearchResponse searchResponse = new SearchResponse();
		Date startTime = new Date();
		if (!ShopSearchRequestValidator.validate(shopRequest)) {
			return searchResponse;
		} // 检查搜索请求参数合法性

		try {
			searchResponse.setCompanyId(shopRequest.getCompanyId());
			ESSearchRequest esSearchRequest = new ESSearchRequest(IndexNameManager.getIndexName(),
					IndexConstants.index_type);
			if (shopRequestBuilders != null) {
				for (RequestBuilder builder : shopRequestBuilders) {
					builder.build(esSearchRequest, shopRequest);
				}
			}
			org.elasticsearch.action.search.SearchResponse esSearchResponse = ESService.search(esSearchRequest);

			if (shopResponseHandlers != null) {
				for (ResponseHandler responseHandler : shopResponseHandlers) {
					responseHandler.handle(esSearchResponse, searchResponse, esSearchRequest, shopRequest);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new SearchException("search failed", e);
		}

		Date endTime = new Date();
		searchResponse.costTime = endTime.getTime() - startTime.getTime();
		shopRequest.setRequestTime(startTime);
		return searchResponse;
	}

	public List<RequestBuilder> getShopRequestBuilders() {
		return shopRequestBuilders;
	}

	public void setShopRequestBuilders(List<RequestBuilder> shopRequestBuilders) {
		this.shopRequestBuilders = shopRequestBuilders;
	}

	public List<ResponseHandler> getShopResponseHandlers() {
		return shopResponseHandlers;
	}

	public void setShopResponseHandlers(List<ResponseHandler> shopResponseHandlers) {
		this.shopResponseHandlers = shopResponseHandlers;
	}

}
