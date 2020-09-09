package com.odianyun.search.whale.index.server;

import java.util.ArrayList;
import java.util.List;

import com.odianyun.search.whale.common.util.ConfigUtil;
import com.odianyun.search.whale.index.geo.GeoIndexFlowImpl;
import com.odianyun.search.whale.index.opluso.full.OplusOIndexFlowImpl;
import com.odianyun.search.whale.processor.IndexFlow;
import org.apache.commons.collections.CollectionUtils;
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
import com.odianyun.search.whale.data.manager.DBCacheManager;
import com.odianyun.search.whale.data.manager.SegmentManager;
import com.odianyun.search.whale.es.api.ESService;
import com.odianyun.search.whale.index.api.common.IndexConstants;
import com.odianyun.search.whale.index.api.common.UpdateType;
import com.odianyun.search.whale.index.business.process.build.IncIndexProcessorBuilder;
import com.odianyun.search.whale.index.convert.IDConverterManager;
import com.odianyun.search.whale.index.full.MerchantProductIndexFlowImpl;
import com.odianyun.search.whale.index.full.MerchantProductSparkIndexFlowImpl;
import com.odianyun.search.whale.index.realtime.MerchantProductIncIndex;
import com.odianyun.search.whale.index.realtime.UpdateScheduler;


@Controller
public class IndexService {

	static Logger logger = Logger.getLogger(IndexService.class);

	private volatile boolean idIndexing = false;

	@Autowired
	MerchantProductIndexFlowImpl merchantProductIndexFlow;

	@Autowired
	OplusOIndexFlowImpl oplusoIndexFlow;

	@Autowired
	MerchantProductIncIndex merchantProductIncIndex;

	@Autowired
	UpdateScheduler updateScheduler;

	@Autowired
	MerchantProductSparkIndexFlowImpl sparkIndexFlow;

	@Autowired
	GeoIndexFlowImpl geoIndexFlow;

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/fullindex")
	public String fullindex(@RequestParam(value = "isValidation",required = false,defaultValue="true") boolean isValidation,
			@RequestParam(value = "isSendIndexRequest",required = false,defaultValue="true") boolean isSendIndexRequest) throws Exception{
		if(indexing()){
			return "fullIndexing.........";
		}
		boolean isStartWithOplusO = ConfigUtil.getBool("isStartWithOplusO", true);
		logger.info("isStartWithOplusO fullindex : " + isStartWithOplusO);
		IndexFlow indexFlow = merchantProductIndexFlow;
		if(isStartWithOplusO){
			indexFlow = oplusoIndexFlow;
		}
		doIndex(indexFlow,isSendIndexRequest,isValidation);

		return HttpClientUtil.http_resp_successful;
	}

