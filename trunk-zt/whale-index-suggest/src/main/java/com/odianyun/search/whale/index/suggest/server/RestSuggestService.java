package com.odianyun.search.whale.index.suggest.server;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.odianyun.search.whale.common.util.HttpClientUtil;
import com.odianyun.search.whale.processor.IndexFlow;

@Controller
public class RestSuggestService {
	
	static Logger logger = Logger.getLogger(RestSuggestService.class);

	private boolean indexing = false; 

	@Autowired
	IndexFlow suggestWordIndexFlowImpl;
	
	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/suggest-fullindex")
	public String fullindex(@RequestParam(value = "isValidation",required = false,defaultValue="true") boolean isValidation,
			@RequestParam(value = "isSendIndexRequest",required = false,defaultValue="true") boolean isSendIndexRequest) throws Exception{
		try {		
			if(indexing()){
				return "fullIndexing.........";
			}			
			suggestWordIndexFlowImpl.init();
			Boolean processSuccess=suggestWordIndexFlowImpl.process();		
			logger.info("processSuccess="+processSuccess);
//			if((!isValidation||processSuccess)&&isSendIndexRequest){
			if(processSuccess && isSendIndexRequest){
				logger.info("SendIndexRequest=========");
				suggestWordIndexFlowImpl.done(isValidation);
			}
		} catch (Exception e) {
			throw e;
		} finally{
			indexing = false;
		}
		
		return HttpClientUtil.http_resp_successful;
	}

	public boolean indexing() {
		synchronized (RestSuggestService.class){
			if(indexing){
				return true;
			}
			indexing = true;
			return false;
		}
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/suggest-incindex")
	public String incindex(@RequestParam(value = "ids",required = true) String ids,
			@RequestParam(value = "isSendIndexRequest",required = false,defaultValue="false") boolean isSendIndexRequest) throws Exception{
		
		return "";
	}
}
