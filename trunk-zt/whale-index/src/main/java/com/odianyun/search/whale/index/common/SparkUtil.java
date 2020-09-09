package com.odianyun.search.whale.index.common;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

import com.odianyun.search.whale.common.util.GsonUtil;
import com.odianyun.search.whale.index.api.common.IndexConstants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.hadoop.yarn.api.records.ApplicationId;
import org.apache.hadoop.yarn.api.records.ApplicationReport;
import org.apache.hadoop.yarn.api.records.YarnApplicationState;
import org.apache.hadoop.yarn.client.api.YarnClient;
import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.deploy.yarn.Client;
import org.apache.spark.deploy.yarn.ClientArguments;

public class SparkUtil {
	static Logger logger = Logger.getLogger(SparkUtil.class);
    private static YarnClient yarnClient;


    public static ApplicationId submitJob(String jobName, String jobClass,
                                          SparkJobConf jobConf, boolean waitForComplete) throws IOException {
        return submitJob(jobName, jobClass, jobConf, waitForComplete, -1);
    }

	public static ApplicationId submitJob(String jobName, String jobClass,
                                          SparkJobConf jobConf, boolean waitForComplete, int execCores) throws IOException {
		String cmd = "tar -zcf /data/env.tgz /data/env/";
        Process process = Runtime.getRuntime().exec(cmd);
        try {
            process.waitFor();
        } catch (Exception e) {
            logger.error("tar config failed,", e);
        }

        String[] arg0 = new String[] {
                "--name", jobName,
                "--class", jobClass,
                "--arg", GsonUtil.getGson().toJson(jobConf),
                "--addJars", getJarPaths(new String[]{}),
                "--archives", "/data/env.tgz#index"
		};

        if(execCores > 0) {
            arg0 = new String[]{
                    "--name", jobName,
                    "--class", jobClass,
                    "--executor-cores", String.valueOf(execCores),
                    "--arg", GsonUtil.getGson().toJson(jobConf),
                    "--addJars", getJarPaths(new String[]{}),
                    "--archives", "/data/env.tgz#index"
            };
        }

		System.setProperty("SPARK_YARN_MODE", "true");
		System.setProperty("HADOOP_USER_NAME", "search");

		SparkConf sparkConf = new SparkConf();
		sparkConf.setMaster("yarn-cluster");
		sparkConf.set("spark.yarn.jar", "hdfs:///user/search/spark-assembly-1.6.0-hadoop2.6.0.jar");

		ClientArguments cArgs = new ClientArguments(arg0, sparkConf);

        Client client = new Client(cArgs, SparkConfigBuilder.build(), sparkConf);

        if(waitForComplete) {
            client.run();
            return null;
        } else {
            return client.submitApplication();
        }
	}

    public static void killJob(ApplicationId applicationId) {
        try {
            getYarnClient().killApplication(applicationId);
        } catch (Exception e) {
            logger.error("kill job failed, application:" + applicationId.toString(), e);
        }
    }


    public static List<ApplicationId> getRunningJobIds() {
        List<ApplicationReport> runningApplications = getRunningApplications();
        List<ApplicationId> runningJobIds = new ArrayList<ApplicationId>();
        for (ApplicationReport application : runningApplications) {
            runningJobIds.add(application.getApplicationId());
        }

        return  runningJobIds;
    }

    public static List<ApplicationReport> getRunningApplications() {
        List<ApplicationReport> runningApplications = new ArrayList<ApplicationReport>();
        try {
            List<ApplicationReport> allApplications = getYarnClient().getApplications();
            if (CollectionUtils.isNotEmpty(allApplications)) {
                for (ApplicationReport application : allApplications) {
                    if (application.getYarnApplicationState() == YarnApplicationState.RUNNING)
                        runningApplications.add(application);
                }
            }
        } catch (Exception e) {
            logger.error("get running applications failed", e);
        }

        return runningApplications;
    }

    private static YarnClient getYarnClient() {
        if(yarnClient != null)
            return yarnClient;

        synchronized (SparkUtil.class) {
            YarnConfiguration yarnConf = new YarnConfiguration(SparkConfigBuilder.build());
            yarnClient = YarnClient.createYarnClient();
            yarnClient.init(yarnConf);
            yarnClient.start();
        }

        return yarnClient;
    }

	private static String getJarPaths(String[] excludeJars) {
		Set<String> jars = new HashSet<String>();
		String classPath = System.getProperty("java.class.path");
		if (classPath != null) {
			String[] jarPaths = classPath.split(File.pathSeparator);
			for (String jarPath : jarPaths) {
				if (jarPath.endsWith(".jar")) {
					jars.add(jarPath);
				}
			}
		}

		String classFilePath = SparkUtil.class.getProtectionDomain().getCodeSource()
				.getLocation().getFile();
        logger.info("classFilePath:" + classFilePath);
        if(!classFilePath.endsWith("WEB-INF/classes")) {
            classFilePath = classFilePath.split("WEB-INF/classes")[0] + "WEB-INF/classes";
            logger.info("new classFilePath:" + classFilePath);
        }
		// URL Decoding
		try {
			classFilePath = java.net.URLDecoder.decode(classFilePath, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		File jarFile = new File(new File(classFilePath).getParentFile(), "lib");
		File[] jarFiles = jarFile.listFiles();
		if (jarFiles != null) {
			for (File jar : jarFiles) {
				if (jar.getName().endsWith(".jar")) {
					jars.add(jar.getAbsolutePath());
				}
			}
		}


        Iterator<String> it = jars.iterator();
        while (it.hasNext()) {
            String jarPath = it.next();
            String jarName = jarPath.replaceAll("^.*?lib/", "");
            for (String excludeJar : excludeJars) {
                if (jarName.contains(excludeJar)) {
                    it.remove();
                }
            }
        }

        String jarString = "";
        for (String jarPath : jars) {
            jarString += jarPath;
            jarString += ",";
        }
        
        return jarString;
	}
	
	public static void main(String[] args) throws IOException {

        SparkJobConf jobConf = new SparkJobConf( -1, "index", IndexConstants.index_type);
        jobConf.setIndexVersion("2016053001");
        System.out.println(GsonUtil.getGson().toJson(jobConf));
        submitJob("test",  "com.odianyun.search.whale.index.scala.full.FullIndexProcessor", jobConf, true);
	}
}
