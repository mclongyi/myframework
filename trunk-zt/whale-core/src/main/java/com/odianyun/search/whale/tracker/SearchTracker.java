package com.odianyun.search.whale.tracker;

import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.log4j.Logger;

public class SearchTracker {

	private static final ThreadPoolExecutor worker;
	private static final LinkedList<SearchProcessor> lstProcessor = new LinkedList<SearchProcessor>();
	private static final int bufferSize = 1000;
	private static final int threadAmount = 8;
	private static final Logger logger = Logger.getLogger(SearchTracker.class);
	private static AtomicLong lost = new AtomicLong();
	
	static {//初始化线程池和处理模块
		
		worker = new ThreadPoolExecutor(threadAmount, threadAmount * 2, 1, TimeUnit.MINUTES,
				new LinkedBlockingQueue<Runnable>(bufferSize), new RejectedExecutionHandler() {

					@Override
					public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {

//						lost.incrementAndGet();
						logger.error("queue is full, request lost!");
					}

				});
		createProcessor();
	}
	
	private SearchTracker() {
		
	}
	
	
	private static void createProcessor() {
		logger.info("createProcessor begin!");
		lstProcessor.add(new OmqSendProcessor());
		lstProcessor.add(new SearchCountProcessor());
		logger.info("createProcessor success!");
	}
	
	public static int getQueueSize() {
		return worker.getQueue().size();
	}
	
	public static long getLostCount() {
		return lost.get();
	}
	
	public static void registProcessor(SearchProcessor processor) {
		lstProcessor.add(processor);
	}
	
	public static void shutDown() {
		worker.shutdown();
	}
	
	public static boolean terminate() {
		return worker.isTerminated();
	}
	
	public static void process(final TrackContext context) {
		
		worker.submit(new Runnable() {//将track任务丢给线程池处理

			@Override
			public void run() {
				try {
					for(SearchProcessor processor : lstProcessor) {
						processor.process(context);
					}
				}
				catch(Throwable ex) {
					logger.error("SearchWorker process error :" + ex, ex);
				}
			}
			
		});
	}
}
