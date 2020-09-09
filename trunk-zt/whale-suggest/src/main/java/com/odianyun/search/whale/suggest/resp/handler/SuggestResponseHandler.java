package com.odianyun.search.whale.suggest.resp.handler;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.elasticsearch.action.suggest.SuggestResponse;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.Suggest.Suggestion.Entry;
import org.elasticsearch.search.suggest.Suggest.Suggestion.Entry.Option;

import com.odianyun.search.whale.api.model.resp.SuggestResult;
import com.odianyun.search.whale.index.api.common.SuggestIndexFieldConstants;

public class SuggestResponseHandler implements ResponseHandler<SuggestResponse, com.odianyun.search.whale.api.model.resp.SuggestResponse> {

	@Override
	public void handle(SuggestResponse esSuggestResponse, com.odianyun.search.whale.api.model.resp.SuggestResponse suggestResponse) throws Exception {
		Suggest suggest=esSuggestResponse.getSuggest();
		if(suggest==null){
			return;
		}
		Suggest.Suggestion suggestion=suggest.getSuggestion(SuggestIndexFieldConstants.SUGGEST);
		if(suggestion==null){
			return;
		}
		List<? extends Entry<? extends Option>> list = suggestion.getEntries();
		if(CollectionUtils.isEmpty(list)){
			return ;
		}
		List<SuggestResult> suggestResult = new ArrayList<>();
		for (Entry<? extends Option> e : list) {
			for (Option option : e) {
				String optionString = option.getText().toString();
				SuggestResult result = new SuggestResult(optionString, (int) option.getScore());
				suggestResult.add(result);
			}
		}
		suggestResponse.setSuggestResult(suggestResult);
	}

}
