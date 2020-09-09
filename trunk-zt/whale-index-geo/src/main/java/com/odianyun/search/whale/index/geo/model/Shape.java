package com.odianyun.search.whale.index.geo.model;


public class Shape {

	private String type;
	
	private  String coordinates;
	
	public Shape(String type,String coordinates){
		this.type = type;
		this.coordinates = coordinates;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(String coordinates) {
		this.coordinates = coordinates;
	}
	
	
}
