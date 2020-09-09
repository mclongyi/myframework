package com.odianyun.search.whale.api.model.selectionproduct;

import net.sf.cglib.core.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PromotionProduct implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8799402082061761121L;
	//产品编码
	private String productCode;
	//ean码
	private String eanNo ;

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getEanNo() {
		return eanNo;
	}

	public void setEanNo(String eanNo) {
		this.eanNo = eanNo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((eanNo == null) ? 0 : eanNo.hashCode());
		result = prime * result + ((productCode == null) ? 0 : productCode.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PromotionProduct other = (PromotionProduct) obj;
		if (eanNo == null) {
			if (other.eanNo != null)
				return false;
		} else if (eanNo.equals(other.eanNo)) {
			return false;
		}
		if (productCode == null) {
			if (other.productCode != null)
				return false;
		} else if (!productCode.equals(other.productCode))
			return false;
		return true;
	}

	
	
}
