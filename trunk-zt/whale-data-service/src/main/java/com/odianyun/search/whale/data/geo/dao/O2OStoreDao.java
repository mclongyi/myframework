package com.odianyun.search.whale.data.geo.dao;

import java.util.List;

import com.odianyun.search.whale.data.model.geo.O2OStore;


public interface O2OStoreDao {
	
	/**
	 * 保存到临时表中
	 * @param stores
	 * @throws Exception
	 */
//	public void saveO2OStore(List<O2OStore> stores) throws Exception;
    
    /**
     * 清空正式表数据
     * @throws Exception
     */
//    public void cleanO2OStore() throws Exception;
    
	/**
	 * 在正式表中删除部分数据
	 * @param storeIds
	 * @throws Exception
	 */
//	public void deleteO2OStores(List<Long> storeIds) throws Exception;
	
	/**
	 * 统计正式表的数据总数
	 * @return
	 * @throws Exception
	 */
//	public Integer countNewAllO2OStores() throws Exception;
	
	
	/**
	 * 统计正式表的数据总数
	 * @return
	 * @throws Exception
	 */
//	public Integer countOldAllO2OStores() throws Exception;
	
	/**
	 * 从正式表中分页读取数据
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public List<O2OStore> queryO2OStoresWithPage(int pageNo,int pageSize,int companyId) throws Exception;
	
//	public void switchTableName();
	
	
}
