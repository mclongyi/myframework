package com.odianyun.search.whale.data.service.impl;

import java.util.List;

import org.apache.log4j.Logger;

import com.odianyun.search.whale.data.manager.DBCacheManager;

/**
 * 数据库join缓存服务基类
 *
 * @author ody
 *
 */
public abstract class AbstractDBService {
	private static final Logger LOG = Logger.getLogger(AbstractDBService.class);

	public AbstractDBService() {
		DBCacheManager.instance.registe(this);
	}

	public void reload() {
		try {
			LOG.info("reload " + this.getClass().getSimpleName());
			tryReload();
			LOG.info("reload end " + this.getClass().getSimpleName());
		} catch (Throwable ex) {
			LOG.error(this.getClass().getSimpleName(), ex);
		}
	}
	
	public void reload(List<Long> ids) {
		try {
			LOG.info("reload " + this.getClass().getSimpleName());
			tryReload(ids);
			LOG.info("reload ids : " + ids );
			LOG.info("reload end " + this.getClass().getSimpleName());
		} catch (Throwable ex) {
			LOG.error(this.getClass().getSimpleName(), ex);
		}
	}

	protected abstract void tryReload() throws Exception;
	
	protected abstract void tryReload(List<Long> ids) throws Exception;

	// reload interval in minutes
	public int getInterval(){
        return 15;
	}

	public String getName() {
		return this.getClass().getCanonicalName();
	}

}
