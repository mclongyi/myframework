package com.odianyun.search.whale.suggest.resp.handler;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.elasticsearch.action.suggest.SuggestResponse;
import org.elasticsearch.search.suggest.Suggest.Suggestion.Entry;
import org.elasticsearch.search.suggest.Suggest.Suggestion.Entry.Option;

import com.odianyun.search.whale.api.model.resp.AreaResult;
import com.odianyun.search.whale.index.api.common.SuggestIndexFieldConstants;

public class AreaSuggestResponseHandler implements ResponseHandler<SuggestResponse, com.odianyun.search.whale.api.model.resp.AreaSuggestResponse> {

	@Override
	public void handle(SuggestResponse esSuggestResponse, com.odianyun.search.whale.api.model.resp.AreaSuggestResponse suggestResponse) throws Exception {
		List<? extends Entry<? extends Option>> list = esSuggestResponse.getSuggest().getSuggestion(SuggestIndexFieldConstants.SUGGEST).getEntries();
		if(CollectionUtils.isEmpty(list)){
			return ;
		}
		List<AreaResult> areaResult	 = new ArrayList<>();
		for (Entry<? extends Option> e : list) {
			for (Option option : e) {
				String optionString = option.getText().toString();
				String[] optionStrings = optionString.split("_");
				if(optionStrings.length != 2){
					continue;
				}
				AreaResult result = new AreaResult();
				result.setAreaName(optionStrings[0]);
				result.setAreaCode(Long.valueOf(optionStrings[1]));
				areaResult.add(result);
			}
		}

		suggestResponse.setAreaResult(areaResult);
	}

}
