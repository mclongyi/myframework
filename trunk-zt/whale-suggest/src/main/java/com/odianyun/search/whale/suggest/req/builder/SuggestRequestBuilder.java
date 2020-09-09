package com.odianyun.search.whale.suggest.req.builder;

import com.odianyun.search.whale.api.model.SuggestType;
import com.odianyun.search.whale.api.model.req.SuggestRequest;
import com.odianyun.search.whale.data.service.ConfigService;
import com.odianyun.search.whale.es.request.ESSuggestRequest;
import com.odianyun.search.whale.index.api.common.IndexConstants;
import com.odianyun.search.whale.index.api.common.SuggestIndexFieldConstants;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class SuggestRequestBuilder implements RequestBuilder<ESSuggestRequest, SuggestRequest> {
	@Autowired
	ConfigService configService;

	@Override
	public void build(ESSuggestRequest esSuggestRequest, SuggestRequest suggestRequest) {
		String input = suggestRequest.getInput().toLowerCase();
		Integer companyId = suggestRequest.getCompanyId();
//		esSuggestRequest.setInput(companyId+ "_" + input);
		esSuggestRequest.setInput(input);
		List<String> companyIdList = new ArrayList<>();
		companyIdList.add(String.valueOf(companyId));
		esSuggestRequest.getCategoryMap().put(SuggestIndexFieldConstants.COMPANYID,companyIdList);

		Long merchantId = suggestRequest.getMerchantId();

		// 不设开关建索引时将merchantId作为key值的一部分
//		boolean hasBusiness = configService.getBool("suggest_has_business", true, IndexConstants.DEFAULT_COMPANY_ID);

		// 当suggestType为merchant时，merchantId为-1
		if(merchantId == null || merchantId == 0){
			merchantId = -1l;
		}

		List<String> merchantIdList = new ArrayList<>();
		merchantIdList.add(String.valueOf(merchantId));
		esSuggestRequest.getCategoryMap().put(SuggestIndexFieldConstants.MERCHANTID,merchantIdList);

		List<String> suggestType = new ArrayList<>();
		suggestType.add(String.valueOf(suggestRequest.getType().getCode()));
		esSuggestRequest.getCategoryMap().put(SuggestIndexFieldConstants.ISAREA,suggestType);

		esSuggestRequest.setField(SuggestIndexFieldConstants.SUGGEST);
	}

}
