package com.odianyun.search.whale.api.model.series;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.odianyun.search.whale.api.model.MerchantProduct;

public class SeriesMerchantProduct implements java.io.Serializable,Comparable<SeriesMerchantProduct>{
	
	private Long seriesAttrValueId;
	
	private String seriesAttrValue;
	
	private Integer sortValue=0;
	
	private List<MerchantProduct> merchantProducts=new LinkedList<MerchantProduct>();

	public Long getSeriesAttrValueId() {
		return seriesAttrValueId;
	}

	public void setSeriesAttrValueId(Long seriesAttrValueId) {
		this.seriesAttrValueId = seriesAttrValueId;
	}

	public String getSeriesAttrValue() {
		return seriesAttrValue;
	}

	public void setSeriesAttrValue(String seriesAttrValue) {
		this.seriesAttrValue = seriesAttrValue;
	}

	public List<MerchantProduct> getMerchantProducts() {
		return merchantProducts;
	}

	public void setMerchantProducts(List<MerchantProduct> merchantProducts) {
		this.merchantProducts = merchantProducts;
	}

	public Integer getSortValue() {
		return sortValue;
	}

	public void setSortValue(Integer sortValue) {
		this.sortValue = sortValue;
	}

	@Override
	public int compareTo(SeriesMerchantProduct o) {
		if(this.getSortValue()>o.getSortValue()){
			return 1;
		}else if(this.getSortValue()<o.getSortValue()){
			return -1;
		}else{
			if(this.seriesAttrValue==null || o.getSeriesAttrValue()==null){
				return 0;
			}
			return this.seriesAttrValue.compareTo(o.getSeriesAttrValue());
		}
	}

	@Override
	public String toString() {
		return "SeriesMerchantProduct [seriesAttrValueId=" + seriesAttrValueId
				+ ", seriesAttrValue=" + seriesAttrValue + ", sortValue="
				+ sortValue + ", merchantProducts=" + merchantProducts + "]";
	}

    
	
}
