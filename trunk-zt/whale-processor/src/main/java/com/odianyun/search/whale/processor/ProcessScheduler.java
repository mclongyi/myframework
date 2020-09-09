package com.odianyun.search.whale.processor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

public class ProcessScheduler {
	
	private static Logger log = Logger.getLogger(ProcessScheduler.class);

    int batchnum = Constans.batch_num;
    
    private String indexName;
    
    private String indexType;
    
    private int companyId;
    
    List<Future> taskList = Collections.synchronizedList(new ArrayList<Future>());
    
	List<DataRecord> dataList = Collections.synchronizedList(new LinkedList<DataRecord>());

	List<Processor> processors = Collections.synchronizedList(new ArrayList<Processor>());
	
	private Lock lock = new ReentrantLock();
	
//	private static final int CORE_POOL = Math.min(Runtime.getRuntime().availableProcessors() + 1, 32) ;
	private static final int CORE_POOL = 4;
	private static final int MAX_POOL = CORE_POOL;
	private static final int KEEP_ALIVE = 60;
	private static final int QUEUE_SIZE = 200;
		
	private static final ExecutorService es = new ThreadPoolExecutor(CORE_POOL, MAX_POOL, KEEP_ALIVE,
			TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(QUEUE_SIZE), new ThreadPoolExecutor.CallerRunsPolicy());
	

    public ProcessScheduler(){
    	
    }
    
    public ProcessScheduler(List<Processor> processors) {
		this.processors = processors;
	}
    
    public ProcessScheduler(List<Processor> processors, int batchnum) {
		this.processors = processors;
		this.batchnum = batchnum;
	}

	public void registProcessor(Processor processor) {
		processors.add(processor);
	}

	public void put(DataRecord dataRecord) throws InterruptedException {	
		dataList.add(dataRecord);
		lock.lock();
		try {
			if (dataList.size() >= batchnum) {
				flush();
			}
		}catch(Exception ex) {
			log.error("put DataRecord error:" + ex);
		}finally {
			lock.unlock();
		}

	}
	
	public void put(List<DataRecord> dataRecordList) throws InterruptedException {	
		dataList.addAll(dataRecordList);
		lock.lock();
		try {
			if (dataList.size() >= batchnum) {
				flush();
			}
		}catch(Exception ex) {
			log.error("put dataRecordList error:" + ex);
		}finally {
			lock.unlock();
		}
	}
	
	private void submitIndexTask(final ProcessorContext context) {
		
		taskList.add(es.submit(new Runnable() {
			
			@Override
			public void run() {
				for(Processor processor : processors){
					try {
						log.info("==>>processor:"+ processors.size() + " size:" + context.getDataRecords().size());
						processor.process(context);
					} catch (Exception e) {
						log.error("error:"+e.getMessage()+e);
						e.printStackTrace();
					}
				}
				
			}
		}));
	}

	public void close() throws InterruptedException{
		flush();
		if(taskList.size() > 0){
			for(Future task : taskList){
				try {
					task.get();
				} catch (Throwable e) {
					log.error(e.getMessage(), e);
				}
			}
		}
		taskList = new ArrayList<Future>();
		
	}
	
	public void flush() throws InterruptedException{
		if(dataList.size() > 0){
			List<DataRecord> tempList = dataList;
			dataList = Collections.synchronizedList(new LinkedList<DataRecord>());
			ProcessorContext context = new ProcessorContext(tempList);
			context.setIndexName(indexName);
			context.setIndexType(indexType);
			context.setCompanyId(companyId);
			submitIndexTask(context);
		}
	}

	public List<DataRecord> getDataList() {
		return dataList;
	}

	public String getIndexName() {
		return indexName;
	}

	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}

	public String getIndexType() {
		return indexType;
	}

	public void setIndexType(String indexType) {
		this.indexType = indexType;
	}

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) throws InterruptedException {
		flush();
		this.companyId = companyId;
	}
	

}
