package com.odianyun.search.whale.index.schedule;

import com.odianyun.search.whale.data.saas.model.CommonConfig;
import com.odianyun.search.whale.data.saas.model.DumpConfig;
import com.odianyun.search.whale.data.saas.service.SaasConfigService;
import com.odianyun.search.whale.index.server.IndexService;

import org.apache.log4j.Logger;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by cuikai on 16/7/1.
 */
public class FullIndexScheduler {

    static Logger logger = Logger.getLogger(FullIndexScheduler.class);

    // @Autowired
    SaasConfigService saasConfigService;

    @Autowired
    IndexService indexService;

    @Autowired
    QuartzHelper quartzHelper;

    private Map<String, Scheduler> fullIndexSchedulers = new HashMap<String, Scheduler>();

    /*
     * 1. 获取所有当前处于有效状态的index
     * 2. 停止被禁用的index的全量调用
     * 3. 对当前有效的每个index判断是否已经存在调度任务,如果不存在则新增对应的调度job
     * 4. 多个job之间的协调问题:暂时通过IndexService中的isIndexing变量来控制，同一时间只容许构建一个全量索引
     *    后续如果所有job都提交到spark上，可以将控制粒度下降到索引级别，也就是同一时间同一份索引只容许提交一次
     */
    public void schedulerFullIndex() {
        try {
            Set<String> indexNames = saasConfigService.getAllIndexNames();
            if (indexNames == null) {
                logger.error("can't get dump indexNames from db!");
                return;
            }

            //停止已经禁用的index的全量调度
            Set<String> scheduledIndexs = fullIndexSchedulers.keySet();
            for (String scheduledIndex : scheduledIndexs) {
                if(!indexNames.contains(scheduledIndex)) {
                    quartzHelper.unScheduleJob(fullIndexSchedulers.get(scheduledIndex), scheduledIndex);
                    logger.info("index " + scheduledIndex + " fullIndex scheduler stop success!");

                    fullIndexSchedulers.remove(scheduledIndex);
                }
            }


            for (String indexName : indexNames) {
                try {
                    Scheduler fullIndexScheduler = fullIndexSchedulers.get(indexName);
                    if (fullIndexScheduler != null && fullIndexScheduler.isStarted()) {
                        continue;
                    }

                    CommonConfig commonConfig = saasConfigService.getCommonConfig(indexName);
                    Map<String, Object> jobDataMap = new HashMap<String, Object>();
                    jobDataMap.put("companyId", commonConfig.getCompanyId());
                    jobDataMap.put("indexService", indexService);

                    DumpConfig dumpConfig = saasConfigService.getDumpConfig(indexName);
                    if (dumpConfig == null) {
                        logger.error("can't fetch dump config of index " + indexName);
                        continue;
                    }

                    fullIndexScheduler = quartzHelper.scheduleJob(indexName,
                            FullIndexJob.class, dumpConfig, jobDataMap);
                    fullIndexSchedulers.put(indexName, fullIndexScheduler);
                    logger.info(indexName + " fullIndex scheduler start success!");
                } catch (SchedulerException e) {
                    logger.error("Start fullIndex schedule error! indexIO:" + indexName, e);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}
