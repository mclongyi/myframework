package com.odianyun.search.whale.api.model.o2o;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * o2o 店铺内搜索接口定义
 * @author zengfenghua
 *
 */
@Data
@NoArgsConstructor
public class O2OShopSearchRequest implements java.io.Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3293529833277314813L;
	//o2o可能以后需要支持多商家搜索
	private Long merchantId;
	//店铺内搜索词搜索
    private String keyword;
    //商家类目id
    private List<Long> merchantCategoryIds;
    
    private Integer companyId;

	public O2OShopSearchRequest(Integer companyId,Long merchantId) {
		super();
		this.companyId=companyId;
		this.merchantId = merchantId;
	}

	public Long getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public List<Long> getMerchantCategoryIds() {
		return merchantCategoryIds;
	}

	public void setMerchantCategoryIds(List<Long> merchantCategoryIds) {
		this.merchantCategoryIds = merchantCategoryIds;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}
    
	
    

}
