package com.odianyun.search.whale.data.geo.dao.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.orm.ibatis.SqlMapClientCallback;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.ibatis.sqlmap.client.SqlMapExecutor;
import com.odianyun.search.whale.data.geo.dao.O2OStoreDao;
import com.odianyun.search.whale.data.model.geo.O2OStore;

public class O2OStoreDaoImpl extends SqlMapClientDaoSupport implements
		O2OStoreDao {
	
    static String tableNameA="O2OStore_a";
	
	static String tableNameB="O2OStore_b";
	
	public String tableName=tableNameA;

	/*@Override
	public void saveO2OStore(final List<O2OStore> stores) throws Exception {
		getSqlMapClientTemplate().execute(new SqlMapClientCallback() {
			 
	           @Override
	           public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
	               executor.startBatch();
	               int i = 1;
	               for (O2OStore store : stores) {
	            	   store.setTableName(tableName);
	                   executor.insert("saveO2OStore", store);
	                   if (i % 500 == 0) {
	                       // executeBatch会将inBatch置为false
	                       executor.executeBatch();
	                       // 需要再启动一次
	                       executor.startBatch();
	                   }
	                   i++;
	               }
	               executor.executeBatch();
	               return null;
	           }
	       });

	}

	@Override
	public void cleanO2OStore() throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tableName", tableName);
		getSqlMapClientTemplate().delete("cleanO2OStore",params);

	}

	@Override
	public void deleteO2OStores(List<Long> storeIds) throws Exception {
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tableName", tableName);
		if(CollectionUtils.isNotEmpty(storeIds)){
			StringBuffer sb=new StringBuffer();
			for(int i=0;i<storeIds.size();i++){
				if(i==storeIds.size()-1){
					sb.append(storeIds.get(i));
				}else{
					sb.append(storeIds.get(i)+",");
				}			
			}
			params.put("ids",sb.toString());
			getSqlMapClientTemplate().delete("deleteO2OStores", params);
		}
	}

	@Override
	public Integer countNewAllO2OStores() throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tableName", tableName);
		return (Integer) getSqlMapClientTemplate().queryForObject("countAllO2OStores",params);
	}
	
	@Override
	public Integer countOldAllO2OStores() throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tableName", tableName.equals(tableNameA)?tableNameB:tableNameA);
		return (Integer) getSqlMapClientTemplate().queryForObject("countAllO2OStores",params);
	}*/


	@SuppressWarnings("unchecked")
	@Override
	public List<O2OStore> queryO2OStoresWithPage(int pageNo, int pageSize,int companyId)
			throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
        int startIndex = (pageNo - 1) * pageSize;
        params.put("startIndex", startIndex);
        params.put("pageSize", pageSize);
        params.put("tableName", tableName);
		return getSqlMapClientTemplate().queryForList("queryO2OStoresWithPage",params);
	}

	/*@Override
	public void switchTableName() {
		if(tableName.equals(tableNameA)){
			tableName=tableNameB;
		}else{
			tableName=tableNameA;
		}
	}*/

}
