package com.odianyun.search.whale.data.model;

import java.util.HashMap;
import java.util.Map;

public class Shop {

	private long id;
	
	private long merchant_id=0;
	
	private String name="";
	
	private String logo="";
	
	private long shop_type=0;

	private String address;

	private int businessState;

	private int hasInSiteService;
	
	public static final Map<String, String> resultMap = new HashMap<String, String>();
	
	static{
		resultMap.put("id","id" );
        resultMap.put("merchant_id","merchant_id");
        resultMap.put("name","NAME" );
        resultMap.put("logo","logo" );
        resultMap.put("shop_type","shop_type" );
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getMerchant_id() {
		return merchant_id;
	}

	public void setMerchant_id(long merchant_id) {
		this.merchant_id = merchant_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public long getShop_type() {
		return shop_type;
	}

	public void setShop_type(long shop_type) {
		this.shop_type = shop_type;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	public static Map<String, String> getResultmap() {
		return resultMap;
	}

	public int getBusinessState() {
		return businessState;
	}

	public void setBusinessState(int businessState) {
		this.businessState = businessState;
	}

	public int getHasInSiteService() {
		return hasInSiteService;
	}

	public void setHasInSiteService(int hasInSiteService) {
		this.hasInSiteService = hasInSiteService;
	}
}
