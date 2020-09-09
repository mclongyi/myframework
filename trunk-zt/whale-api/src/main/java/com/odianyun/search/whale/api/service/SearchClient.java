package com.odianyun.search.whale.api.service;

import com.odianyun.search.whale.api.common.SearchException;
import com.odianyun.search.whale.api.model.geo.GeoSearchService;
import com.odianyun.search.whale.api.model.o2o.O2OShopSearchService;
import com.odianyun.search.whale.api.model.selectionproduct.SelectionProductSearchService;
import com.odianyun.search.whale.common.util.ConfigUtil;
import com.odianyun.soa.client.SoaClientFactoryBean;
import com.odianyun.soa.common.dto.ClientProfile;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * 
 * SearchClient提供获取Search SOA接口的代理
 * 
 * @author zengfenghua
 *
 */
public class SearchClient {
	private static final Logger LOGGER = Logger.getLogger(SearchClient.class);

	static String _domainName="search";
	static String _serviceName="SOASearchService";
	static String _cacheServiceName="SOASearchCacheService";
	static String _geoServiceName="SOAGeoSearchService";
	static String _o2oShopSearchServiceName="SOAO2oShopSearchService";
	static String _selectionProductSearchServiceName="SOASelectionProductSearchService";
	static String _shopServiceName = "SOAShopService";
	static String _searchBusinessServiceName="SOASearchBusinessService";
	static String _serviceAppName="search";
	static String _serviceVersion="0.1";
	static Long _timeout=6000l;
	static Long _readTimeout=3000l;
	static String soa_config_file_name="search_soa.properties";
	static SearchService _searchService;
	static CacheService _cacheService;
	static GeoSearchService _geoSearchService;
	static O2OShopSearchService _o2oShopSearchService;
	static SelectionProductSearchService _selectionProductSearchService;
	static ShopService _shopService;
//	private static HistoryLogService historyLogService = null;
	private static SearchBusinessService _searchBusinessService;

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
		String cacheServiceName=ConfigUtil.get("soa.cacheServiceName");
		if(StringUtils.isNotEmpty(cacheServiceName)){
			_cacheServiceName=cacheServiceName;
		}
		String geoServiceName=ConfigUtil.get("soa.geoServiceName");
		if(StringUtils.isNotEmpty(geoServiceName)){
			_geoServiceName=geoServiceName;
		}
		String o2oShopSearchServiceName=ConfigUtil.get("soa.o2oShopSearchServiceName");
		if(StringUtils.isNotEmpty(o2oShopSearchServiceName)){
			_o2oShopSearchServiceName=o2oShopSearchServiceName;
		}
		String selectionProductSearchServiceName=ConfigUtil.get("soa.selectionProductSearchServiceName");
		if(StringUtils.isNotEmpty(selectionProductSearchServiceName)){
			_selectionProductSearchServiceName=selectionProductSearchServiceName;
		}
		String shopServiceName=ConfigUtil.get("soa.shopServiceName");
		if(StringUtils.isNotEmpty(shopServiceName)){
			_shopServiceName=shopServiceName;
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
	public static SearchService getSearchService(String clientName){
		return getSearchService(clientName, _serviceVersion);
	}
	
	/**
	 * 
	 * @param clientName  调用方的应用名
	 * @param serviceVersion  服务的版本号
	 * @return 远程服务的代理对象
	 */
	public static SearchService getSearchService(String clientName,String serviceVersion){
		if(_searchService==null){
		     synchronized (SearchClient.class) {
				if(_searchService==null){
					try{
						ClientProfile clientProfile=new ClientProfile();
						clientProfile.setDomainName(_domainName);
						clientProfile.setServiceName(_serviceName);
						clientProfile.setServiceVersion(serviceVersion);
						clientProfile.setServiceAppName(_serviceAppName);
						clientProfile.setClientAppName(clientName);
						clientProfile.setTimeout(_timeout);
						clientProfile.setReadTimeout(_readTimeout);
						clientProfile.setRedoAble(true);
						clientProfile.setClientThrottle(false);
						SoaClientFactoryBean soaClientFactoryBean=new SoaClientFactoryBean(SearchService.class
								,clientProfile);
						_searchService=new SearchCacheService((SearchService) soaClientFactoryBean.getObject(), clientName);	
//						_searchService=new SearchCacheService((SearchService) soaClientFactoryBean.getObject(),getHistoryLogService(), clientName);			

					}catch(Exception e){
						throw new SearchException("get SearchService proxy failed",e);
					}	
				}
			}
		}
		return _searchService;
	}

	
	/**
	 * @param clientName  调用方的应用名
	 * @return 远程服务的代理对象
	 */
	public static CacheService getCacheService(String clientName){
		return getCacheService(clientName, _serviceVersion);
	}
	
	/**
	 * 
	 * @param clientName  调用方的应用名
	 * @param serviceVersion  服务的版本号
	 * @return 远程服务的代理对象
	 */
	public static CacheService getCacheService(String clientName,String serviceVersion){
		if(_cacheService==null){
			synchronized (SearchClient.class) {
				if(_cacheService==null){
					try{
						ClientProfile clientProfile=new ClientProfile();
						clientProfile.setDomainName(_domainName);
						clientProfile.setServiceName(_cacheServiceName);
						clientProfile.setServiceVersion(serviceVersion);
						clientProfile.setServiceAppName(_serviceAppName);
						clientProfile.setClientAppName(clientName);
						clientProfile.setTimeout(_timeout);
						clientProfile.setReadTimeout(_readTimeout);
						clientProfile.setClientThrottle(false);
						SoaClientFactoryBean soaClientFactoryBean=new SoaClientFactoryBean(CacheService.class
								,clientProfile);
						_cacheService=(CacheService) soaClientFactoryBean.getObject();
					}catch(Exception e){
						throw new SearchException("get CacheService proxy failed",e);
					}	
				}
			}
		}
		return _cacheService;
	}
	
	/**
	 * @param clientName  调用方的应用名
	 * @return 远程服务的代理对象
	 */
	public static O2OShopSearchService getO2oShopSearchService(String clientName){
		return getO2oShopSearchService(clientName, _serviceVersion);
	}
	
	/**
	 * 
	 * @param clientName  调用方的应用名
	 * @param serviceVersion  服务的版本号
	 * @return 远程服务的代理对象
	 */
	public static O2OShopSearchService getO2oShopSearchService(String clientName,String serviceVersion){
		if(_o2oShopSearchService==null){
			synchronized (SearchClient.class) {
				if(_o2oShopSearchService==null){
					try{
						ClientProfile clientProfile=new ClientProfile();
						clientProfile.setDomainName(_domainName);
						clientProfile.setServiceName(_o2oShopSearchServiceName);
						clientProfile.setServiceVersion(serviceVersion);
						clientProfile.setServiceAppName(_serviceAppName);
						clientProfile.setClientAppName(clientName);
						clientProfile.setTimeout(_timeout);
						clientProfile.setReadTimeout(_readTimeout);
						clientProfile.setRedoAble(true);
						clientProfile.setClientThrottle(false);
						SoaClientFactoryBean soaClientFactoryBean=new SoaClientFactoryBean(O2OShopSearchService.class
								,clientProfile);
						_o2oShopSearchService=new O2OShopSearchCacheService((O2OShopSearchService) soaClientFactoryBean.getObject());
					}catch(Exception e){
						throw new SearchException("get SearchService proxy failed",e);
					}	
				}
			}
		}
		return _o2oShopSearchService;
	}

	
	
	/**
	 * @param clientName  调用方的应用名
	 * @return 远程服务的代理对象
	 */
	public static GeoSearchService getGeoSearchService(String clientName){
		return getGeoSearchService(clientName, _serviceVersion);
	}
	
	/**
	 * 
	 * @param clientName  调用方的应用名
	 * @param serviceVersion  服务的版本号
	 * @return 远程服务的代理对象
	 */
	public static GeoSearchService getGeoSearchService(String clientName,String serviceVersion){
		if(_geoSearchService==null){
			synchronized (SearchClient.class) {
				if(_geoSearchService==null){
					try{
						ClientProfile clientProfile=new ClientProfile();
						clientProfile.setDomainName(_domainName);
						clientProfile.setServiceName(_geoServiceName);
						clientProfile.setServiceVersion(serviceVersion);
						clientProfile.setServiceAppName(_serviceAppName);
						clientProfile.setClientAppName(clientName);
						clientProfile.setTimeout(_timeout);
						clientProfile.setReadTimeout(_readTimeout);
						clientProfile.setRedoAble(true);
						clientProfile.setClientThrottle(false);
						SoaClientFactoryBean soaClientFactoryBean=new SoaClientFactoryBean(GeoSearchService.class
								,clientProfile);
						_geoSearchService=(GeoSearchService) soaClientFactoryBean.getObject();
					}catch(Exception e){
						throw new SearchException("get SearchService proxy failed",e);
					}	
				}
			}
		}
		return _geoSearchService;
	}
	
	/**
	 * @param clientName  调用方的应用名
	 * @return 远程服务的代理对象
	 */
	public static SelectionProductSearchService getSelectionProductSearchService(String clientName){
		return getSelectionProductSearchService(clientName, _serviceVersion);
	}
	
	/**
	 * 
	 * @param clientName  调用方的应用名
	 * @param serviceVersion  服务的版本号
	 * @return 远程服务的代理对象
	 */
	public static SelectionProductSearchService getSelectionProductSearchService(String clientName,String serviceVersion){
		if(_selectionProductSearchService==null){
			synchronized (SearchClient.class) {
				if(_selectionProductSearchService==null){
					try{
						ClientProfile clientProfile=new ClientProfile();
						clientProfile.setDomainName(_domainName);
						clientProfile.setServiceName(_selectionProductSearchServiceName);
						clientProfile.setServiceVersion(serviceVersion);
						clientProfile.setServiceAppName(_serviceAppName);
						clientProfile.setClientAppName(clientName);
						clientProfile.setTimeout(_timeout);
						clientProfile.setReadTimeout(_readTimeout);
						clientProfile.setRedoAble(true);
						clientProfile.setClientThrottle(false);
						SoaClientFactoryBean soaClientFactoryBean=new SoaClientFactoryBean(SelectionProductSearchService.class
								,clientProfile);
						_selectionProductSearchService=(SelectionProductSearchService) soaClientFactoryBean.getObject();
					}catch(Exception e){
						throw new SearchException("get SearchService proxy failed",e);
					}
				}
			}
		}
		return _selectionProductSearchService;
	}

	/**
	 * @param clientName  调用方的应用名
	 * @return 远程服务的代理对象
	 */
	public static ShopService getShopService(String clientName){
		return getShopService(clientName, _serviceVersion);
	}

	/**
	 *
	 * @param clientName  调用方的应用名
	 * @param serviceVersion  服务的版本号
	 * @return 远程服务的代理对象
	 */
	public static ShopService getShopService(String clientName,String serviceVersion){
		if(_shopService==null){
			synchronized (SearchClient.class) {
				if(_shopService==null){
					try{
						ClientProfile clientProfile=new ClientProfile();
						clientProfile.setDomainName(_domainName);
						clientProfile.setServiceName(_shopServiceName);
						clientProfile.setServiceVersion(serviceVersion);
						clientProfile.setServiceAppName(_serviceAppName);
						clientProfile.setClientAppName(clientName);
						clientProfile.setTimeout(_timeout);
						clientProfile.setReadTimeout(_readTimeout);
						clientProfile.setRedoAble(true);
						clientProfile.setClientThrottle(false);
						SoaClientFactoryBean soaClientFactoryBean=new SoaClientFactoryBean(ShopService.class
								,clientProfile);
						_shopService= new ShopCacheService((ShopService) soaClientFactoryBean.getObject());
					}catch(Exception e){
						throw new SearchException("get SearchService proxy failed",e);
					}
				}
			}
		}
		return _shopService;
	}

//	private static HistoryLogService getHistoryLogService(){
//		if (historyLogService == null) {
//			synchronized (IndexClient.class) {
//				if (historyLogService == null) {
//					try {
//						historyLogService = IndexClient.getHistoryLogService("search");
//					} catch (Exception e) {
//						LOGGER.error("get HistoryLogService proxy failed", e);
//					}
//				}
//			}
//		}
//		return historyLogService;
//	}

	public static SearchBusinessService getSearchBusinessService(String clientName){
		return getSearchBusinessService(clientName,_serviceVersion);
	}

	/**
	 *
	 * @param clientName  调用方的应用名
	 * @param serviceVersion  服务的版本号
	 * @return 远程服务的代理对象
	 */
	public static SearchBusinessService getSearchBusinessService(String clientName,String serviceVersion){
		if(_searchBusinessService==null){
			synchronized (SearchClient.class) {
				if(_searchBusinessService==null){
					try{
						ClientProfile clientProfile=new ClientProfile();
						clientProfile.setDomainName(_domainName);
						clientProfile.setServiceName(_searchBusinessServiceName);
						clientProfile.setServiceVersion(serviceVersion);
						clientProfile.setServiceAppName(_serviceAppName);
						clientProfile.setClientAppName(clientName);
						clientProfile.setTimeout(_timeout);
						clientProfile.setReadTimeout(_readTimeout);
						clientProfile.setRedoAble(true);
						clientProfile.setClientThrottle(false);
						SoaClientFactoryBean soaClientFactoryBean=new SoaClientFactoryBean(SearchBusinessService.class
								,clientProfile);
						_searchBusinessService=new SearchBusinessCacheService((SearchBusinessService) soaClientFactoryBean.getObject(), clientName);
					}catch(Exception e){
						throw new SearchException("get SearchService proxy failed",e);
					}
				}
			}
		}
		return _searchBusinessService;
	}

}
