package com.odianyun.search.whale.api.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class PointNumRange implements Serializable{
	public PointNumRange() {
	}

	/**
	 *
	 */
	private static final long serialVersionUID = -7205946211874770631L;

	private Double minPoint;

	private Double maxPoint;

	public PointNumRange(Double minPoint, Double maxPoint){
		this.minPoint=minPoint;
		this.maxPoint=maxPoint;
	}

	public Double getMinPoint() {
		return minPoint;
	}

	public void setMinPoint(Double minPoint) {
		this.minPoint = minPoint;
	}

	public Double getMaxPoint() {
		return maxPoint;
	}

	public void setMaxPoint(Double maxPoint) {
		this.maxPoint = maxPoint;
	}
}
