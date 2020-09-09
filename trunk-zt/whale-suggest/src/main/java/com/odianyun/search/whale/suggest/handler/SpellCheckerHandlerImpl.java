package com.odianyun.search.whale.suggest.handler;

import java.util.List;

import org.apache.log4j.Logger;
import org.elasticsearch.action.suggest.SuggestResponse;
import org.elasticsearch.client.Client;
import org.springframework.beans.factory.annotation.Autowired;

import com.odianyun.search.whale.analysis.ISegment;
import com.odianyun.search.whale.analysis.user.SearchPolicy;
import com.odianyun.search.whale.api.common.SearchException;
import com.odianyun.search.whale.api.model.req.SpellCheckerRequest;
import com.odianyun.search.whale.api.model.resp.SpellCheckerResponse;
import com.odianyun.search.whale.data.saas.model.CompanyAppType;
import com.odianyun.search.whale.data.saas.model.ESClusterConfig;
import com.odianyun.search.whale.data.saas.service.CompanyRoutingService;
import com.odianyun.search.whale.es.api.ESClient;
import com.odianyun.search.whale.es.api.ESService;
import com.odianyun.search.whale.es.request.ESSpellCheckRequest;
import com.odianyun.search.whale.index.api.common.IndexConstants;
import com.odianyun.search.whale.suggest.req.builder.RequestBuilder;
import com.odianyun.search.whale.suggest.resp.handler.ResponseHandler;

/**
 * 处理拼写检查实现
 *
 * @author jing liu
 *
 */
public class SpellCheckerHandlerImpl implements SpellCheckerHandler {

	private static final Logger LOGGER = Logger.getLogger(SpellCheckerHandlerImpl.class);

	private List<RequestBuilder<ESSpellCheckRequest, SpellCheckerRequest>> requestBuilders;
	private List<ResponseHandler<SuggestResponse, com.odianyun.search.whale.api.model.resp.SpellCheckerResponse>> responseHandlers;

	private final SearchPolicy policy;
	private final ISegment segment;
//	@Autowired
//	CompanyRoutingService companyRoutingService;
	public SpellCheckerHandlerImpl() throws Exception {
		 this.policy = new SearchPolicy();
		 this.segment = this.policy.get();
	}

	public List<RequestBuilder<ESSpellCheckRequest, SpellCheckerRequest>> getRequestBuilders() {
		return requestBuilders;
	}

	public void setRequestBuilders(List<RequestBuilder<ESSpellCheckRequest, SpellCheckerRequest>> requestBuilders) {
		this.requestBuilders = requestBuilders;
	}

	public List<ResponseHandler<SuggestResponse, com.odianyun.search.whale.api.model.resp.SpellCheckerResponse>> getResponseHandlers() {
		return responseHandlers;
	}

	public void setResponseHandlers(
			List<ResponseHandler<SuggestResponse, com.odianyun.search.whale.api.model.resp.SpellCheckerResponse>> responseHandlers) {
		this.responseHandlers = responseHandlers;
	}

	@Override
	public SpellCheckerResponse handle(SpellCheckerRequest request) throws SearchException {

		SpellCheckerResponse response = new SpellCheckerResponse();
		response.setText(request.getInput());
			try {
				List<String> tokens = this.segment.segment(request.getInput());
				response.setTokens(tokens);
				if (tokens != null && !tokens.isEmpty()) {
					StringBuilder sb = new StringBuilder();
					for (String token : tokens) {
						sb.append(token).append(" ");
					}
					request.setInput(sb.toString());
				}

				ESSpellCheckRequest esRequest = new ESSpellCheckRequest(IndexConstants.index_alias,IndexConstants.index_type);
				if (this.requestBuilders != null) {
					for (RequestBuilder<ESSpellCheckRequest, SpellCheckerRequest> builder : this.requestBuilders) {
						builder.build(esRequest, request);
					}
				}
				org.elasticsearch.action.suggest.SuggestResponse esResponse = ESService.spellcheck(esRequest);

				if (responseHandlers != null) {
					for (ResponseHandler<SuggestResponse, com.odianyun.search.whale.api.model.resp.SpellCheckerResponse> responseHandler : responseHandlers) {
						responseHandler.handle(esResponse, response);
					}
				}
			}catch(Exception e){
				LOGGER.error("spell check error", e);
				throw new SearchException("spell check failed", e);
			}

		return response;
	}
}
