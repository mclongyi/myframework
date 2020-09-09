package com.odianyun.search.whale.common.util;

import com.google.gson.Gson;

import org.apache.commons.lang.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

public class RedisUtil {

	public static  final  int secondsSevenDay = 24*60*60*7;
	
	Gson gson=new Gson();
	
	private JedisPool pool;
	
	public RedisUtil(JedisPoolConfig config,String host,int port,int connectionTimeout, int soTimeout){
//		pool = new JedisPool(config,host,port,timeout);
//		pool = new JedisPool(config, host, port, connectionTimeout, soTimeout, null, Protocol.DEFAULT_DATABASE, null);
		this(config,host,port,connectionTimeout,soTimeout,null);
	}
	public RedisUtil(JedisPoolConfig config,String host,int port,int connectionTimeout, int soTimeout,String password){
//		pool = new JedisPool(config,host,port,timeout);
		if(StringUtils.isBlank(password)){
			password = null;
		}
		pool = new JedisPool(config, host, port, connectionTimeout, soTimeout, password, Protocol.DEFAULT_DATABASE, null);
	}
	
//	public void put(String k,Object v,long timeout){
//		Jedis jedis=pool.getResource();
//		try{
//			jedis.set(k, gson.toJson(v), "NX", "PX", timeout);
//		}finally{
//			if(jedis!=null){
//				jedis.close();
//			}
//		}
//	}


	public Boolean putIfNotExist(String k,Object v,long timeout){
		Jedis jedis=pool.getResource();
		try{
		    String code=jedis.set(k, gson.toJson(v), "NX", "PX", timeout);
            if(StringUtils.isNotBlank(code)){
				return true;
			}
			return false;
		}finally{
			if(jedis!=null){
				jedis.close();
			}
		}
	}
	
	public void putExpireSeconds(String k,Object v,int seconds){
		Jedis jedis=pool.getResource();
		try{
			jedis.set(k, gson.toJson(v));
			jedis.expire(k,seconds);
		}finally{
			if(jedis!=null){
				jedis.close();
			}
		}
	}
	
	public void clean(String k){
		Jedis jedis=pool.getResource();
		try{
			jedis.del(k);
		}finally{
			if(jedis!=null){
				jedis.close();
			}
		}
	}
	
	public <T> T get(String k,Class<T> clazz){
		Jedis jedis=pool.getResource();
		T t=null;
		try{
			String value=jedis.get(k);
			if(value!=null){
				t= gson.fromJson(value, clazz);
			}
		    return t;
		}finally{
			if(jedis!=null){
				jedis.close();
			}
		}
	}

}
