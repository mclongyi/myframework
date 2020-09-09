package com.odianyun.search.whale.processor;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;


public class CountProcessor implements Processor {

	private static Logger log = Logger.getLogger(CountProcessor.class);

	AtomicInteger count=new AtomicInteger();

	ReentrantLock lock = new ReentrantLock();
	
	Condition condition=lock.newCondition();

	@Override
	public String getName() {
		return this.getClass().getSimpleName();
	}

	public int addAndGet(int num) {
		return count.addAndGet(num);
	}

	@Override
	public void process(ProcessorContext processorContext) throws Exception {
		try {
			lock.lock();
			if(count.decrementAndGet()==0){
				log.info("count is 0 and send a signal");
				condition.signalAll();
			}
		} finally {
			lock.unlock();
		}
	}

	public void awaitComplete() throws InterruptedException {
		try {
			lock.lock();
			condition.await(5L,TimeUnit.MINUTES);
			log.info("receive a signal and complete");
		} finally {
			lock.unlock();
		}
	}

	public ReentrantLock getLock() {
		return lock;
	}


}
