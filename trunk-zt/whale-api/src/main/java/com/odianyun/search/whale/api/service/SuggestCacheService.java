package com.odianyun.search.whale.api.service;


import java.util.List;

import com.odianyun.search.whale.api.model.req.HotWordRequest;
import com.odianyun.search.whale.api.model.resp.HotWordResponse;
import com.odianyun.soa.InputDTO;
import com.odianyun.soa.OutputDTO;
import com.odianyun.soa.SoaUtil;
import org.apache.commons.lang.StringUtils;

import com.odianyun.search.whale.api.common.SearchException;
import com.odianyun.search.whale.api.model.req.SpellCheckerRequest;
import com.odianyun.search.whale.api.model.req.SuggestRequest;
import com.odianyun.search.whale.api.model.resp.AreaSuggestResponse;
import com.odianyun.search.whale.api.model.resp.SpellCheckerResponse;
import com.odianyun.search.whale.api.model.resp.SuggestResponse;
import org.apache.log4j.Logger;

public class SuggestCacheService implements SuggestService {

	private Logger logger = Logger.getLogger(this.getClass());

	SuggestService suggestService;

	public SuggestCacheService(SuggestService suggestService){
		this.suggestService = suggestService;
	}

	@Override
	public SuggestResponse autoComplete(SuggestRequest request) throws SearchException {
		if(StringUtils.isBlank(request.getInput())){
			throw new SearchException("input cann't be null");
		}
		return suggestService.autoComplete(request);
	}

	@Deprecated
	@Override
	public List<String> recommendWordsWithZero(SuggestRequest request) throws SearchException {
		if(StringUtils.isBlank(request.getInput())){
			throw new SearchException("input cann't be null");
		}
		return suggestService.recommendWordsWithZero(request);
	}

	@Override
	public AreaSuggestResponse areaAutoComplete(SuggestRequest request) throws SearchException {
		if(StringUtils.isBlank(request.getInput())){
			throw new SearchException("input cann't be null");
		}
		return suggestService.areaAutoComplete(request);
	}

	@Override
	public SpellCheckerResponse spellcheck(SpellCheckerRequest request) throws SearchException {
		if(StringUtils.isBlank(request.getInput())){
			throw new SearchException("input cann't be null");
		}
		return this.suggestService.spellcheck(request);
	}


	//----------------------标准化改造---------------------------------

	@Override
	public OutputDTO<SuggestResponse> autoCompleteStandard(InputDTO<SuggestRequest> inputDTO) throws SearchException {
//		logger.info(errorsoa 调用开始......" + inputDTO.toString());
		SuggestResponse suggestResponse = null;
		try {
			suggestResponse = autoComplete(inputDTO.getData());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("autoCompleteStandard调用失败," + inputDTO.toString() + e);
		}
//		logger.info("soa 调用结束......" + suggestResponse);
		return SoaUtil.resultSucess(suggestResponse);
	}

	@Override
	public OutputDTO<AreaSuggestResponse> areaAutoCompleteStandard(InputDTO<SuggestRequest> inputDTO) throws SearchException {
//		logger.info("soa 调用开始......" + inputDTO.toString());
		AreaSuggestResponse areaSuggestResponse = null;
		try {
			areaSuggestResponse = areaAutoComplete(inputDTO.getData());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("areaAutoCompleteStandard调用失败," + inputDTO.toString() + e);
		}
//		logger.info("soa 调用结束......" + areaSuggestResponse);
		return SoaUtil.resultSucess(areaSuggestResponse);
	}


	@Override
	public OutputDTO<SpellCheckerResponse> spellcheckStandard(InputDTO<SpellCheckerRequest> inputDTO) throws SearchException {
//		logger.info("soa 调用开始......" + inputDTO.toString());
		SpellCheckerResponse spellcheck = null;
		try {
			spellcheck = spellcheck(inputDTO.getData());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("autoCompleteStandard调用失败," + inputDTO.toString() + e);
		}
//		logger.info("soa 调用结束......" + spellcheck);
		return SoaUtil.resultSucess(spellcheck);
	}

	@Override
	public OutputDTO<HotWordResponse> getHotwordDistinct(InputDTO<HotWordRequest> inputDTO) throws SearchException {
		logger.error("soa 异常调用......" + inputDTO.getData());
		return null;
	}

}
