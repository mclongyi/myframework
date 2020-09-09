package com.odianyun.search.whale.index.realtime;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.odianyun.search.whale.index.api.common.OplusOIndexConstants;
import org.apache.log4j.Logger;
import org.elasticsearch.common.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;

import com.odianyun.search.whale.common.util.ConfigUtil;
import com.odianyun.search.whale.data.common.DBManagerInitedEvent;
import com.odianyun.search.whale.data.manager.UpdateConsumer;
import com.odianyun.search.whale.data.saas.service.CompanyService;
import com.odianyun.search.whale.data.service.ConfigService;
import com.odianyun.search.whale.es.api.ESService;
import com.odianyun.search.whale.index.api.common.IndexConstants;
import com.odianyun.search.whale.index.history.server.SearchHistoryConsumer;
import com.odianyun.search.whale.index.schedule.FullIndexScheduler;
import com.odianyun.search.whale.index.schedule.RealTimeIndexScheduler;
import com.odianyun.search.whale.index.server.IndexService;
import com.odianyun.search.whale.processor.IndexFlow;

public class UpdateScheduler implements ApplicationListener<DBManagerInitedEvent>{
	
	static Logger logger = Logger.getLogger(UpdateScheduler.class);
	
	@Autowired
	IndexFlow merchantProductIndexFlow;

	@Autowired
	IndexService indexService;
	
	@Autowired
	ConfigService configService;
	
	@Autowired
	UpdateConsumer updateConsumer;
	
	@Autowired
	SearchHistoryConsumer searchHistoryConsumer;

    @Autowired
    FullIndexScheduler fullIndexScheduler;

    @Autowired
    RealTimeIndexScheduler realTimeIndexScheduler;
    
    @Autowired
	CompanyService companyService;
	
	boolean runFullIndex = true;
	
	boolean runIncIndex = true;
		
	SimpleDateFormat indexDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
	
	static long HOUR = 3600*1000;

	Pattern indexDatePattern = Pattern.compile("(\\d{4}-\\d{2}-\\d{2}_\\d{2}-\\d{2}-\\d{2})",
			Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);

	public void start() {
		try {
			ConfigUtil.loadPropertiesFile("spark.properties");
		} catch (Exception e) {
			logger.warn("load properties failed", e);
		}
		
		boolean isStartWithSpark = ConfigUtil.getBool("isStartWithSpark", false);
		logger.info("isStartWithSpark: "+isStartWithSpark);

		if (isStartWithSpark) {
			startFullIndexOnSpark();
			startIncIndexOnSpark();
		} else {
			startFullIndex();
//			startIncIndex();
			startIndexCheck();
		}
	}

