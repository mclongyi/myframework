package com.odianyun.search.whale.api.model.selectionproduct;

import java.util.LinkedList;
import java.util.List;

import com.odianyun.search.whale.common.cache.CacheInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SelectionProductSearchResponse implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4369794371383972608L;
	
	List<SelectionProduct> selectionProducts=new LinkedList<SelectionProduct>();

	public List<SelectionProduct> getSelectionProducts() {
		return selectionProducts;
	}

	public void setSelectionProducts(List<SelectionProduct> selectionProducts) {
		this.selectionProducts = selectionProducts;
	}

	//缓存的key值
	private CacheInfo cacheInfo;
		
	@Override
	public String toString() {
		return "SelectionProductSearchResponse [selectionProducts="
				+ selectionProducts + "]";
	}
	
	
	
	

}
