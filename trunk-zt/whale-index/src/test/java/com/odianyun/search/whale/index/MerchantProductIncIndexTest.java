package com.odianyun.search.whale.index;

import java.util.ArrayList;
import java.util.List;

import com.odianyun.search.whale.index.api.common.IndexConstants;
import com.odianyun.search.whale.index.realtime.MerchantProductIncIndex;

public class MerchantProductIncIndexTest extends AbstractTest{
	
	public static void main(String[] args) throws Exception{
		MerchantProductIncIndex service=(MerchantProductIncIndex) context.getBean("merchantProductIncIndex");
		List<Long> merchantProductIds=new ArrayList<Long>();
		merchantProductIds.add(101L);
		merchantProductIds.add(102L);
		String mpss=service.process(merchantProductIds, true,IndexConstants.index_alias,IndexConstants.index_type,2);
		System.out.println(mpss);
	}

}
