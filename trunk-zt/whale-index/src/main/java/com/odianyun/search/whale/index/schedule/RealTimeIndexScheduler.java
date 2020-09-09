package com.odianyun.search.whale.index.schedule;

import com.odianyun.search.whale.common.util.ConfigUtil;
import com.odianyun.search.whale.data.saas.service.SaasConfigService;
import com.odianyun.search.whale.index.common.SparkJobConf;
import com.odianyun.search.whale.index.common.SparkUtil;
import com.odianyun.search.whale.index.scala.realtime.RealTimeProcessor;
import com.odianyun.search.whale.index.server.IndexService;

import org.apache.hadoop.yarn.api.records.ApplicationId;
import org.apache.hadoop.yarn.api.records.ApplicationReport;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * Created by cuikai on 16/7/1.
 */
public class RealTimeIndexScheduler {

	static Logger logger = Logger.getLogger(RealTimeIndexScheduler.class);

	//@Autowired
	SaasConfigService saasConfigService;

	@Autowired
	IndexService indexService;

	private ApplicationId realTimeIndexJobId;

	public void schedulerRealTimeIndex() {
		try {
			// 判断job是否仍然alive
			List<ApplicationId> runningJobIds = SparkUtil.getRunningJobIds();
			if (realTimeIndexJobId != null && runningJobIds.contains(realTimeIndexJobId))
				return;
			
			SparkJobConf jobConf = new SparkJobConf();
			String jobName = "RealTimeIndex-" + ConfigUtil.get("mail.env");
			realTimeIndexJobId = SparkUtil.submitJob(jobName, RealTimeProcessor.class.getName(),
					jobConf, false, 1);
			logger.info("real time index start success, jobId:" + realTimeIndexJobId.toString());
		} catch (Exception e) {
			logger.error("Start real time index error!", e);
		}
	}

	public void stopAllRealTimeIndex() {
		List<ApplicationReport> runningApplications = SparkUtil.getRunningApplications();
		for (ApplicationReport application : runningApplications) {
			if (application.getName().startsWith("RealTimeIndex")
					&& application.getName().contains(ConfigUtil.get("mail.env"))) {
				SparkUtil.killJob(application.getApplicationId());
				logger.info("kill job when initial, job name:" + application.getName()
						+ ", job id:" + application.getApplicationId().toString());
			}
		}
	}
}
