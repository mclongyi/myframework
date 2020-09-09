package com.odianyun.search.whale.suggest.resp.handler;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.suggest.SuggestResponse;
import org.elasticsearch.search.suggest.Suggest.Suggestion;
import org.elasticsearch.search.suggest.Suggest.Suggestion.Entry;
import org.elasticsearch.search.suggest.Suggest.Suggestion.Entry.Option;

import com.odianyun.search.whale.es.api.ESService;

public class SpellCheckResponseHandler implements ResponseHandler<SuggestResponse, com.odianyun.search.whale.api.model.resp.SpellCheckerResponse>{

	@Override
	public void handle(SuggestResponse esResponse, com.odianyun.search.whale.api.model.resp.SpellCheckerResponse response)
			throws Exception {
		Suggestion<? extends Entry<? extends Option>> suggest = esResponse.getSuggest().getSuggestion(ESService.DEFAULT_SPELLCHECK_NAME);

		Map<String, List<String>> result = new HashMap<String, List<String>>();
		if (suggest != null) {
			for (Suggestion.Entry<? extends Option> entry : suggest) {
				String token = entry.getText().string();
				List<String> opts = new LinkedList<String>();
				for (Option op : entry.getOptions()) {
					opts.add(op.getText().string());
				}
				result.put(token, opts);
			}
		}

		response.setResults(result);
	}

}
