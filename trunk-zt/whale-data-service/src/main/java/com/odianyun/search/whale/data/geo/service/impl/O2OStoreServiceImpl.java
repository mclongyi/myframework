package com.odianyun.search.whale.data.geo.service.impl;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.odianyun.search.whale.common.util.ConfigUtil;
import com.odianyun.search.whale.data.geo.dao.O2OStoreDao;
import com.odianyun.search.whale.data.geo.service.O2OStoreService;
import com.odianyun.search.whale.data.model.geo.O2OStore;

public class O2OStoreServiceImpl implements O2OStoreService {

	static Logger logger = Logger.getLogger(O2OStoreServiceImpl.class);
	
	O2OStoreDao storeDao;
	
	/*@Override
	public void cleanTable() throws Exception {
		storeDao.cleanO2OStore();
	}

	@Override
	public void switchTable() throws Exception {
		storeDao.switchTableName();
	}

	@Override
	public boolean validation() throws Exception {
		int new_count=storeDao.countNewAllO2OStores();
		logger.info("new fullindex o2ostore size is "+new_count);

		int now_count=storeDao.countOldAllO2OStores();
		logger.info("now fullindex o2ostore size is "+now_count);
		return validation(now_count,new_count);
	}*/

	@Override
	public List<O2OStore> queryO2OStoresWithPage(int pageNo, int pageSize,int companyId) throws Exception {
		return storeDao.queryO2OStoresWithPage(pageNo, pageSize,companyId);
	}

	/*@Override
	public void updateO2OStroes(List<O2OStore> stores) throws Exception {
		if(stores==null|| stores.isEmpty())
			return;
		List<Long> idlist = new ArrayList<Long>();
		for(O2OStore os:stores){
			idlist.add(os.getMerchantId());
		}
		storeDao.deleteO2OStores(idlist);
		storeDao.saveO2OStore(stores);
		
	}*/
	
	/*private Boolean validation(int now_count,int new_count) throws Exception{
		if(now_count==0){
			return true;
		}
		double dbRatio = ConfigUtil.getDouble("dbRatio", 0.8);
		BigDecimal new_count_BigDecimal=new BigDecimal(new_count);
		BigDecimal now_count_BigDecimal=new BigDecimal(now_count);
		double ratio=new_count_BigDecimal.divide(now_count_BigDecimal,new MathContext(2)).doubleValue();
		if(ratio<dbRatio){
			return false;
		}
		return true;
	}*/

	public O2OStoreDao getStoreDao() {
		return storeDao;
	}

	public void setStoreDao(O2OStoreDao storeDao) {
		this.storeDao = storeDao;
	}
	
	

}
