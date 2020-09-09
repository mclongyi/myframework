package com.odianyun.search.whale.index.schedule;

import java.util.*;
import java.util.Calendar;

import org.apache.log4j.Logger;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import com.odianyun.search.whale.data.saas.model.DumpConfig;

public class QuartzHelper {

	private static Logger log = Logger.getLogger(QuartzHelper.class);

	private static final String defaultCronExpression = "0 1 * * * ?";

	//jobType(clock, period), cronExpression/periodMinus
	public Scheduler scheduleJob(String jobName, Class<? extends Job> jobClass, DumpConfig dumpConfig,
			Map<String, Object> jobDataMap) {
		Scheduler scheduler = null;
		try {
			scheduler = StdSchedulerFactory.getDefaultScheduler();
			JobDetail jobDetail = JobBuilder.newJob(jobClass)
					.withIdentity(jobName, jobName + "Grop").build();
			jobDetail.getJobDataMap().putAll(jobDataMap);

			Trigger trigger = null;
			int jobType = dumpConfig.getJobType();
			if (jobType == 1) {// 定时任务
				trigger = genClockTrigger(jobName, dumpConfig.getCronExpression());
			} else if (jobType == 0) {// 周期任务
				trigger = genPeriodTrigger(jobName, dumpConfig.getPeriod());
			}

			if (trigger == null) {
				log.error("trigger is null, jobName is " + jobName);
				return null;
			}
			scheduler.scheduleJob(jobDetail, trigger);
			scheduler.start();
		} catch (Exception e) {
			log.error("schedule " + jobName + " error", e);
		}
		log.info("schedule " + jobName + " over");
		return scheduler;
	}

	private static Trigger genClockTrigger(String jobName, String cronExpression) {
		String triggerName = jobName + "Trigger";
		String triggerGroup = jobName + "TriggerGroup";

		CronScheduleBuilder cronScheduleBuilder = null;
		try {
			cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
		} catch (Exception e) {
			log.error("cronExpression is error, cronExpression=" + cronExpression);
			cronScheduleBuilder = CronScheduleBuilder.cronSchedule(defaultCronExpression);
		}

		Trigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerName, triggerGroup)
				.startNow().withSchedule(cronScheduleBuilder).build();

		log.info("trigger for : " + jobName + " [cronExpression=" + cronExpression + "]");
		return trigger;
	}

	private static Trigger genPeriodTrigger(String jobName, int periodMinus) {
		String triggerName = jobName + "Trigger";
		String triggerGroup = jobName + "TriggerGroup";

		Calendar curr = Calendar.getInstance();
		curr.set(Calendar.MINUTE, curr.get(Calendar.MINUTE) + periodMinus);
		Date startTime = curr.getTime();

		Trigger trigger = TriggerBuilder
				.newTrigger()
				.withIdentity(triggerName, triggerGroup)
				.startAt(startTime)
				.withSchedule(
						SimpleScheduleBuilder.simpleSchedule().withIntervalInMinutes(periodMinus) 
								.repeatForever()).build();
		log.info("trigger for : " + jobName + "[periodMinus=" + periodMinus + "]");
		return trigger;
	}

    public void unScheduleJob(Scheduler scheduler, String jobName) {
        try {
            scheduler.deleteJob(new JobKey(jobName, jobName + "Grop"));
        } catch (SchedulerException e) {
            log.error("unschedule " + jobName + " error", e);
        }
    }
}
