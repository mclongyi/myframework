package com.odianyun.search.whale.index.common;

import com.odianyun.search.whale.common.util.ConfigUtil;
import org.apache.hadoop.conf.Configuration;
import org.apache.log4j.Logger;

/**
 * Created by cuikai on 16/7/14.
 */
public class SparkConfigBuilder {
    private static Logger logger = Logger.getLogger(SparkConfigBuilder.class);

    public static Configuration build() {
        try {
            ConfigUtil.loadPropertiesFile("spark.properties");
        } catch (Exception e) {
            logger.warn("load properties failed", e);
        }


        Configuration conf = new Configuration();
        conf.set("mapreduce.framework.name", "yarn"); // 指定使用yarn框架
        conf.set("fs.defaultFS",
                ConfigUtil.get("fs.defaultFS", "hdfs://172.16.2.134:8020"));// 指定namenode
        conf.set("yarn.resourcemanager.address",
                ConfigUtil.get("yarn.resourcemanager.address", "172.16.2.134:8032")); // 指定resourcemanager
        conf.set("yarn.resourcemanager.scheduler.address",
                ConfigUtil.get("yarn.resourcemanager.scheduler.address", "172.16.2.134:8030"));// 指定资源分配器
        conf.set("mapreduce.jobhistory.address",
                ConfigUtil.get("mapreduce.jobhistory.address", "172.16.2.134:10020"));

        return conf;
    }
}
