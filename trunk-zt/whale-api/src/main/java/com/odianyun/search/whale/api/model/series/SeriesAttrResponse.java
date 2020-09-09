package com.odianyun.search.whale.api.model.series;

import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class SeriesAttrResponse implements java.io.Serializable,Comparable<SeriesAttrResponse>{
	
    private Long seriesAttrId;
	
	private String seriesAttrName;
	
	List<SeriesMerchantProduct> seriesMerchantProducts=new LinkedList<SeriesMerchantProduct>();

	private Integer sortValue=0;

	public Long getSeriesAttrId() {
		return seriesAttrId;
	}

	public void setSeriesAttrId(Long seriesAttrId) {
		this.seriesAttrId = seriesAttrId;
	}

	public String getSeriesAttrName() {
		return seriesAttrName;
	}

	public void setSeriesAttrName(String seriesAttrName) {
		this.seriesAttrName = seriesAttrName;
	}

	public List<SeriesMerchantProduct> getSeriesMerchantProducts() {
		return seriesMerchantProducts;
	}

	public void setSeriesMerchantProducts(
			List<SeriesMerchantProduct> seriesMerchantProducts) {
		this.seriesMerchantProducts = seriesMerchantProducts;
	}

	@Override
	public int compareTo(SeriesAttrResponse o) {
		return this.sortValue.compareTo(o.getSortValue());
	}

	@Override
	public String toString() {
		return "SeriesAttrResponse{" +
				"seriesAttrId=" + seriesAttrId +
				", seriesAttrName='" + seriesAttrName + '\'' +
				", seriesMerchantProducts=" + seriesMerchantProducts +
				", sortValue=" + sortValue +
				'}';
	}

	public int getSortValue() {
		return sortValue;
	}

	public void setSortValue(int sortValue) {
		this.sortValue = sortValue;
	}

}
