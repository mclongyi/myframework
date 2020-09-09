package com.odianyun.search.whale.api.service;

import java.util.List;

import com.odianyun.search.whale.api.common.SearchException;
import com.odianyun.search.whale.api.model.req.HotSearchRequest;
import com.odianyun.search.whale.api.model.req.HotWordRequest;
import com.odianyun.search.whale.api.model.req.SpellCheckerRequest;
import com.odianyun.search.whale.api.model.req.SuggestRequest;
import com.odianyun.search.whale.api.model.resp.AreaSuggestResponse;
import com.odianyun.search.whale.api.model.resp.HotWordResponse;
import com.odianyun.search.whale.api.model.resp.SpellCheckerResponse;
import com.odianyun.search.whale.api.model.resp.SuggestResponse;
import com.odianyun.soa.InputDTO;
import com.odianyun.soa.OutputDTO;

public interface SuggestService {

	/**
	 * 自动提示
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	SuggestResponse autoComplete(SuggestRequest request) throws SearchException;

	/**
	 * 零结果推荐接口
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	List<String> recommendWordsWithZero(SuggestRequest request) throws SearchException;

	/**
	 * 区域自动提示
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	AreaSuggestResponse areaAutoComplete(SuggestRequest request) throws SearchException;

	/**
	 * 关键字拼写检查
	 *
	 * @param request
	 * @return
	 * @throws SearchException
	 */
	@Deprecated
	SpellCheckerResponse spellcheck(SpellCheckerRequest request) throws SearchException;

	//------------------------SOA标准化改造-----------------------
	/**
	 * 自动提示
	 * @return
	 * @throws Exception
	 */
	OutputDTO<SuggestResponse> autoCompleteStandard(InputDTO<SuggestRequest> inputDTO) throws SearchException;

	/**
	 * 区域自动提示
	 * @param inputDTO
	 * @return
	 * @throws Exception
	 */
	OutputDTO<AreaSuggestResponse> areaAutoCompleteStandard(InputDTO<SuggestRequest> inputDTO) throws SearchException;

	/**
	 * 关键字拼写检查
	 *
	 * @param inputDTO
	 * @return
	 * @throws SearchException
	 */
	OutputDTO<SpellCheckerResponse> spellcheckStandard(InputDTO<SpellCheckerRequest> inputDTO) throws SearchException;


	//热词查询
	OutputDTO<HotWordResponse> getHotwordDistinct(InputDTO<HotWordRequest> inputDTO) throws SearchException;
}
