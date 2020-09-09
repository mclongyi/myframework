package com.odianyun.search.whale.common;

import com.odianyun.search.whale.api.model.geo.Point;

/**
 * Created by fishcus on 16/12/28.
 */
public class GeoPathInfo {
    private Point origin;
    private Point destination;
    private Long distance;

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

    public Long getDistance() {
        return distance;
    }

    public void setDistance(Long distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "GeoPathInfo{" +
                "origin=" + origin +
                ", destination=" + destination +
                ", distance='" + distance + '\'' +
                '}';
    }
}