	private void startIndexCheck() {
		Thread indexCheckThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						Long sleep_time=configService.getLong("fullindex.interval_time", 24*HOUR,-1) + HOUR/2;
						Thread.sleep(sleep_time);
						logger.info("start indexCheckThread");
						indexCheck(sleep_time);
						logger.info("end indexCheckThread");
					} catch (InterruptedException e) {
						logger.error(e.getMessage(), e);
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
					}
				}
			}
		});
		indexCheckThread.setDaemon(true);
		indexCheckThread.setName("indexCheckThread");
		indexCheckThread.start();
		
	}

	private void startFullIndex() {
		Thread fullIndexThread = new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					try {
//						Long sleep_time=configService.getLong("fullindex.interval_time", 24*HOUR,-1);
						Long sleep_time=24*HOUR;
						Thread.sleep(sleep_time);
						if(runFullIndex) {
							logger.info("start fullindex");
							indexService.fullindex(true, true);
							logger.info("end fullindex");
						}
					} catch (InterruptedException e) {
						logger.error(e.getMessage(), e);
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
					}

				}

			}
		});
		fullIndexThread.setDaemon(true);
		fullIndexThread.setName("fullIndexThread");
		fullIndexThread.start();
	}

	private void startFullIndexOnSpark() {
		logger.info("start full index");
		final Thread fullIndexThread = new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(2 * 60 * 1000);
						fullIndexScheduler.schedulerFullIndex();
					} catch (Throwable e) {
						logger.error(e.getMessage(), e);
					}
				}
			}
		});
		fullIndexThread.setDaemon(true);
		fullIndexThread.setName("fullIndexOnSparkThread");
		fullIndexThread.start();
	}

	
	private void startIncIndexOnSpark() {
		logger.info("stop old real time job");
		realTimeIndexScheduler.stopAllRealTimeIndex();
		logger.info("start real time index");
		final Thread incIndexThread = new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(2 * 60 * 1000);
						realTimeIndexScheduler.schedulerRealTimeIndex();
					} catch (Throwable e) {
						logger.error(e.getMessage(), e);
					}
				}
			}
		});
		incIndexThread.setDaemon(true);
		incIndexThread.setName("incIndexOnSparkThread");
		incIndexThread.start();
	}

	public boolean isRunFullIndex() {
		return runFullIndex;
	}

	public void setRunFullIndex(boolean runFullIndex) {
		this.runFullIndex = runFullIndex;
	}

	public boolean isRunIncIndex() {
		return runIncIndex;
	}

	public void setRunIncIndex(boolean runIncIndex) {
		this.runIncIndex = runIncIndex;
	}

	public static void main(String[] args){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String end=sdf.format(new Date());
		System.out.println(end);
	}

	@Override
	public void onApplicationEvent(DBManagerInitedEvent event) {
		logger.info("start Index consumer================================================================");
		updateConsumer.startConsumerReload(IndexConstants.CACHE_TOPIC);
		//updateConsumer.startConsumerReload(IndexConstants.CACHE_TOPIC,IndexConstants.OMQ_NAMESPACE);
		searchHistoryConsumer.startConsumerReload(IndexConstants.SEARCH_HISTORY_TOPIC);
		logger.info("start Index consumer sucessfully=====================================================");
		serviceStartupDoIndex();
	}

	//服务启动的时候触发一次
	private void serviceStartupDoIndex() {
		Thread fullIndexThread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					logger.info("start serviceStartupDoIndex");
					Thread.sleep(3*60*1000);
					indexService.fullindex(true, true);
					logger.info("end serviceStartupDoIndex");
				} catch (InterruptedException e) {
					logger.error(e.getMessage(), e);
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}

			}
		});
		fullIndexThread.setDaemon(true);
		fullIndexThread.setName("serviceStartupDoIndex");
		fullIndexThread.start();
	}
	
	private void indexCheck(long sleep_time) throws Exception{
		Date now = new Date();
		boolean isStartWithOplusO = ConfigUtil.getBool("isStartWithOplusO", true);
		logger.info("isStartWithOplusO: "+isStartWithOplusO);

		String indexAlias = IndexConstants.index_alias;
		if(isStartWithOplusO){
			indexAlias = OplusOIndexConstants.index_alias;
		}
		List<String> indexNames=ESService.getIndexNameByAlias(indexAlias);
		String currentIndexName="";
		if(indexNames!=null&&indexNames.size()>0){
			currentIndexName=indexNames.get(0);
			logger.info(" alias now poits to index "+ currentIndexName);
		}
		if(StringUtils.isBlank(currentIndexName)){
			logger.error(" alias does not poit to any index "+ currentIndexName);
			return;
		}
		Matcher m = indexDatePattern.matcher(currentIndexName);
		if(m.find()) {
			currentIndexName = m.group(0);
		} else {
			logger.error("can't parsre index time from " + currentIndexName);
			return;
		}
		Date date = indexDateFormat.parse(currentIndexName);
		long timeSpan = now.getTime() - date.getTime();
		if(timeSpan >= sleep_time){
			logger.error("The alias has not switch for more than " + (double)timeSpan/HOUR +" hours ");
		}else{
			logger.info("index version check ok !!!");
		}
	}

}
