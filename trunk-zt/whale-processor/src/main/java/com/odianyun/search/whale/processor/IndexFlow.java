package com.odianyun.search.whale.processor;

public interface IndexFlow {
	
	public void init() throws Exception;
	
	public boolean process() throws Exception;
	
	public void done(boolean needValidation) throws Exception;

	void afterDone();

}
