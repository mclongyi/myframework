package com.odianyun.search.whale.index.geo.build;

import com.odianyun.search.whale.index.geo.GeoProcessor;
import com.odianyun.search.whale.index.geo.GeoSegmentProcessor;
import com.odianyun.search.whale.index.geo.IncIndexProcessor;
import com.odianyun.search.whale.processor.Processor;
import com.odianyun.search.whale.processor.ProcessorsBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/11/29.
 */
public class GeoFullIndexProcessorBuilder implements ProcessorsBuilder {
    @Override
    public List<Processor> build() throws Exception {
        List<Processor> processors =new ArrayList<Processor>();
        processors.add(new GeoProcessor());
        processors.add(new GeoSegmentProcessor());
        processors.add(new IncIndexProcessor());
        return processors;
    }
}
