package com.odianyun.search.whale.api.model;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Fu Yifan on 2017/8/19.
 */
@Data
public class ShopResult implements Serializable {
    private static final long serialVersionUID = -4383853230749716194L;

    private long id;

    private long merchant_id=0;

    private String name="";

    private String logo="";

    private long shop_type=0;

    private String address;

    private int businessState;

    private int hasInSiteService;

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
