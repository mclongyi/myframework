package com.odianyun.search.whale.index.schedule;

import java.util.Map;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.odianyun.search.whale.index.server.IndexService;


public class FullIndexJob implements Job{

	private static Logger log = Logger.getLogger(FullIndexJob.class);
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		Map<String, Object> dataMap = context.getJobDetail().getJobDataMap();
		IndexService indexService = (IndexService)dataMap.get("indexService");
		Integer companyId = (Integer) dataMap.get("companyId");

		try {
			indexService.fullindexOnSpark(true, true);
		} catch (Exception e) {
			log.error("excute fullIndex failed, companyId:" + companyId, e);
		}
	}

}
