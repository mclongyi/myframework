package com.odianyun.search.whale.api.model.o2o;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.List;

@Data
@NoArgsConstructor
public class O2OShopSearchResponse implements java.io.Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2429998184521717566L;

	private Long merchantId;
	//为了兼容后台和service不支持商家类目的情况，临时方案，前台类目
	private List<O2OShopCategoryResponse> navCategoryResponse=new LinkedList<O2OShopCategoryResponse>();
	
	private List<O2OShopCategoryResponse> shopCategoryResponse=new LinkedList<O2OShopCategoryResponse>();
	
	private int companyId;

	public Long getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}

	public List<O2OShopCategoryResponse> getShopCategoryResponse() {
		return shopCategoryResponse;
	}

	public void setShopCategoryResponse(
			List<O2OShopCategoryResponse> shopCategoryResponse) {
		this.shopCategoryResponse = shopCategoryResponse;
	}

	public List<O2OShopCategoryResponse> getNavCategoryResponse() {
		return navCategoryResponse;
	}

	public void setNavCategoryResponse(
			List<O2OShopCategoryResponse> navCategoryResponse) {
		this.navCategoryResponse = navCategoryResponse;
	}

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}
	
}
