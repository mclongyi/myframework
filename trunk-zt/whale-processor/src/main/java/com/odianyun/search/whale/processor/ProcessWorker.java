package com.odianyun.search.whale.processor;

import java.util.concurrent.BlockingQueue;
import org.apache.log4j.Logger;

public class ProcessWorker extends Thread{
	
	private static Logger log = Logger.getLogger(ProcessWorker.class);
	
    private BlockingQueue<ProcessorContext> sourceQueue;
	
	private BlockingQueue<ProcessorContext> sinkQueue;
	
	private Processor processor;
		
	public ProcessWorker(Processor processor,BlockingQueue<ProcessorContext> sourceQueue,
			BlockingQueue<ProcessorContext> sinkQueue){	
		this.processor=processor;
		this.sourceQueue=sourceQueue;
		this.sinkQueue=sinkQueue;

	}

	@Override
	public void run() {
		while(true){			
			try {
				ProcessorContext context=sourceQueue.take();
//				log.info(Thread.currentThread().getName()+" take ProcessContext");
				try{
					if(context.getEndSignal()==false || (context.getEndSignal()==true 
							&& processor instanceof CountProcessor)){
						processor.process(context);
					}				
				}catch (Throwable e) {
					log.error(e.getMessage(),e);
				}
				
				if(sinkQueue!=null){
					sinkQueue.put(context);
				}			
			} catch (Throwable e) {
				log.error(e.getMessage(),e);
			}
			
		}
		
	}

}
