package com.odianyun.search.whale.api.model.req;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 店铺内搜索请求
 *
 */
@Data
@NoArgsConstructor
public class ShopSearchRequest extends BaseSearchRequest{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//商家类目
	private List<Long> merchantCategoryIds;

	public ShopSearchRequest(Integer companyId,Long merchantId){
		super(companyId);
		if(merchantIdList == null){
			merchantIdList = new ArrayList<Long>();
		}
		this.merchantId=merchantId;
		merchantIdList.add(merchantId);
	}

	public ShopSearchRequest(Integer companyId,List<Long> merchantIdList){
		super(companyId);
		this.merchantIdList=merchantIdList;
	}

	public List<Long> getMerchantCategoryIds() {
		return merchantCategoryIds;
	}

	public void setMerchantCategoryIds(List<Long> merchantCategoryIds) {
		this.merchantCategoryIds = merchantCategoryIds;
	}

	@Override
	public String toString(){
		return super.toString() + "ShopRequest [ merchantId="+ merchantId
				+", merchantCategoryIds="+merchantCategoryIds +"]";
	}

}
