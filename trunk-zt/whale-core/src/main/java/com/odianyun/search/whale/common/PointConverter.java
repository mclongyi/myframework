package com.odianyun.search.whale.common;

import com.odianyun.search.whale.api.model.geo.Point;
import org.apache.commons.collections.CollectionUtils;
import org.elasticsearch.search.SearchHitField;

import java.util.List;

/**
 * Created by fishcus on 16/12/29.
 */
public class PointConverter {

    public static Point convert(SearchHitField point){
        if(point == null || CollectionUtils.isEmpty(point.getValues())){
            return null;
        }

        Double lon = (Double) point.getValues().get(0);
        Double lat = (Double) point.getValues().get(1);

        return new Point(lon,lat);
    }
}
