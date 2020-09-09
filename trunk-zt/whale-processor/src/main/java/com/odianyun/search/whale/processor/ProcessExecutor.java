package com.odianyun.search.whale.processor;

import java.util.concurrent.BlockingQueue;

public class ProcessExecutor{
	
	private BlockingQueue<ProcessorContext> sourceQueue;
	
	private BlockingQueue<ProcessorContext> sinkQueue;
	
	private Processor processor;
	
	private int threadnum=Constans.thread_num;
	
	public ProcessExecutor(Processor processor,BlockingQueue<ProcessorContext> sourceQueue,
			BlockingQueue<ProcessorContext> sinkQueue){
		this(processor,sourceQueue,sinkQueue,Constans.thread_num);
	}
	
	public ProcessExecutor(Processor processor,BlockingQueue<ProcessorContext> sourceQueue,
			BlockingQueue<ProcessorContext> sinkQueue,int threadnum){
		this.processor=processor;
		this.sourceQueue=sourceQueue;
		this.sinkQueue=sinkQueue;
		this.threadnum=threadnum;
	}
	
	public void execute(){
		for(int i=0;i<threadnum;i++){
			ProcessWorker worker=new ProcessWorker(this.processor,this.sourceQueue,this.sinkQueue);		
			worker.setName(this.processor.getName()+"_"+i);
			worker.setDaemon(true);
			worker.start();
		}
		
	}
	

}
