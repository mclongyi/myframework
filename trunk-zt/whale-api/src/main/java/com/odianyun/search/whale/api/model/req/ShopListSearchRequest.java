package com.odianyun.search.whale.api.model.req;

import com.odianyun.search.whale.api.model.geo.GeoSearchRequest;
import com.odianyun.search.whale.api.model.geo.Point;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 店铺搜索请求
 *
 */
@Data
@NoArgsConstructor
public class ShopListSearchRequest extends GeoSearchRequest {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public ShopListSearchRequest(Point point, Integer companyId) {
		super(point, companyId);
	}

	public ShopListSearchRequest(String address, Integer companyId ) {
		super(address, companyId);
	}
	// 附加商品是否是热卖商品
	private boolean isAdditionalHotProduct = false;
	private boolean isZeroResponseHandler = true;

	//互联网搜索用户唯一id
	private String userId;

	public boolean isAdditionalHotProduct() {
		return isAdditionalHotProduct;
	}

	public void setAdditionalHotProduct(boolean additionalHotProduct) {
		isAdditionalHotProduct = additionalHotProduct;
	}
	//	private SearchRequest searchRequest;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public boolean isZeroResponseHandler() {
		return isZeroResponseHandler;
	}

	public void setZeroResponseHandler(boolean isZeroResponseHandler) {
		this.isZeroResponseHandler = isZeroResponseHandler;
	}
}
