package com.odianyun.search.whale.data.geo.service;

import java.util.List;

import com.odianyun.search.whale.data.model.geo.O2OStore;

public interface O2OStoreService {

	/**
	 * 清空临时表数据
	 * @throws Exception
	 */
//	public void cleanTable() throws Exception;

	/**
	 * 切换门店表
	 * @throws Exception
	 */
//	public void switchTable() throws Exception;

	/**
	 * 校验数据的有效性
	 * @throws Exception
	 */
//	public boolean validation() throws Exception;

	/**
	 * 分页查询门店数据
	 * @throws Exception
	 */
	public List<O2OStore> queryO2OStoresWithPage(int pageNo, int pageSize,int companyId) throws Exception;
	
	/**
	 * 更新门店数据
	 * @throws Exception
	 */
//	public void updateO2OStroes(List<O2OStore> stores) throws Exception;
}
