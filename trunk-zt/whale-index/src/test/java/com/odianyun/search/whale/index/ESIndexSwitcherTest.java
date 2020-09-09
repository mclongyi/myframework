package com.odianyun.search.whale.index;

import com.odianyun.search.whale.index.api.common.IndexConstants;
import com.odianyun.search.whale.index.full.MerchantProductIndexSwitcher;

public class ESIndexSwitcherTest extends AbstractTest{
	
	public static void main(String[] args) throws Exception{
		MerchantProductIndexSwitcher service=(MerchantProductIndexSwitcher) context.getBean("esIndexSwitcher");
		service.indexAllDataWithPage(IndexConstants.index_alias);
	}

}