	private void doIndex(IndexFlow indexFlow, boolean isSendIndexRequest, boolean isValidation) throws Exception {
		try {
			indexFlow.init();
			Boolean processSuccess=indexFlow.process();
			logger.info("processSuccess="+processSuccess);
			if(processSuccess && isSendIndexRequest){
				logger.info("SendIndexRequest=========");
				indexFlow.done(isValidation);
			}
		}catch (Exception e){
			logger.error("索引创建异常:"+e);
			throw new RuntimeException(e);
		}finally{
			try{
				indexFlow.afterDone();
			}catch(Exception e){
				logger.error(e.getMessage(),e);
			}
			idIndexing = false;
		}
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/update-geo-fullindex")
	public String updateGeoFullIndex() throws Exception {

		if (indexing()) {
			return "fullIndexing.........";
		}
		boolean isStartWithOplusO = ConfigUtil.getBool("isStartWithOplusO", true);
		logger.info("isStartWithOplusO fullindex : " + isStartWithOplusO);
		if (isStartWithOplusO) {
			this.geoIndexFlow.reloadCache();
			this.geoIndexFlow.process();
		} else {
			return "it's not started with O+O ,please try geo-fullindex";
		}

		return "{'operate':'successful'}";
	}
	
	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/fullindexOnSpark")
	public String fullindexOnSpark(
            @RequestParam(value = "isValidation",required = false,defaultValue="true") boolean isValidation,
			@RequestParam(value = "isSendIndexRequest",required = false,defaultValue="true") boolean isSendIndexRequest) throws Exception{
		if(indexing()){
			return "fullIndexing.........";
		}
		try {
			sparkIndexFlow.init();
			Boolean processSuccess=sparkIndexFlow.process();
			logger.info("processSuccess="+processSuccess);
			if(processSuccess && isSendIndexRequest){
				sparkIndexFlow.done(isValidation);
			}
		}finally{
			idIndexing = false;
		}

		return HttpClientUtil.http_resp_successful;
	}

	public boolean indexing() {
		synchronized (IndexService.class){
			if(idIndexing){
				return true;
			}
			idIndexing = true;
			return false;
		}

	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/incindex")
	public String incindex(@RequestParam(value = "ids",required = true) String ids,
			@RequestParam(value = "isSendIndexRequest",required = false,defaultValue="false") boolean isSendIndexRequest,
			@RequestParam(value = "companyId",required = true) int companyId) throws Exception{
		String[] idsArray=ids.split(",");
		List<Long> merchantProductIds=new ArrayList<Long>();
		for(String str:idsArray){
			merchantProductIds.add(Long.valueOf(str));
		}
		List<Long> convertIds = IDConverterManager.instanse.convert(merchantProductIds, UpdateType.merchant_product_id, companyId);
		return merchantProductIncIndex.process(convertIds, isSendIndexRequest,
				IndexConstants.index_alias,IndexConstants.index_type,companyId);
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/updateAlias")
	public String updateAlias(@RequestParam(value = "indexName",required = true) String indexName) throws Exception{
		openIndex(indexName);
		List<String> indexNameList = ESService.getIndexNameByAlias(IndexConstants.index_alias);
		ESService.updateAlias(indexName, IndexConstants.index_alias);
		if(CollectionUtils.isNotEmpty(indexNameList)){
			cloesIndex(indexNameList.get(0));
		}
		return HttpClientUtil.http_resp_successful;
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/controlFullIndex")
	public String controlFullIndex(@RequestParam(value = "runFullIndex",required = true) Boolean runFullIndex) throws Exception{
		updateScheduler.setRunFullIndex(runFullIndex);
		return HttpClientUtil.http_resp_successful;
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/controlIncIndex")
	public String controlIncIndex(@RequestParam(value = "runIncIndex",required = true) Boolean runIncIndex) throws Exception{
		updateScheduler.setRunIncIndex(runIncIndex);
		return HttpClientUtil.http_resp_successful;
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/reloadCache")
	public String reloadCache(@RequestParam(value = "name",required = false) String name,
			@RequestParam(value = "reloadAll",required = false,defaultValue="false") Boolean reloadAll,
			@RequestParam(value = "companyId",required = true) int companyId,@RequestParam(value = "dbManagerType",required = true) int dbManagerType) throws Exception{
		if(reloadAll != null && reloadAll == true){
			if(dbManagerType == 0){
				DBCacheManager.instance.reloadAll();
			}else{
				CompanyDBCacheManager.instance.reloadAll();
			}
		}else{
			if(StringUtils.isNotEmpty(name)){
				String[] names = name.split(",");
				for(String serviceName : names){
					if(dbManagerType == 0){
						DBCacheManager.instance.reload(serviceName);
					}else{
						CompanyDBCacheManager.instance.reload(serviceName,companyId);
					}
				}
			}
		}
		return HttpClientUtil.http_resp_successful;
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/reloadSegment")
	public String reloadSegment() throws Exception {
		SegmentManager.getInstance().reload();
		return HttpClientUtil.http_resp_successful;
	}
	
	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/registeIndexInfo")
	public String registeIndexInfo(@RequestParam(value = "indexName",required = true) String indexName,
			@RequestParam(value = "indexType",required = true) String indexType) throws Exception {
		IncIndexProcessorBuilder.registe(indexName, indexType);
		return HttpClientUtil.http_resp_successful;
	}
	
	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/removeIndexInfo")
	public String removeIndexInfo(@RequestParam(value = "indexName",required = true) String indexName,
			@RequestParam(value = "indexType",required = true) String indexType) throws Exception {
		IncIndexProcessorBuilder.remove(indexName, indexType);
		return HttpClientUtil.http_resp_successful;
	}
	
	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/clearIndexInfo")
	public String clearIndexInfo() throws Exception {
		IncIndexProcessorBuilder.clear();
		return HttpClientUtil.http_resp_successful;
	}
	
	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/openIndex")
	public String openIndex(@RequestParam(value = "indexNames",required = true) String indexNames) throws Exception {
		if(StringUtils.isBlank(indexNames)){
			return "indexNames is blank !!!!";
		}
		List<String> indexNameList = new ArrayList<String>();
		String[] names = indexNames.split(",");
		for(String name : names){
			indexNameList.add(name);
		}
		ESService.openIndices(indexNameList);
		return HttpClientUtil.http_resp_successful;
	}
	
	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/closeIndex")
	public String cloesIndex(@RequestParam(value = "indexNames",required = true) String indexNames) throws Exception {
		if(StringUtils.isBlank(indexNames)){
			return "indexNames is blank !!!!";
		}
		List<String> indexNameList = new ArrayList<String>();
		String[] names = indexNames.split(",");
		for(String name : names){
			indexNameList.add(name);
		}
		ESService.closeIndices(indexNameList);
		return HttpClientUtil.http_resp_successful;
	}
}
