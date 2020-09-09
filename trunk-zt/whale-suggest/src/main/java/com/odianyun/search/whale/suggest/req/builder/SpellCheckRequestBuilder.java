package com.odianyun.search.whale.suggest.req.builder;

import com.odianyun.search.whale.api.model.req.SpellCheckerRequest;
import com.odianyun.search.whale.es.request.ESSpellCheckRequest;
import com.odianyun.search.whale.index.api.common.IndexConstants;
import com.odianyun.search.whale.index.api.common.IndexFieldConstants;

/**
 * 构建面向业务的拼写检查请求
 *
 * @author jing.liu
 *
 */
public class SpellCheckRequestBuilder implements RequestBuilder<ESSpellCheckRequest, SpellCheckerRequest> {

	@Override
	public void build(ESSpellCheckRequest esRequest, SpellCheckerRequest request) {
		esRequest.setField(IndexFieldConstants.TAG_WORDS);
//		esRequest.setIndexName(IndexConstants.index_alias);
//		esRequest.setType(IndexConstants.index_type);
		esRequest.setInput(request.getInput());
	}
}
