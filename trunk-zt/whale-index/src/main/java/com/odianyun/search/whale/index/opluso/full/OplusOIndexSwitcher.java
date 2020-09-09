package com.odianyun.search.whale.index.opluso.full;

import com.odianyun.search.whale.data.model.MerchantProductSearch;
import com.odianyun.search.whale.es.api.ESService;
import com.odianyun.search.whale.es.api.EsIndexSwitcher;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import java.util.List;

public class OplusOIndexSwitcher extends EsIndexSwitcher{
	
	static Logger logger = Logger.getLogger(OplusOIndexSwitcher.class);

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
	
	public static void main(String[] args) throws Exception{
		MerchantProductSearch merchantProductSearch=new MerchantProductSearch();
		merchantProductSearch.setId(1L);
		merchantProductSearch.setTag_words("test test");
		merchantProductSearch.setCategoryId(3333L);
		System.out.println(convert(merchantProductSearch));
		
		String mapping_json=IOUtils.toString(OplusOIndexSwitcher.class.getResourceAsStream("/es/b2c_mapping.json"));
		ESService.createIndex("b2c_test", mapping_json);
	}
    
}
