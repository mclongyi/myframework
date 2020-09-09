package com.odianyun.search.whale.store.hbasestore;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.log4j.Logger;

import com.odianyun.search.whale.store.conf.ConfigurationUtil;


public class HBaseUtil {

	private static Logger log = Logger.getLogger(HBaseUtil.class);

	public static Configuration createConf(String quorum, int port) {
		return ConfigurationUtil.createHBaseConf(quorum, port);
	}

	/*public static HTablePool createHTablePool(Configuration conf) {
		HTablePool tablePool = new HTablePool(conf, Integer.MAX_VALUE);

		return tablePool;
	}*/
	
	public static Connection createConnection(Configuration conf) {
		Connection connection = null;
		try {
			connection = ConnectionFactory.createConnection(conf);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
		return connection;
	}

}
