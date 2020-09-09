package com.odianyun.search.whale.api.model.geo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by fishcus on 16/12/28.
 */
@NoArgsConstructor
@Data
public class GeoPathResponse implements Serializable{

    private List<GeoPath> geoPaths = new ArrayList<>();

    public List<GeoPath> getGeoPaths() {
        return geoPaths;
    }

    public void setGeoPaths(List<GeoPath> geoPaths) {
        this.geoPaths = geoPaths;
    }
}
