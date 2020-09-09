package com.odianyun.search.whale.history.redis;

import java.util.List;

import com.odianyun.search.whale.api.model.req.AbstractHistoryRequest;
import com.odianyun.search.whale.index.api.model.req.HistoryType;
import org.apache.log4j.Logger;

import com.odianyun.search.whale.api.model.req.AbstractSearchRequest;
import com.odianyun.search.whale.api.model.req.HistoryCleanRequest;
import com.odianyun.search.whale.api.model.req.HistoryReadRequest;
import com.odianyun.search.whale.common.util.ConfigUtil;
import com.odianyun.search.whale.common.util.RedisUtil;
import com.odianyun.search.whale.index.api.model.req.HistoryResult;
import com.odianyun.search.whale.index.api.model.req.HistoryWriteRequest;
import com.odianyun.search.whale.index.api.model.req.UserHistory;

import redis.clients.jedis.JedisPoolConfig;

public class UserHistoryResdis {

	static Logger logger = Logger.getLogger(UserHistoryResdis.class);

	static String history_redis_config_file_name = "history_redis.properties";

	private static RedisUtil redisUtil;

	public UserHistoryResdis() {
		try{
			ConfigUtil.loadPropertiesFile(history_redis_config_file_name);
		}catch(Exception e){
			logger.error(e.getMessage(),e);

		}
		logger.info("start init redis ----------------------------------------------");
		
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setMaxTotal(ConfigUtil.getInt("history_redis_max", 100));
		jedisPoolConfig.setMaxIdle(ConfigUtil.getInt("history_redis_idle", 30));
		jedisPoolConfig.setMaxWaitMillis(ConfigUtil.getInt("history_redis_maxWait", 200));
		jedisPoolConfig.setTestOnBorrow(true);
		String ip=ConfigUtil.get("history_redis_ip", "localhost");
		int port=ConfigUtil.getInt("history_redis_port", 6379);
		int connection_timeout=ConfigUtil.getInt("history_redis_connection_timeout", 2000);
		int so_timeout=ConfigUtil.getInt("history_redis_socket_timeout", 10000);
		logger.info("history_redis_ip :" + ip);
		//因为合并代码，注释下面两行
		//String password = ConfigUtil.get("history_redis_password", "");
		//redisUtil = new RedisUtil(jedisPoolConfig, ip, port, connection_timeout,so_timeout,password);
		redisUtil = new RedisUtil(jedisPoolConfig, ip, port, connection_timeout,so_timeout);
		logger.info("end init redis ---------------------------------------------------");

	}

	public List<HistoryResult> getUserHistory(HistoryReadRequest request) {
		UserHistory userHistory = redisUtil.get(getUserHistoryKey(request), UserHistory.class);
		if(userHistory == null){
			return null;
		}
		List<HistoryResult> historyResult = userHistory.getHistoryResult();
		return historyResult;
	}
	
	public void setUserHisttory(HistoryWriteRequest request, List<HistoryResult> historyResult){
		UserHistory userHistory = new UserHistory();
		userHistory.setUserId(request.getUserId());
		userHistory.setHistoryResult(historyResult);
		redisUtil.putExpireSeconds(request.getType().getCode() + "_" + request.getCompanyId() + "_" + request.getUserId() + "_" + request.getMerchantId(), userHistory,RedisUtil.secondsSevenDay);
	}

	public void clean(HistoryCleanRequest request) {
		redisUtil.clean(getUserHistoryKey(request));
	}
	
	private String getUserHistoryKey(AbstractHistoryRequest request){
		return new StringBuilder().append(request.getType().getCode()).append("_")
				.append(request.getCompanyId()).append("_")
				.append(request.getUserId()).append("_")
				.append(request.getMerchantId()).toString();
	}
}
