package com.odianyun.search.whale.index;

import com.odianyun.search.whale.processor.IndexFlow;

public class MerchantProductIndexFlowTest extends AbstractTest{
	
	public static void main(String[] args) throws Exception{
		IndexFlow flow=(IndexFlow) context.getBean("merchantProductIndexFlow");
		flow.init();
		flow.process();
		flow.done(true);
	}

}
