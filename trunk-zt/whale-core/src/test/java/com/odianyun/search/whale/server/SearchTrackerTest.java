package com.odianyun.search.whale.server;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import com.odianyun.search.whale.api.model.PriceRange;
import com.odianyun.search.whale.api.model.SearchResponse;
import com.odianyun.search.whale.api.model.req.SearchRequest;
import com.odianyun.search.whale.tracker.SearchProcessor;
import com.odianyun.search.whale.tracker.SearchTracker;
import com.odianyun.search.whale.tracker.TrackContext;

import junit.framework.TestCase;

public class SearchTrackerTest extends TestCase {

	
	
	private SearchRequest createSearchRequest() {
		SearchRequest req = new SearchRequest(1);
    	req.setCompanyId(1);
    	req.setKeyword("test-keyword");
    	List<Long> lst = new ArrayList<Long>();
    	lst.add((long)1);
    	lst.add((long)2);
    	List<Integer> lstInt = new ArrayList<Integer>();
    	lstInt.add(1);
    	lstInt.add(2);
		List<String> eans = new ArrayList<String>();
		eans.add("kk");
		//req.setEan_no(eans);
    	req.setCategoryIds(lst);
    	req.setBrandIds(lst);
    	req.setAttrValueIds(lst);
    	req.setCoverProvinceIds(lstInt);
    	req.setPriceRange(new PriceRange(1.0, 99.0));
    	req.setStart(2);
    	req.setCount(100);
    	req.setNavCategoryIds(lst);
    	req.setRequestTime(new Date());
    	return req;
	}
	
	private SearchResponse createSearchResponse() {
		SearchResponse rsp = new SearchResponse();
    	rsp.setTotalHit(99);
    	return rsp;
	}

	public void testSearchMp(){
		System.setProperty("global.config.path", "/svn/odianyun/prop/lyf-branch");

		SearchRequest req = createSearchRequest();

	}

	public void testSearchWorker() throws IOException {
//		System.setProperty("global.config.path", "/data/env");
		System.setProperty("global.config.path", "/svn/odianyun/prop/lyf-branch");

		SearchRequest req = createSearchRequest();
		SearchResponse rsp = createSearchResponse();
    	
    	
		final AtomicLong count = new AtomicLong();
		SearchTracker.registProcessor(new SearchProcessor() {

			@Override
			public void process(TrackContext context) {
				// TODO Auto-generated method stub
				count.incrementAndGet();
//				System.out.println("thread id " + Thread.currentThread().getId() 
//						+ " invoke process, " + count.incrementAndGet());
			}
	    	
	    });
	    
	    
	    int hopeCount = 100;
	    for(int i = 0; i< hopeCount; ++i) {
	    	
	    	
	    	SearchTracker.process(new TrackContext(req, rsp));
//	    	System.out.println("queue size: " + searchWorker.getQueueSize());
	    }
	    
	    
	    SearchTracker.shutDown();
	    while(!SearchTracker.terminate())
	    	;
	    assertEquals(hopeCount, count.get() + SearchTracker.getLostCount());
	}
}
