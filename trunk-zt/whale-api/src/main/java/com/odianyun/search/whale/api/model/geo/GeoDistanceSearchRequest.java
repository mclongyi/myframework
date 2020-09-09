package com.odianyun.search.whale.api.model.geo;


public class GeoDistanceSearchRequest extends GeoSearchRequest{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4106255327520072059L;
	//最大距离
	private Distance distance;
	
	public GeoDistanceSearchRequest(Point point, Integer companyId,
			Distance distance) {
		super(point,companyId);
		this.distance = distance;
	}

	public GeoDistanceSearchRequest(String address, Integer companyId, Distance distance) {
		super(address,companyId);
		this.distance = distance;
	}
	
	public Distance getDistance() {
		return distance;
	}

	public void setDistance(Distance distance) {
		this.distance = distance;
	}


	public static class Distance implements java.io.Serializable{
		
		private Double distance; //单位km,多少距离以内
		private Type type=Type.LINE;
		private Double from;
		private Double to;
		public static enum Type{
			LINE, //直线距离
			WALK  //步行距离
		}
		
		public Distance(Double distance) {
			this(distance,Type.LINE);
		}
		
		public Distance(Double from,Double to) {
			this(from,to,Type.LINE);
		}
		
		public Distance(Double from,Double to, Type type) {
			super();
			this.from=from;
			this.to=to;
			this.type = type;
		}

		public Distance(Double distance, Type type) {
			super();
			this.distance = distance;
			this.type = type;
		}

		public Double getDistance() {
			return distance;
		}

		public Type getType() {
			return type;
		}

		public void setType(Type type) {
			this.type = type;
		}

		public Double getFrom() {
			return from;
		}

		public Double getTo() {
			return to;
		}
		
	}

}
