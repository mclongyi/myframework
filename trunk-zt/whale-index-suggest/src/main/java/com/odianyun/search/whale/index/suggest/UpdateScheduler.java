package com.odianyun.search.whale.index.suggest;

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
import com.odianyun.search.whale.index.api.common.SuggestIndexConstants;
import com.odianyun.search.whale.index.suggest.server.RestSuggestService;

public class UpdateScheduler {
	
	static Logger logger = Logger.getLogger(UpdateScheduler.class);
	SimpleDateFormat indexDateFormat=new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");

	Pattern indexDatePattern = Pattern.compile("(\\d{4}-\\d{2}-\\d{2}_\\d{2}-\\d{2}-\\d{2})",
			Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
	static long HOUR = 3600*1000;
	@Autowired
	ConfigService configService;
	@Autowired
	RestSuggestService indexService;
	@Autowired
	CompanyService companyService;
	
	boolean runFullIndex = true;
	
	int companyId=-1;
	
	public void start() {
		logger.info("suggest UpdateScheduler start ...........................");
//		companyId = ConfigUtil.getInt("company_id", 1001);
//		logger.info("suggest companyId : " + companyId);
		startFullIndex();
		startIndexCheck();
		logger.info("suggest UpdateScheduler start end...........................");

	}

	private void startFullIndex() {
		Thread fullIndexThread = new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					try {
						Long sleep_time=configService.getLong("suggest.fullindex.interval_time", 24*3600*1000,companyId);
						Thread.sleep(sleep_time);
						logger.info("start suggest fullindex");
						indexService.fullindex(true, true);
						logger.info("end suggest fullindex");
					} catch (InterruptedException e) {
						logger.error(e.getMessage(), e);
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
					}

				}

			}
		});
		fullIndexThread.setDaemon(true);
		fullIndexThread.setName("suggest-fullIndexThread");
		fullIndexThread.start();
	}
	
	private void startIndexCheck() {
		Thread indexCheckThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						Long sleep_time=configService.getLong("suggest.fullindex.interval_time", 24*3600*1000,companyId) + HOUR/2;
						Thread.sleep(sleep_time);
						logger.info("start suggest indexCheckThread");
						indexCheck(sleep_time);
						logger.info("end suggest indexCheckThread");
					} catch (InterruptedException e) {
						logger.error(e.getMessage(), e);
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
					}
				}
			}
		});
		indexCheckThread.setDaemon(true);
		indexCheckThread.setName("suggest-indexCheckThread");
		indexCheckThread.start();
		
	}

	public boolean isRunFullIndex() {
		return runFullIndex;
	}

	public void setRunFullIndex(boolean runFullIndex) {
		this.runFullIndex = runFullIndex;
	}
	
	private void indexCheck(long sleep_time) throws Exception{
		Date now = new Date();
		List<String> indexNames=ESService.getIndexNameByAlias(SuggestIndexConstants.index_alias);
		String currentIndexName="";
		if(indexNames!=null&&indexNames.size()>0){
			currentIndexName=indexNames.get(0);
			logger.info("suggest alias now poits to index "+ currentIndexName);
		}
		if(StringUtils.isBlank(currentIndexName)){
			logger.error("suggest alias does not poit to any index "+ currentIndexName);
			return;
		}
		Matcher m = indexDatePattern.matcher(currentIndexName);
		if(m.find()) {
			currentIndexName = m.group(0);
		} else {
			logger.error("can't parsre suggest index time from " + currentIndexName);
			return;
		}
		Date date = indexDateFormat.parse(currentIndexName);
		long timeSpan = now.getTime() - date.getTime();
		if(timeSpan >= sleep_time){
			logger.error("The suggest alias has not switch for more than " + (double)timeSpan/HOUR +" hours ");
		}else{
			logger.info("suggest index version check ok !!!");
		}
	}

}
