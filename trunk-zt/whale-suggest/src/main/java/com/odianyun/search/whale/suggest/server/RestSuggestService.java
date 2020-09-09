package com.odianyun.search.whale.suggest.server;

import org.elasticsearch.common.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.odianyun.search.whale.api.model.req.SpellCheckerRequest;
import com.odianyun.search.whale.api.model.req.SuggestRequest;
import com.odianyun.search.whale.api.model.resp.AreaSuggestResponse;
import com.odianyun.search.whale.api.model.resp.SpellCheckerResponse;
import com.odianyun.search.whale.api.model.resp.SuggestResponse;
import com.odianyun.search.whale.common.util.GsonUtil;
import com.odianyun.search.whale.suggest.handler.AreaSuggestHandler;
import com.odianyun.search.whale.suggest.handler.SuggestHandler;

@Controller
public class RestSuggestService {

	@Autowired
	SuggestServer suggestServer;

	@Autowired
	SuggestHandler suggestHandler;

	@Autowired
	AreaSuggestHandler areaSuggestHandler;

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/suggest")
	public String suggest(Integer companyId, String keyword, Integer count) throws Exception{
		SuggestResponse response = null;
		try {
			SuggestRequest request = new SuggestRequest(companyId,keyword);
			request.setCount(count);
			response = suggestServer.autoComplete(request);
		} catch (Exception e) {
			throw e;
		} finally{

		}

		return GsonUtil.getGson().toJson(response);
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/areaAutoComplete")
	public String areaAutoComplete(Integer companyId, String keyword, Integer count) throws Exception{
		AreaSuggestResponse response = null;
		try {
			SuggestRequest request = new SuggestRequest(companyId,keyword);
			request.setCount(count);
			response = suggestServer.areaAutoComplete(request);
		} catch (Exception e) {
			throw e;
		} finally{

		}

		return GsonUtil.getGson().toJson(response);
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/cleanCache.json")
	public String cleanCache(String cleanType) throws Exception{
		if(StringUtils.isBlank(cleanType)){
			return "cleanType cann't be null !!!";
		}
		if(cleanType.equals("suggest")){
			suggestHandler.cleanCache();
		}else if(cleanType.equals("area")){
			areaSuggestHandler.cleanCache();
		}else if(cleanType.equals("all")){
			suggestHandler.cleanCache();
			areaSuggestHandler.cleanCache();
		}
		return GsonUtil.getGson().toJson("clean successfully!!!");
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/spell")
	public SpellCheckerResponse spellcheck(@RequestBody SpellCheckerRequest request) throws Exception {
		return this.suggestServer.spellcheck(request);
	}
}
