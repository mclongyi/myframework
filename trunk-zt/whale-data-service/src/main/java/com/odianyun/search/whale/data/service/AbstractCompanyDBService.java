package com.odianyun.search.whale.data.service;

import java.util.List;

import org.apache.log4j.Logger;

import com.odianyun.search.whale.data.manager.CompanyDBCacheManager;

public abstract class AbstractCompanyDBService{
	static Logger log = Logger.getLogger(AbstractCompanyDBService.class);
	
	public AbstractCompanyDBService(){
		CompanyDBCacheManager.instance.registe(this);
	}

	public void reload(int companyId) {
		try {//加载缓存数据
            log.info("companyId:"+companyId+" reload " + this.getClass().getSimpleName());
            tryReload(companyId);
            log.info("companyId:"+companyId+" reload end " + this.getClass().getSimpleName());
        } catch (Throwable ex) {
        	log.error(this.getClass().getSimpleName(),ex);
        }
	}
	
	public void reload(List<Long> ids,int companyId) {
		try {//加载指定id缓存数据
            log.info("companyId:"+companyId+" reload ids:"+ids + " "+this.getClass().getSimpleName());
            tryReload(ids,companyId);
            log.info("end  companyId:"+companyId+" reload ids:"+ids + " "+this.getClass().getSimpleName());
        } catch (Throwable ex) {
        	log.error(this.getClass().getSimpleName(),ex);
        }
	}

    protected abstract void tryReload(int companyId) throws Exception;
    
    protected void tryReload(List<Long> ids,int companyId) throws Exception{};
    
  //reload interval in minutes
     public int getInterval(){
        return 15;
    }
    
    public String getName(){
    	return this.getClass().getCanonicalName();
    }
    

}
