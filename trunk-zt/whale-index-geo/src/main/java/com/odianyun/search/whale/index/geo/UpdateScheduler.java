package com.odianyun.search.whale.index.geo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.elasticsearch.common.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.odianyun.search.whale.common.util.ConfigUtil;
import com.odianyun.search.whale.data.saas.model.Company;
import com.odianyun.search.whale.data.saas.model.CompanyAppType;
import com.odianyun.search.whale.data.saas.model.ESClusterConfig;
import com.odianyun.search.whale.data.saas.service.CompanyRoutingService;
import com.odianyun.search.whale.data.saas.service.CompanyService;
import com.odianyun.search.whale.data.service.ConfigService;
import com.odianyun.search.whale.es.api.ESService;
import com.odianyun.search.whale.index.api.common.MerchantAreaIndexContants;
import com.odianyun.search.whale.index.geo.server.GeoIndexService;

public class UpdateScheduler {
	
	static Logger logger = Logger.getLogger(UpdateScheduler.class);
	SimpleDateFormat indexDateFormat=new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");

	Pattern indexDatePattern = Pattern.compile("(\\d{4}-\\d{2}-\\d{2}_\\d{2}-\\d{2}-\\d{2})",
			Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
	static long HOUR = 3600*1000;
	@Autowired
	ConfigService configService;
//	@Autowired
//	CompanyRoutingService companyRoutingService;
	@Autowired
	CompanyService companyService;
	@Autowired
	GeoIndexService geoIndexService;
	
	int companyId=-1;

	public void start() {
		try {
			ConfigUtil.loadPropertiesFile("spark.properties");
		} catch (Exception e) {
			logger.warn("load properties failed", e);
		}
		boolean is_start_fullIndex = ConfigUtil.getBool("is_start_geo", false);
		boolean isStartWithOplusO = ConfigUtil.getBool("isStartWithOplusO", true);

		if(!is_start_fullIndex || isStartWithOplusO){
			logger.info("no need to start geo index scheduler --------------------------------");
			return;
		}
		logger.info("geo UpdateScheduler start init--------------------------------------------");
//		companyId = ConfigUtil.getInt("company_id", 1001);
//		logger.info("geo companyId : " + companyId);
		startFullIndex();
		startIndexCheck();
		logger.info("geo UpdateScheduler end init-----------------------------------------------");
	}

	private void startFullIndex() {
		Thread fullIndexThread = new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					try {
						Long sleep_time=configService.getLong("geo.fullindex.interval_time", 1*HOUR,companyId);
						Thread.sleep(sleep_time);
						logger.info("start geo fullindex");
						geoIndexService.fullindex(true, true);
						logger.info("end geo fullindex");

					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						logger.error(e.getMessage(), e);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logger.error(e.getMessage(), e);
					}

				}

			}
		});
		fullIndexThread.setDaemon(true);
		fullIndexThread.setName("geo-fullIndexThread");
		fullIndexThread.start();
	}
	
	private void startIndexCheck() {
		Thread indexCheckThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						Long sleep_time=configService.getLong("geo.fullindex.interval_time", 1*HOUR,companyId) + HOUR/2;
						Thread.sleep(sleep_time);
						logger.info("start geo indexCheckThread");
						indexCheck(sleep_time);
						logger.info("end geo indexCheckThread");
					} catch (InterruptedException e) {
						logger.error(e.getMessage(), e);
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
					}
				}
			}
		});
		indexCheckThread.setDaemon(true);
		indexCheckThread.setName("geo-indexCheckThread");
		indexCheckThread.start();
		
	}

	private void indexCheck(long sleep_time) throws Exception{
		Date now = new Date();
		List<String> indexNames=ESService.getIndexNameByAlias(MerchantAreaIndexContants.index_alias);
		String currentIndexName="";
		if(indexNames!=null&&indexNames.size()>0){
			currentIndexName=indexNames.get(0);
			logger.info("geo alias now poits to index "+ currentIndexName);
		}
		if(StringUtils.isBlank(currentIndexName)){
			logger.error("geo alias does not poit to any index "+ currentIndexName);
			return;
		}
		Matcher m = indexDatePattern.matcher(currentIndexName);
		if(m.find()) {
			currentIndexName = m.group(0);
		} else {
			logger.error("can't parsre geo index time from " + currentIndexName);
			return;
		}
		Date date = indexDateFormat.parse(currentIndexName);
		long timeSpan = now.getTime() - date.getTime();
		if(timeSpan >= sleep_time){
			logger.error("The geo alias has not switch for more than " + (double)timeSpan/HOUR +" hours ");
		}else{
			logger.info("geo index version check ok !!!");
		}
	}
}
