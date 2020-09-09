package com.odianyun.search.whale.data.model;

public class UpdateTimeRange {
	private String start;
	private String end;
	
	
	public UpdateTimeRange(String start, String end) {
		super();
		this.start = start;
		this.end = end;
	}
	public String getStart() {
		return start;
	}
	public void setStart(String start) {
		this.start = start;
	}
	public String getEnd() {
		return end;
	}
	public void setEnd(String end) {
		this.end = end;
	}
	@Override
	public String toString() {
		return "UpdateTimeRange [start=" + start + ", end=" + end + "]";
	}
	
	
	
	
	

}
