package com.odianyun.search.whale.suggest.handler;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.odianyun.search.whale.api.model.SuggestType;
import org.apache.log4j.Logger;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.cache.CacheBuilder;
import org.elasticsearch.common.cache.CacheLoader;
import org.elasticsearch.common.cache.LoadingCache;
import org.springframework.beans.factory.annotation.Autowired;

import com.odianyun.search.whale.api.common.SearchException;
import com.odianyun.search.whale.api.model.req.SuggestRequest;
import com.odianyun.search.whale.api.model.resp.AreaSuggestResponse;
import com.odianyun.search.whale.common.util.ConfigUtil;
import com.odianyun.search.whale.data.saas.model.CompanyAppType;
import com.odianyun.search.whale.data.saas.model.ESClusterConfig;
import com.odianyun.search.whale.data.saas.service.CompanyRoutingService;
import com.odianyun.search.whale.es.api.ESClient;
import com.odianyun.search.whale.es.api.ESService;
import com.odianyun.search.whale.es.request.ESSuggestRequest;
import com.odianyun.search.whale.index.api.common.SuggestIndexConstants;
import com.odianyun.search.whale.suggest.req.builder.RequestBuilder;
import com.odianyun.search.whale.suggest.resp.handler.ResponseHandler;

public class AreaSuggestHandler {

	static Logger logger = Logger.getLogger(AreaSuggestHandler.class);

	List<RequestBuilder<ESSuggestRequest, SuggestRequest>> requestBuilders;

	List<ResponseHandler<org.elasticsearch.action.suggest.SuggestResponse, AreaSuggestResponse>> responseHandlers;

	static {
		try {
			ConfigUtil.loadPropertiesFile("suggest.properties");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage(), e);
		}
	}

	private static long autoCacheSize = ConfigUtil.getLong("autoCacheSize", 100 * 1000);
	private static long expireAfterAccessTime = ConfigUtil.getLong("expireAfterAccessTime", 60);
	private static long expireAfterWriteTime = ConfigUtil.getLong("expireAfterWriteTime", 60);

	private LoadingCache<SuggestRequest, AreaSuggestResponse> suggestCache = CacheBuilder.newBuilder()
			.concurrencyLevel(8).maximumSize(autoCacheSize).expireAfterAccess(expireAfterAccessTime, TimeUnit.MINUTES)
			.expireAfterWrite(expireAfterWriteTime, TimeUnit.MINUTES).recordStats()
			.build(new CacheLoader<SuggestRequest, AreaSuggestResponse>() {
				@Override
				public AreaSuggestResponse load(SuggestRequest key) throws Exception {
					return suggest(key);
				}
			});

	public List<RequestBuilder<ESSuggestRequest, SuggestRequest>> getRequestBuilders() {
		return requestBuilders;
	}

	public void setRequestBuilders(List<RequestBuilder<ESSuggestRequest, SuggestRequest>> requestBuilders) {
		this.requestBuilders = requestBuilders;
	}

	public List<ResponseHandler<org.elasticsearch.action.suggest.SuggestResponse, AreaSuggestResponse>> getResponseHandlers() {
		return responseHandlers;
	}

	public void setResponseHandlers(
			List<ResponseHandler<org.elasticsearch.action.suggest.SuggestResponse, AreaSuggestResponse>> responseHandlers) {
		this.responseHandlers = responseHandlers;
	}

	protected AreaSuggestResponse suggest(SuggestRequest request) {
		AreaSuggestResponse areaSuggestResponse = new AreaSuggestResponse();

		try {

			ESSuggestRequest esSuggestRequest = new ESSuggestRequest(SuggestIndexConstants.index_alias,
					SuggestIndexConstants.index_type);
			if (requestBuilders != null) {
				for (RequestBuilder<ESSuggestRequest, SuggestRequest> builder : requestBuilders) {
					builder.build(esSuggestRequest, request);
				}
			}
			org.elasticsearch.action.suggest.SuggestResponse esSuggestResponse = ESService.suggest(esSuggestRequest);

			if (responseHandlers != null) {
				for (ResponseHandler<org.elasticsearch.action.suggest.SuggestResponse, AreaSuggestResponse> responseHandler : responseHandlers) {
					responseHandler.handle(esSuggestResponse, areaSuggestResponse);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new SearchException("areaSuggest failed", e);
		}

		return areaSuggestResponse;
	}

	public AreaSuggestResponse handler(SuggestRequest request) throws SearchException {
		AreaSuggestResponse response = new AreaSuggestResponse();
		request.setType(SuggestType.AREA);
		try {
			response = suggestCache.get(request);
		} catch (ExecutionException e) {
			throw new SearchException(e);
		}
		return response;
	}

	public void cleanCache() throws Exception {
		suggestCache.cleanUp();
		suggestCache.invalidateAll();
	}
}
