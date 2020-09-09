package com.odianyun.search.whale.index.geo;

import com.odianyun.search.whale.data.manager.SegmentManager;
import com.odianyun.search.whale.data.model.geo.O2OStore;

/**
 * Created by admin on 2016/11/25.
 */
public class GeoSegmentProcessor extends GeoBaseSegmentProcessor {
    private SegmentManager segmentManager;

    public GeoSegmentProcessor(){
        segmentManager = SegmentManager.getInstance();
    }
    @Override
    public void dosegment(O2OStore o2OStore) throws Exception {
        if(segmentManager == null){
            return;
        }else{
            segmentManager.process(o2OStore);
        }
    }
}
