package com.odianyun.search.whale.index.full;

import com.odianyun.search.whale.common.util.ConfigUtil;
import com.odianyun.search.whale.data.saas.model.CommonConfig;
import com.odianyun.search.whale.data.saas.model.ESClusterConfig;
import com.odianyun.search.whale.data.saas.service.SaasConfigService;
import com.odianyun.search.whale.data.service.ConfigService;
import com.odianyun.search.whale.es.api.ESClient;
import com.odianyun.search.whale.es.api.ESService;
import com.odianyun.search.whale.index.api.common.IndexConstants;
import com.odianyun.search.whale.index.common.SparkJobConf;
import com.odianyun.search.whale.index.common.SparkUtil;
import com.odianyun.search.whale.index.scala.full.FullIndexProcessor;
import com.odianyun.search.whale.processor.IndexFlow;
import org.apache.log4j.Logger;
import org.elasticsearch.client.Client;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MerchantProductSparkIndexFlowImpl implements IndexFlow{
	
	SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
	static Logger logger = Logger.getLogger(MerchantProductSparkIndexFlowImpl.class);

	String index_start_time;
	
	static int INDEX_NUM = 5;
	
	@Autowired
	MerchantProductIndexSwitcher esIndexSwitcher;

    @Autowired
    ConfigService configService;

    // @Autowired
    SaasConfigService saasConfigService;

//    private List<String> b2CAliasSwitchIndices = new ArrayList<String>();
    
    private CommonConfig commonConfig = null;
    
    @Override
    public void init() throws Exception {
        try {
            ConfigUtil.loadPropertiesFile("mail.properties");
            ConfigUtil.loadPropertiesFile("spark.properties");
        } catch (Exception e) {
            logger.warn("load properties failed", e);
        }

		index_start_time=simpleDateFormat.format(new Date());
		int companyId=10;
        commonConfig = saasConfigService.getCommonConfigByCompany(companyId);
        String esAdminUrl = saasConfigService.getESClusterConfig(commonConfig.getEsClusterId()).getAdminUrl();
		esIndexSwitcher.createIndex(esAdminUrl, index_start_time, commonConfig.getIndexName() + "_",
                "/es/"+IndexConstants.index_mapping_name);
	}

    @Override
	public boolean process() throws Exception {
        SparkJobConf jobConf = new SparkJobConf(commonConfig.getCompanyId(),
                commonConfig.getIndexName(), commonConfig.getIndexType());
        jobConf.setIndexVersion(index_start_time);

        String jobName = "FullIndex-" + jobConf.getIndexName() + "-" + jobConf.getIndexVersion()
                + "-" + ConfigUtil.get("mail.env");
        SparkUtil.submitJob(jobName,
                FullIndexProcessor.class.getName(), jobConf, true);
		return true;
	}

    @Override
	public void done(boolean needValidation) throws Exception {
		String indexName = commonConfig.getIndexName();
        String indexType = commonConfig.getIndexType();

        ESClusterConfig clusterConfig = saasConfigService.getESClusterConfig(commonConfig.getEsClusterId());

        if(needValidation){
            Client client = ESClient.getClient(clusterConfig.getClusterName(), clusterConfig.getClusterNode());
			if(!esIndexSwitcher.validate(client, indexName, indexType, index_start_time)){
				ESService.deleteIndex(clusterConfig.getAdminUrl(), indexName + "_" + index_start_time);
				return;
			}
		}

		try {
			//TODO 这里companyIds 暂时设置为null  修改spark索引流程的时候需要重新修改
			esIndexSwitcher.switchIndex(clusterConfig.getAdminUrl(), index_start_time,indexName + "_",
							"/es/"+IndexConstants.index_mapping_name,indexName,
							needValidation, null,INDEX_NUM);

            //TODO:暂时生成两个别名
            /*if(isSwitchB2cAlias(indexName)) {
                ESService.updateAlias(clusterConfig.getAdminUrl(),
                        indexName + "_" + index_start_time, indexName+"_alias");
                logger.info(IndexConstants.index_alias + " alias point to " +
                        ESService.getIndexNameByAlias(clusterConfig.getAdminUrl(), indexName+"_alias"));
            }*/
		} catch (Exception e) {
			logger.error("switch index error, ", e);
		}
	}

	@Override
	public void afterDone() {

	}


   /* public boolean isSwitchB2cAlias(String indexName) {
        boolean isSwitchB2cAlias = false;
        if(b2CAliasSwitchIndices.contains(indexName))
            isSwitchB2cAlias = true;

        return isSwitchB2cAlias;
    }*/
}
