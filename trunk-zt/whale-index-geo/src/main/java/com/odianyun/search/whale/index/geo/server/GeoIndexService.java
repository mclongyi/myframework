package com.odianyun.search.whale.index.geo.server;


import com.odianyun.search.whale.common.util.ConfigUtil;
import com.odianyun.search.whale.index.geo.GeoIndexFlowImpl;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.odianyun.search.whale.common.util.HttpClientUtil;
import com.odianyun.search.whale.data.manager.CompanyDBCacheManager;
import com.odianyun.search.whale.data.saas.model.CompanyAppType;
import com.odianyun.search.whale.data.saas.model.ESClusterConfig;
import com.odianyun.search.whale.data.saas.service.CompanyRoutingService;
import com.odianyun.search.whale.es.api.ESService;
import com.odianyun.search.whale.index.api.common.IndexConstants;
import com.odianyun.search.whale.index.api.common.MerchantAreaIndexContants;
import com.odianyun.search.whale.processor.IndexFlow;


@Controller
public class GeoIndexService {
	
	static Logger logger = Logger.getLogger(GeoIndexService.class);
	
	private boolean indexing = false;
	
	@Autowired
	GeoIndexFlowImpl geoIndexFlow;
	
	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/geo-fullindex")
	public String fullindex(@RequestParam(value = "isValidation",required = false,defaultValue="true") boolean isValidation,
			@RequestParam(value = "isSendIndexRequest",required = false,defaultValue="true") boolean isSendIndexRequest) throws Exception{
		if(indexing()){
			return "fullIndexing.........";
		}
		boolean isStartWithOplusO = ConfigUtil.getBool("isStartWithOplusO", true);
		logger.info("isStartWithOplusO fullindex : " + isStartWithOplusO);
		if(isStartWithOplusO){
			return "it is started with O+O model , please execute fullindex directly !!!";
		}
		try {
			geoIndexFlow.setIndexName(null);
			geoIndexFlow.init();
			Boolean processSuccess=geoIndexFlow.process();		
			logger.info("processSuccess="+processSuccess);
//			if((!isValidation||processSuccess)&&isSendIndexRequest){
			if(processSuccess && isSendIndexRequest){
				logger.info("SendIndexRequest=========");
				geoIndexFlow.done(isValidation);
			}
		} catch (Exception e) {
			throw e;
		} finally{
			geoIndexFlow.afterDone();
			indexing = false;
		}
		
		return HttpClientUtil.http_resp_successful;
	}
	
	public boolean indexing() {
		synchronized (GeoIndexService.class){
			if(indexing){
				return true;
			}
			indexing = true;
			return false;
		}
	}

	
	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/geo-updateAlias")
	public String updateAlias(@RequestParam(value = "indexName",required = true) String indexName,@RequestParam(value = "companyId",required = true) Integer companyId) throws Exception{
		/*ESClusterConfig esClusterConfig = companyRoutingService.getCompanyESClusterConfig(companyId, CompanyAppType.O2O);
		if(null != esClusterConfig){
			ESService.updateAlias(esClusterConfig.getAdminUrl(),indexName, IndexConstants.index_alias);
		}else {
			return "esClusterConfig is null!!!";
		}*/
		ESService.updateAlias(indexName, MerchantAreaIndexContants.index_alias);

		return HttpClientUtil.http_resp_successful;
	}
	
	
	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/geo-reloadCache")
	public String reloadCache(@RequestParam(value = "name",required = false) String name,
			@RequestParam(value = "companyId",required = true) int companyId) throws Exception{
		if(companyId==-1){
			CompanyDBCacheManager.instance.reloadAll();
		}else{
			if(StringUtils.isNotEmpty(name)){
				String[] names = name.split(",");
				for(String dbName : names){
					CompanyDBCacheManager.instance.reload(dbName,companyId);
				}
			}
		}
		return HttpClientUtil.http_resp_successful;
	}

}
