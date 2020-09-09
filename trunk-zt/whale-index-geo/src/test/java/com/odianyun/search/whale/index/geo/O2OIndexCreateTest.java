package com.odianyun.search.whale.index.geo;

import java.io.IOException;

import org.apache.commons.io.IOUtils;

import com.odianyun.search.whale.es.api.ESService;
import com.odianyun.search.whale.es.api.EsIndexSwitcher;

public class O2OIndexCreateTest {
	
	public static void main(String[] args) throws Exception{
		try{
			String mapping_json=IOUtils.toString(EsIndexSwitcher.class.getResourceAsStream("/es/o2o_mapping.json"));
			ESService.createIndex("o2o_test3", mapping_json);
		}catch(Exception e){
			//e.printStackTrace();
		}
		
		
	}

}
