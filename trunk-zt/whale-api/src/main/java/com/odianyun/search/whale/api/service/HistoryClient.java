package com.odianyun.search.whale.api.service;

import org.apache.commons.lang.StringUtils;

import com.odianyun.search.whale.api.common.SearchException;
import com.odianyun.search.whale.common.util.ConfigUtil;
import com.odianyun.soa.client.SoaClientFactoryBean;
import com.odianyun.soa.common.dto.ClientProfile;

/**
 * 
 * HistoryClient提供获取Search SOA接口的代理
 * 
 * @author 
 *
 */
public class HistoryClient {
	
	static String _domainName="search";
	static String _serviceName="SOAHistoryService";
	static String _serviceAppName="search";
	static String _serviceVersion="0.1";
	static Long _timeout=6000l;
	static Long _readTimeout=3000l;
	static String soa_config_file_name="search_soa.properties";
	
	static HistoryService _historyService;
	
	static{
		ConfigUtil.loadPropertiesFile(soa_config_file_name);
		String domainName=ConfigUtil.get("soa.domainName");
		if(StringUtils.isNotEmpty(domainName)){
			_domainName=domainName;
		}
		String serviceName=ConfigUtil.get("soa.hostoryServiceName");
		if(StringUtils.isNotEmpty(serviceName)){
			_serviceName=serviceName;
		}
		String serviceAppName=ConfigUtil.get("soa.serviceAppName");
		if(StringUtils.isNotEmpty(serviceAppName)){
			_serviceAppName=serviceAppName;
		}
		String serviceVersion=ConfigUtil.get("soa.serviceVersion");
		if(StringUtils.isNotEmpty(serviceVersion)){
			_serviceVersion=serviceVersion;
		}
	}
	
	/**
	 * @param clientName  调用方的应用名
	 * @return 远程服务的代理对象
	 */
	public static HistoryService getHistoryService(String clientName){
		return getHistoryService(clientName, _serviceVersion);
	}
	
	/**
	 * 
	 * @param clientName  调用方的应用名
	 * @param serviceVersion  服务的版本号
	 * @return 远程服务的代理对象
	 */
	public static HistoryService getHistoryService(String clientName,String serviceVersion){
		if(_historyService==null){
		     synchronized (HistoryClient.class) {
				if(_historyService==null){
					try{
						ClientProfile clientProfile=new ClientProfile();
						clientProfile.setDomainName(_domainName);
						clientProfile.setServiceName(_serviceName);
						clientProfile.setServiceVersion(serviceVersion);
						clientProfile.setServiceAppName(_serviceAppName);
						clientProfile.setClientAppName(clientName);
						clientProfile.setTimeout(_timeout);
						clientProfile.setReadTimeout(_readTimeout);
						clientProfile.setClientThrottle(false);
						SoaClientFactoryBean soaClientFactoryBean=new SoaClientFactoryBean(HistoryService.class
								,clientProfile);
						_historyService=new HistoryCacheService((HistoryService) soaClientFactoryBean.getObject());			
					}catch(Exception e){
						throw new SearchException("get SearchService proxy failed",e);
					}	
				}
			}
		}
		return _historyService;
	}
	
	
}
