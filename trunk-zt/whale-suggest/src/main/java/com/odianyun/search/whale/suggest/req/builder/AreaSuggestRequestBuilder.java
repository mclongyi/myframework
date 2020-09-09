package com.odianyun.search.whale.suggest.req.builder;

import com.odianyun.search.whale.api.model.req.SuggestRequest;
import com.odianyun.search.whale.data.service.ConfigService;
import com.odianyun.search.whale.es.request.ESSuggestRequest;
import com.odianyun.search.whale.index.api.common.IndexConstants;
import com.odianyun.search.whale.index.api.common.SuggestIndexConstants;
import com.odianyun.search.whale.index.api.common.SuggestIndexFieldConstants;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class AreaSuggestRequestBuilder implements RequestBuilder<ESSuggestRequest, SuggestRequest> {

	@Autowired
	ConfigService configService;

	@Override
	public void build(ESSuggestRequest esSuggestRequest, SuggestRequest suggestRequest) {
		String input = suggestRequest.getInput().toLowerCase();
		boolean distinguishCompany = configService.getBool("distinguish_company",false, IndexConstants.DEFAULT_COMPANY_ID);
		int companyId = -1;
		if(distinguishCompany){
			companyId = suggestRequest.getCompanyId();
		}
//		esSuggestRequest.setInput(SuggestIndexConstants.AREA_PREFIX + companyId +"_"+input);
		esSuggestRequest.setInput(input);
		List<String> companyIdList = new ArrayList<>();
		companyIdList.add(String.valueOf(companyId));
		esSuggestRequest.getCategoryMap().put(SuggestIndexFieldConstants.COMPANYID,companyIdList);

		List<String> merchantIdList = new ArrayList<>();
		merchantIdList.add(String.valueOf(-1));
		esSuggestRequest.getCategoryMap().put(SuggestIndexFieldConstants.MERCHANTID,merchantIdList);

		List<String> isArea = new ArrayList<>();
		isArea.add(String.valueOf(suggestRequest.getType().getCode()));
		esSuggestRequest.getCategoryMap().put(SuggestIndexFieldConstants.ISAREA,isArea);


		esSuggestRequest.setField(SuggestIndexFieldConstants.SUGGEST);
	}

}
