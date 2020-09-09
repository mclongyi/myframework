package com.odianyun.search.whale.api.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class PriceRange implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7205946211874770631L;

	private Double minPrice;
	
	private Double maxPrice;
	
	public PriceRange(Double minPrice,Double maxPrice){
		this.minPrice=minPrice;
		this.maxPrice=maxPrice;
	}
	
}
