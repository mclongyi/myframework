package com.odianyun.search.whale.index.api;

import com.odianyun.search.whale.index.api.service.HessionServiceFactory;
import com.odianyun.search.whale.index.api.service.RealTimeIndexService;

import java.util.ArrayList;
import java.util.List;

public class IncIndexTest {
	public static void main(String[] args) throws Exception{
		System.setProperty("global.config.path","/Users/fishcus/JavaDev/data/env-199");

		String serviceUrl="http://192.168.20.199:8081/index/soa/SOARealTimeIndexService";
//		serviceUrl="http://192.168.1.199:8081/index/soa/SOARealTimeIndexService";
//		serviceUrl="http://120.92.230.233:8081/index/soa/SOARealTimeIndexService";

//		serviceUrl="http://120.92.227.99:8081/index/soa/SOARealTimeIndexService";
//		serviceUrl="http://192.168.20.94:8081/index/soa/SOARealTimeIndexService";

//		serviceUrl="http://120.92.232.180:8081/index/soa/SOARealTimeIndexService";
//		serviceUrl="http://120.92.235.233:8081/index/soa/SOARealTimeIndexService";
		serviceUrl="http://192.168.20.58:8081/index/soa/SOARealTimeIndexService";

		RealTimeIndexService indexService=HessionServiceFactory.getService(serviceUrl, RealTimeIndexService.class);
		
		List<Long> ids = new ArrayList<Long>();
//		ids.add(5601l);
//		ids.add(5604l);
//		ids.add(5607l);
//		ids.add(5617l);
//		ids.add(419l);
		ids.add(1038012300000083l);
//		ids.add(1001011506000006l);
//		ids.add(1053011902000000l);
//		System.setProperty("global.config.path", "/data/env");

//		indexService = IndexClient.getRealTimeIndexService("search");

//		indexService = IndexClient.getRealTimeIndexService("test");
//		indexService.updateIndex(ids, UpdateType.GEO_MERCHANT_ID,11);

	}
}
