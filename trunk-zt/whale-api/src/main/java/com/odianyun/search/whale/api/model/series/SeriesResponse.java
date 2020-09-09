package com.odianyun.search.whale.api.model.series;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.List;

@Data
@NoArgsConstructor
public class SeriesResponse implements java.io.Serializable{
	
	List<SeriesAttrResponse> seriesAttrResponses=new LinkedList<SeriesAttrResponse>();

	public List<SeriesAttrResponse> getSeriesAttrResponses() {
		return seriesAttrResponses;
	}

	public void setSeriesAttrResponses(List<SeriesAttrResponse> seriesAttrResponses) {
		this.seriesAttrResponses = seriesAttrResponses;
	}

	@Override
	public String toString() {
		return "SeriesResponse [seriesAttrResponses=" + seriesAttrResponses
				+ "]";
	}
	
	
	

}
