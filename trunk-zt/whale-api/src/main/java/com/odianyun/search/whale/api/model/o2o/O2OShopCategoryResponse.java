package com.odianyun.search.whale.api.model.o2o;

import java.util.LinkedList;
import java.util.List;

import com.odianyun.search.whale.api.model.MerchantProduct;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class O2OShopCategoryResponse implements java.io.Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5556488293898742738L;
	
	private Long merchantId;
	
	private Long merchantCategoryId;
	
	private String merchantCategoryName;
	
	private List<MerchantProduct> merchantProducts=new LinkedList<MerchantProduct>();

	public Long getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}

	public Long getMerchantCategoryId() {
		return merchantCategoryId;
	}

	public void setMerchantCategoryId(Long merchantCategoryId) {
		this.merchantCategoryId = merchantCategoryId;
	}

	public List<MerchantProduct> getMerchantProducts() {
		return merchantProducts;
	}

	public void setMerchantProducts(List<MerchantProduct> merchantProducts) {
		this.merchantProducts = merchantProducts;
	}

	public String getMerchantCategoryName() {
		return merchantCategoryName;
	}

	public void setMerchantCategoryName(String merchantCategoryName) {
		this.merchantCategoryName = merchantCategoryName;
	}
	
}
