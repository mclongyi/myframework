package com.odianyun.search.whale.data.saas.service.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.ibatis.SqlMapClientFactoryBean;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.odianyun.search.whale.data.common.ServiceConstants;
import com.odianyun.search.whale.data.saas.dao.BaseDao;
import com.odianyun.search.whale.data.saas.dao.DataSourceDao;
import com.odianyun.search.whale.data.saas.dao.impl.BaseDaoImpl;
import com.odianyun.search.whale.data.saas.model.DBType;
import com.odianyun.search.whale.data.saas.model.DataSourceConfig;
import com.odianyun.search.whale.data.saas.service.BaseDaoService;
import com.odianyun.search.whale.data.saas.service.CompanyService;
import com.odianyun.search.whale.data.service.impl.AbstractDBService;

public class BaseDaoServiceImpl extends AbstractDBService implements BaseDaoService {

//	@Autowired
	DataSourceDao dataSourceDao;
	
//	@Autowired
	CompanyService companyService;
	
//	@Autowired
//	@Qualifier("sqlMapClient_product")
	SqlMapClientFactoryBean sqlMapClient_product;
		
	static Logger logger = Logger.getLogger(BaseDaoServiceImpl.class);

	Map<String,BaseDao> baseDaoCache=new ConcurrentHashMap<String,BaseDao>();
	
	private static final String driverClassName="com.mysql.jdbc.Driver";
	
	private static final String defalut_Max_Active="10";
	
	private static final String defalut_Max_Idle="5";
	
	private static final String defalut_Min_Idle="3";
	
	private static final String defalut_Max_Wait="10000";
	
	private static final String timeBetweenEvictionRunsMillis="10000";
	
	private static final String testWhileIdle="true";
	
	private static final String validationQuery="SELECT 1 FROM dual";
	
	@Override
	public BaseDao getBaseDao(int companyId, DBType dbType) {
		int companyIdOrVirtualCompanyId=1;
		BaseDao baseDao = baseDaoCache.get(getBaseDaoKey(companyIdOrVirtualCompanyId,dbType.toString()));
		if(null == baseDao){
			return baseDaoCache.get(getBaseDaoKey(ServiceConstants.DEFAULT_COMPANY_ID,dbType.toString()));
		}
		return baseDao;
	}

	/*@Override
	protected void tryReload() throws Exception {
		List<DataSourceConfig> dataSourceConfigs = dataSourceDao.queryAllDataSourceConfig();
		if(CollectionUtils.isNotEmpty(dataSourceConfigs)){
			for(DataSourceConfig config:dataSourceConfigs){
				String baseDaoKey=getBaseDaoKey(config.getCompanyId(),config.getDbType());
				if(!baseDaoCache.containsKey(baseDaoKey)){
					Properties properties=new Properties();
					properties.setProperty("driverClassName", driverClassName);
					properties.setProperty("url", config.getJdbcUrl());
					properties.setProperty("username", config.getUsername());
					properties.setProperty("password", config.getPassword());
					properties.setProperty("maxActive", defalut_Max_Active);
					properties.setProperty("maxIdle", defalut_Max_Idle);
					properties.setProperty("minIdle", defalut_Min_Idle);
					properties.setProperty("maxWait", defalut_Max_Wait);
					properties.setProperty("timeBetweenEvictionRunsMillis", timeBetweenEvictionRunsMillis);
					properties.setProperty("testWhileIdle", testWhileIdle);
					properties.setProperty("validationQuery", validationQuery);
					
					BaseDaoImpl baseDao = new BaseDaoImpl();
					baseDao.setDataSource(BasicDataSourceFactory.createDataSource(properties));
					baseDao.setSqlMapClient((SqlMapClient) sqlMapClient_product.getObject());
					baseDaoCache.put(baseDaoKey, baseDao);
				}
			}
			
		}
		
	}*/
	@Override
	protected void tryReload() throws Exception {
		List<DataSourceConfig> dataSourceConfigs = dataSourceDao.queryAllDataSourceConfig();
		if(CollectionUtils.isNotEmpty(dataSourceConfigs)){
			Map<String,BaseDao> tempBaseDaoCache=new ConcurrentHashMap<String,BaseDao>();

			for(DataSourceConfig config:dataSourceConfigs){
				String baseDaoKey=getBaseDaoKey(config.getCompanyId(),config.getDbType());
					Properties properties=new Properties();
					properties.setProperty("driverClassName", driverClassName);
					properties.setProperty("url", config.getJdbcUrl());
					properties.setProperty("username", config.getUsername());
					properties.setProperty("password", config.getPassword());
					properties.setProperty("maxActive", defalut_Max_Active);
					properties.setProperty("maxIdle", defalut_Max_Idle);
					properties.setProperty("minIdle", defalut_Min_Idle);
					properties.setProperty("maxWait", defalut_Max_Wait);
					properties.setProperty("timeBetweenEvictionRunsMillis", timeBetweenEvictionRunsMillis);
					properties.setProperty("testWhileIdle", testWhileIdle);
					properties.setProperty("validationQuery", validationQuery);
					
					BaseDaoImpl baseDao = new BaseDaoImpl();
					baseDao.setDataSource(BasicDataSourceFactory.createDataSource(properties));
					baseDao.setSqlMapClient((SqlMapClient) sqlMapClient_product.getObject());
					tempBaseDaoCache.put(baseDaoKey, baseDao);
				
			}
			if(tempBaseDaoCache.size() > 0){
				Map<String,BaseDao> oldBaseDaoCache = this.baseDaoCache;
				this.baseDaoCache = tempBaseDaoCache;
				releaseOldDaoResources(oldBaseDaoCache);
			}
			
		}
		
	}
	private void releaseOldDaoResources(Map<String, BaseDao> oldBaseDaoCache) {
		if(null != oldBaseDaoCache && oldBaseDaoCache.size() > 0){
			for(Map.Entry<String, BaseDao> entry : oldBaseDaoCache.entrySet()){
				BaseDaoImpl baseDao = (BaseDaoImpl) entry.getValue();
				if(null != baseDao){
					BasicDataSource basicDataSource = (BasicDataSource)baseDao.getDataSource();
					try {
						basicDataSource.close();
					} catch (SQLException e) {
						logger.error("close dataSource error : "+e.getMessage(),e);
					}finally {
						if(basicDataSource.isClosed()){
							basicDataSource = null;
							baseDao = null;
						}
					}
				}
			}
		}
		
	}

	@Override
	public int getInterval() {
		// TODO Auto-generated method stub
		return 30;
	}
	
	private static String getBaseDaoKey(int companyId, String dbType){
		return companyId+"_"+dbType;
	}

	@Override
	protected void tryReload(List<Long> ids) throws Exception {
		// TODO Auto-generated method stub
		
	}
}
