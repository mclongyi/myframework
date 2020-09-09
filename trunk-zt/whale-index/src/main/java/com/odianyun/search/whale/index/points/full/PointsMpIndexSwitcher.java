package com.odianyun.search.whale.index.points.full;

import com.odianyun.search.whale.data.model.MerchantProductSearch;
import com.odianyun.search.whale.es.api.ESService;
import com.odianyun.search.whale.es.api.EsIndexSwitcher;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import java.util.List;

public class PointsMpIndexSwitcher extends EsIndexSwitcher{

	static Logger logger = Logger.getLogger(PointsMpIndexSwitcher.class);

	@Override
	public void indexAllDataWithPage(String indexName) throws Exception{

	}

	@Override
	public void incIndexRemediation(String start_index_time,String indexName,List<Integer> companyIds) throws Exception {

	}

	@Override
	public boolean validate(String s) throws Exception {
		return false;
	}
    
}
