package com.odianyun.search.whale.api.model.selectionproduct;

import java.util.LinkedList;
import java.util.List;

import com.odianyun.search.whale.api.model.MerchantProduct;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SelectionProduct implements java.io.Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3662990776683052867L;
	//产品id
	private Long id;
	//产品名称
	private String name;
	//后台类目id
	private Long categoryId;
	//后台类目名称
	private String categoryName;
	//对应的商品
	private List<MerchantProduct> merchantProducts=new LinkedList<MerchantProduct>();
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<MerchantProduct> getMerchantProducts() {
		return merchantProducts;
	}
	public void setMerchantProducts(List<MerchantProduct> merchantProducts) {
		this.merchantProducts = merchantProducts;
	}
	public Long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	@Override
	public String toString() {
		return "SelectionProduct [id=" + id + ", name=" + name
				+ ", categoryId=" + categoryId + ", categoryName="
				+ categoryName + ", merchantProducts=" + merchantProducts + "]";
	}
	
	
	

}
