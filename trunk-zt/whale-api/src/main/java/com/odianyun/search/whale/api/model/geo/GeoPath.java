package com.odianyun.search.whale.api.model.geo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by fishcus on 16/12/28.
 */
@Data
@NoArgsConstructor
public class GeoPath implements Serializable{
    //起始坐标
    private Point origin;
    //终点坐标
    private Point destination;
    //距离
    private Long distance;
    //单位
    private String unit = "m";

    public Point getOrigin() {
        return origin;
    }

    public void setOrigin(Point origin) {
        this.origin = origin;
    }

    public Point getDestination() {
        return destination;
    }

    public void setDestination(Point destination) {
        this.destination = destination;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Long getDistance() {
        return distance;
    }

    public void setDistance(Long distance) {
        this.distance = distance;
    }
}
