package com.odianyun.search.whale.tracker;

import org.apache.log4j.Logger;

import com.odianyun.search.whale.api.model.req.SearchRequest;

public class SearchCountProcessor implements SearchProcessor{

	private final SearchCounterTracker searchCounterTracker = new SearchCounterTracker();
    private final ShopSearchCountTracker shopSearchCountTracker = new ShopSearchCountTracker();
    private final Logger logger = Logger.getLogger(SearchCountProcessor.class);
	
	@Override
	public void process(TrackContext context) {
		try {
			if(context.request instanceof SearchRequest) {
				this.searchCounterTracker.checkHitNum(context.response.getTotalHit(), context.request);
			}
			else {
				this.shopSearchCountTracker.checkHitNum(context.response.getTotalHit(), context.request);
			}
		}
		catch(Exception ex) {
			logger.error("SearchCountProcessor error :" + ex.toString());
		}
	}

}
