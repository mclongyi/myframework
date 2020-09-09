package com.odianyun.search.whale.es.api;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequestBuilder;

public class MerchantGeoDataCreateTest {
	
	public static void main(String[] args) throws IOException{
		String mapping_json=IOUtils.toString(MerchantGeoDataCreateTest.class.getResourceAsStream("data.json"));
        System.out.println(mapping_json);
       // ESClient.getClient().delete(new DeleteRequest("o2o_2016-01-20_21-07-22","merchant","165"));
        IndexRequestBuilder indexRequestBuilder=ESClient.getClient().prepareIndex("o2o_alias","merchant","165").setSource(mapping_json);
        indexRequestBuilder.execute();
	}

}
