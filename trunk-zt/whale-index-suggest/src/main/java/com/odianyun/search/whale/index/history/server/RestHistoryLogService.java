package com.odianyun.search.whale.index.history.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.odianyun.search.whale.common.util.HttpClientUtil;
import com.odianyun.search.whale.index.api.model.req.HistoryType;
import com.odianyun.search.whale.index.api.model.req.HistoryWriteRequest;

@Controller
public class RestHistoryLogService {
	
	@Autowired
	HistoryLogServer historyLogServer;
	
	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/logHistory")
	public String logHistory(Integer companyId, String userId, String keyword, Integer type, Integer total, Integer frequency) throws Exception{	
		try {		
			HistoryType historyType = null;
			if(type != null){
				if(type == HistoryType.MERCHANT.getCode()){
					historyType = HistoryType.MERCHANT;
				}else if(type == HistoryType.SEARCH.getCode()){
					historyType = HistoryType.SEARCH;
				}
			}
			HistoryWriteRequest request = new HistoryWriteRequest(companyId,userId,keyword,historyType);
			request.setFrequency(frequency);
			request.setResultCount(total);
			historyLogServer.logSearchHistory(request);
			
		} catch (Exception e) {
			throw e;
		} finally{

		}
		
		return HttpClientUtil.http_resp_successful;
	}
}
