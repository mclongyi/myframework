package com.odianyun.search.whale.history.server;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.odianyun.search.whale.api.model.req.HistoryCleanRequest;
import com.odianyun.search.whale.api.model.req.HistoryReadRequest;
import com.odianyun.search.whale.api.model.resp.HistoryResponse;
import com.odianyun.search.whale.common.util.GsonUtil;
import com.odianyun.search.whale.common.util.HttpClientUtil;

@Controller
public class RestHistoryService {
	
	@Autowired
	HistoryServer historyServer;
	
	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/history")
	public String history(Integer companyId, String userId, Integer type, Integer count) throws Exception{	
		HistoryResponse response = null;
		try {		
			HistoryReadRequest request = new HistoryReadRequest(companyId,userId,count);
			response = historyServer.autoSearchHistory(request);
			
		} catch (Exception e) {
			throw e;
		} finally{

		}
		
		return GsonUtil.getGson().toJson(response);
	}
	
	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/cleanHistory")
	public String cleanHistory(Integer companyId, String userId, String keyword) throws Exception{	
		try {		
			List<String> keywords = new ArrayList<>();
			String[] temp = keyword.split(",");
			for(String word : temp){
				keywords.add(word);
			}
			HistoryCleanRequest request = new HistoryCleanRequest(companyId,userId,keywords);
			historyServer.cleanSearchHistory(request);
			
		} catch (Exception e) {
			throw e;
		} finally{

		}
		
		return HttpClientUtil.http_resp_successful;
	}
}
