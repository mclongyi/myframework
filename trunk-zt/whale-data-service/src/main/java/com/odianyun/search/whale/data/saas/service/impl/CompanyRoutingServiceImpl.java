package com.odianyun.search.whale.data.saas.service.impl;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.elasticsearch.common.cache.CacheBuilder;
import org.elasticsearch.common.cache.CacheLoader;
import org.elasticsearch.common.cache.LoadingCache;
import org.elasticsearch.common.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.odianyun.search.whale.common.util.ConfigUtil;
import com.odianyun.search.whale.data.common.ServiceConstants;
import com.odianyun.search.whale.data.saas.model.CommonConfig;
import com.odianyun.search.whale.data.saas.model.Company;
import com.odianyun.search.whale.data.saas.model.CompanyAppType;
import com.odianyun.search.whale.data.saas.model.ESClusterConfig;
import com.odianyun.search.whale.data.saas.service.CompanyRoutingService;
import com.odianyun.search.whale.data.saas.service.CompanyService;
import com.odianyun.search.whale.data.saas.service.SaasConfigService;

public class CompanyRoutingServiceImpl implements CompanyRoutingService {

	static Logger logger = Logger.getLogger(CompanyRoutingServiceImpl.class);

	@Autowired
	SaasConfigService saasConfigService;
	
	@Autowired
	CompanyService companyService;
	
	/*static {
		ConfigUtil.loadPropertiesFile("routing.properties");
	}

	private static long autoCacheSize = ConfigUtil.getLong("autoCacheSize", 1*1000);
	private static long expireAfterAccessTime = ConfigUtil.getLong("expireAfterAccessTime", 10);
	private static long expireAfterWriteTime = ConfigUtil.getLong("expireAfterWriteTime", 10);

	private LoadingCache<AutoCacheKey, ESClusterConfig> routingCache = CacheBuilder.newBuilder().concurrencyLevel(8)
			.maximumSize(autoCacheSize).expireAfterAccess(expireAfterAccessTime, TimeUnit.MINUTES)
			.expireAfterWrite(expireAfterWriteTime, TimeUnit.MINUTES).recordStats()
			.build(new CacheLoader<AutoCacheKey, ESClusterConfig>() {
				@Override
				public ESClusterConfig load(AutoCacheKey key) throws Exception {
					return getCompanyESClusterConfig(key);
				}

			});
	
	private LoadingCache<AutoCacheKey, String> aliasNameCache = CacheBuilder.newBuilder().concurrencyLevel(8)
			.maximumSize(autoCacheSize).expireAfterAccess(expireAfterAccessTime, TimeUnit.MINUTES)
			.expireAfterWrite(expireAfterWriteTime, TimeUnit.MINUTES).recordStats()
			.build(new CacheLoader<AutoCacheKey, String>() {
				@Override
				public String load(AutoCacheKey key) throws Exception {
					return getCompanyAliasName(key);
				}

			});*/
	
	@Override
	public ESClusterConfig getCompanyESClusterConfig(Integer companyId, CompanyAppType companyAppType) {
		if(null == companyAppType || null == companyId){
			return null;
		}
		
		/*AutoCacheKey autoCacheKey = new AutoCacheKey(companyId,companyAppType);
		ESClusterConfig esClusterConfig = null;
		try {
			esClusterConfig = routingCache.get(autoCacheKey);
		} catch (ExecutionException e) {
			logger.error("routingCache error : "+ e);
		}
		return esClusterConfig;*/
		Company company = null;
		if (null != company) {
			String indexName = generateIndexName(companyAppType, company.getShortName());
			return getCompanyESClusterConfig(companyId, indexName);
		}
		return null;
	}
	
	/*protected String getCompanyAliasName(AutoCacheKey key) {
		
		Company company = companyService.getCompany(key.getCompanyId());
		if (null != company) {
			String indexName = generateIndexAliasName(key.getCompanyAppType(), company.getShortName());
			return indexName;
		}
		return null;
	}*/

	public String generateIndexName(CompanyAppType companyAppType, String indexShortName){
		StringBuilder sb = new StringBuilder();
		sb.append(companyAppType.getValue())
			.append(ServiceConstants.UNDER_LINE)
			.append(indexShortName);
		return sb.toString();
	}
	
	public String generateIndexAliasName(CompanyAppType companyAppType, String indexShortName){
		StringBuilder sb = new StringBuilder();
		sb.append(companyAppType.getValue())
			.append(ServiceConstants.UNDER_LINE)
			.append(indexShortName);
//			.append(ServiceConstants.UNDER_LINE)
//			.append(ServiceConstants.ALIAS);
		return sb.toString();
	}
	
	private ESClusterConfig getCompanyESClusterConfig(Integer companyId, String indexName) {
		if(StringUtils.isBlank(indexName) || null == companyId){
			return null;
		}
		CommonConfig commonConfig = saasConfigService.getCommonConfig(indexName);
		if(null != commonConfig){
			Integer esClusterId = commonConfig.getEsClusterId();
			if(null != esClusterId){
				return saasConfigService.getESClusterConfig(esClusterId);
			}
		}
		return null;
	}
	
	/*private ESClusterConfig getCompanyESClusterConfig(AutoCacheKey autoCacheKey) {

		Integer companyId = autoCacheKey.getCompanyId();
		CompanyAppType companyAppType = autoCacheKey.getCompanyAppType();

		Company company = companyService.getCompany(companyId);
		if (null != company) {
			String indexName = generateIndexName(companyAppType, company.getShortName());
			return getCompanyESClusterConfig(companyId, indexName);
		}
		return null;
	}*/
	
	/*private static class AutoCacheKey{
		Integer companyId;
		CompanyAppType companyAppType;
		
		public AutoCacheKey(Integer companyId,CompanyAppType companyAppType){
			this.companyId = companyId;
			this.companyAppType = companyAppType;
		}

		public Integer getCompanyId() {
			return companyId;
		}

		public CompanyAppType getCompanyAppType() {
			return companyAppType;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((companyAppType == null) ? 0 : companyAppType.hashCode());
			result = prime * result + ((companyId == null) ? 0 : companyId.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			AutoCacheKey other = (AutoCacheKey) obj;
			if (companyAppType != other.companyAppType)
				return false;
			if (companyId == null) {
				if (other.companyId != null)
					return false;
			} else if (!companyId.equals(other.companyId))
				return false;
			return true;
		}
	}*/

	@Override
	public String getCompanyIndexAliasName(Integer companyId, CompanyAppType companyAppType) {
		if(null == companyAppType || null == companyId){
			return null;
		}
		/*AutoCacheKey autoCacheKey = new AutoCacheKey(companyId,companyAppType);
		String companyIndexAliasName = null;
		try {
			companyIndexAliasName = aliasNameCache.get(autoCacheKey);
		} catch (ExecutionException e) {
			logger.error("companyIndexAliasName error : "+ e);
		}
		return companyIndexAliasName;*/
		Company company = null;
		if (null != company) {
			String indexName = generateIndexAliasName(companyAppType, company.getShortName());
			return indexName;
		}
		return null;
	}

	@Override
	public String getCompanyIndexPreName(Integer companyId, CompanyAppType companyAppType) {
		Company company = null;
		if (null != company) {
			String indexName = generateIndexName(companyAppType, company.getShortName())+"_";
			return indexName;
		}
		return null;
	}

}
