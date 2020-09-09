package com.odianyun.search.whale.data.model;

public class Volume4Sale {

	/**
	 * @return the sale
	 */
	public Long getSale() {
		return sale;
	}

	/**
	 * @param sale the sale to set
	 */
	public void setSale(Long sale) {
		this.sale = sale;
	}

	/**
	 * @return the realSale
	 */
	public Long getRealSale() {
		return realSale;
	}

	/**
	 * @param realSale the realSale to set
	 */
	public void setRealSale(Long realSale) {
		this.realSale = realSale;
	}

	private Long sale = 0l;
	
	private Long realSale = 0l;
	
	
}
