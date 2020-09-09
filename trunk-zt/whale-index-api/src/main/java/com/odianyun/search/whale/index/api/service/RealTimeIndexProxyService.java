package com.odianyun.search.whale.index.api.service;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.odianyun.search.whale.index.api.common.SearchUpdateSender;
import com.odianyun.search.whale.index.api.common.UpdateType;
import com.odianyun.search.whale.index.api.model.req.IndexApplyDTO;
import com.odianyun.soa.InputDTO;

public class RealTimeIndexProxyService implements RealTimeIndexService{

	private static final Logger LOGGER = Logger.getLogger(RealTimeIndexProxyService.class);

	private static final int CORE_POOL = 50;
	private static final int MAX_POOL = 100;
	private static final int KEEP_ALIVE = 60;
	private static final int QUEUE_SIZE = 500;
	private static final int SLEEP_TIME = 1000;
	
	private RealTimeIndexService realTimeIndexService;

	public RealTimeIndexProxyService(){
	}
	public RealTimeIndexProxyService(RealTimeIndexService realTimeIndexService){
		this.realTimeIndexService = realTimeIndexService;
	}
	
	private static final  ExecutorService es = new ThreadPoolExecutor(CORE_POOL, MAX_POOL, KEEP_ALIVE, TimeUnit.SECONDS,
			new LinkedBlockingQueue<Runnable>(QUEUE_SIZE), new ThreadPoolExecutor.DiscardPolicy());
	
	@Override
	public void updateIndex(List<Long> ids, UpdateType updateType,int companyId) throws Exception {

		doUpdateIndex(ids,updateType,companyId);
		
	}

	private void doUpdateIndex(final List<Long> ids, final UpdateType updateType,final int companyId) {

		try{
			es.submit(new Runnable() {
				@Override
				public void run() {
					try {
						LOGGER.info("通知实时索引" + ids + "-" + updateType + "-" + companyId);
						SearchUpdateSender.sendUpdate(ids, updateType,companyId);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
			});
		}catch(Throwable e){
			LOGGER.error("RealTimeIndexService error : " + e);
		}
		
	}
	@Override
	public void updateIndex(InputDTO<IndexApplyDTO> inputDto) throws Exception {
		IndexApplyDTO indexApply = inputDto.getData();
		doUpdateIndex(indexApply.getIds(),indexApply.getUpdateType(),indexApply.getCompanyId());
		
	}

	@Override
	public void updateIndexStandard(InputDTO<IndexApplyDTO> inputDto) throws Exception {
		IndexApplyDTO indexApply = inputDto.getData();
		doUpdateIndex(indexApply.getIds(),indexApply.getUpdateType(),indexApply.getCompanyId());
	}

}
