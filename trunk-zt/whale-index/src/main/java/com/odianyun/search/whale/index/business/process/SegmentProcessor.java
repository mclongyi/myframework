package com.odianyun.search.whale.index.business.process;

import com.odianyun.search.whale.data.manager.SegmentManager;
import com.odianyun.search.whale.data.model.BusinessProduct;
import com.odianyun.search.whale.index.business.process.base.BaseSegmentProcessor;

public class SegmentProcessor extends BaseSegmentProcessor{

	private SegmentManager segmentManager;

	public SegmentProcessor(){
		segmentManager = SegmentManager.getInstance();
	}
	@Override
	public void dosegment(BusinessProduct businessProduct) throws Exception {
		// TODO Auto-generated method stub
		if(segmentManager == null){
			return;
		}
		segmentManager.process(businessProduct);
	}

}
