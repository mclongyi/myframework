package com.odianyun.search.whale.index.api.service;

import com.odianyun.search.whale.index.api.common.IndexException;
import com.odianyun.soa.client.SoaClientFactoryBean;
import com.odianyun.soa.common.dto.ClientProfile;

/**
 *
 * IndexClient提供获取Search SOA接口的代理
 *
 * @author
 *
 */
public class IndexClient {

	static String _domainName="search";
	static String _serviceName="SOARealTimeIndexService";
	private static String _historyServiceName = "SOAHistoryLogService";
	static String _serviceAppName="index";
	static String _serviceVersion="0.1";
	static Long _timeout=10000l;
	static Long _readTimeout=5000l;
	static String soa_config_file_name="index_soa.properties";
    static RealTimeIndexService _realTimeIndexService;
    static HistoryLogService _histoyLogService;
	/**
	static{
		ConfigUtil.loadPropertiesFile(soa_config_file_name);
		String domainName=ConfigUtil.get("soa.domainName");
		if(StringUtils.isNotEmpty(domainName)){
			_domainName=domainName;
		}
		String serviceName=ConfigUtil.get("soa.serviceName");
		if(StringUtils.isNotEmpty(serviceName)){
			_serviceName=serviceName;
		}
		String historyServiceName=ConfigUtil.get("soa.historyServiceName");
		if(StringUtils.isNotEmpty(historyServiceName)){
			_historyServiceName=historyServiceName;
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
	 */

	/**
	 * @param clientName  调用方的应用名
	 * @return 远程服务的代理对象
	 */
	@Deprecated
	public static RealTimeIndexService getRealTimeIndexService(String clientName){
		return getRealTimeIndexService(clientName, _serviceVersion);
	}

	/**
	 *
	 * @param clientName  调用方的应用名
	 * @param serviceVersion  服务的版本号
	 * @return 远程服务的代理对象
	 */
	@Deprecated
	public static RealTimeIndexService getRealTimeIndexService(String clientName,String serviceVersion){
        if(_realTimeIndexService==null){
			synchronized (IndexClient.class){
				if(_realTimeIndexService==null){
					try{
						_realTimeIndexService=new RealTimeIndexProxyService();

//						ClientProfile clientProfile=new ClientProfile();
//						clientProfile.setDomainName(_domainName);
//						clientProfile.setServiceName(_serviceName);
//						clientProfile.setServiceVersion(serviceVersion);
//						clientProfile.setServiceAppName(_serviceAppName);
//						clientProfile.setClientAppName(clientName);
//						clientProfile.setTimeout(_timeout);
//						clientProfile.setReadTimeout(_readTimeout);
//						SoaClientFactoryBean soaClientFactoryBean=new SoaClientFactoryBean(RealTimeIndexService.class
//								,clientProfile);
//						_realTimeIndexService=new RealTimeIndexProxyService((RealTimeIndexService) soaClientFactoryBean.getObject());
					}catch(Exception e){
						throw new IndexException("get RealTimeIndexService proxy failed",e);
					}
				}
			}
		}
		return _realTimeIndexService;
	}

	/**
	 * @param clientName
	 *            调用方的应用名
	 * @return 远程服务的代理对象
	 */
	public static HistoryLogService getHistoryLogService(String clientName) {
		return getHistoryLogService(clientName, _serviceVersion);
	}

	/**
	 *
	 * @param clientName
	 *            调用方的应用名
	 * @param serviceVersion
	 *            服务的版本号
	 * @return 远程服务的代理对象
	 */
	public static HistoryLogService getHistoryLogService(String clientName, String serviceVersion) {
		if(_histoyLogService==null){
			synchronized (HistoryLogService.class){
				if(_histoyLogService==null){
					try {
						ClientProfile clientProfile = new ClientProfile();
						clientProfile.setDomainName(_domainName);
						clientProfile.setServiceName(_historyServiceName);
						clientProfile.setServiceVersion(serviceVersion);
						clientProfile.setServiceAppName(_serviceAppName);
						clientProfile.setClientAppName(clientName);
						clientProfile.setTimeout(_timeout);
						clientProfile.setReadTimeout(_readTimeout);
						SoaClientFactoryBean soaClientFactoryBean = new SoaClientFactoryBean(HistoryLogService.class,
								clientProfile);
						_histoyLogService = (HistoryLogService) soaClientFactoryBean.getObject();
					} catch (Exception e) {
						throw new IndexException("get RealTimeIndexService proxy failed", e);
					}
				}
			}
		}
		return _histoyLogService;
	}

}
