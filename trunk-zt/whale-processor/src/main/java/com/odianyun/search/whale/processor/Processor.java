package com.odianyun.search.whale.processor;

public interface Processor {
	
	public String getName();
		
	public void process(ProcessorContext processorContext) throws Exception;
	
}
