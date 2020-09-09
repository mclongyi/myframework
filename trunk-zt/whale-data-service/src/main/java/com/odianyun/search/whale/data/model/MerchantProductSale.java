package com.odianyun.search.whale.data.model;

/**
 * Created by fishcus on 16/11/23.
 */
public class MerchantProductSale implements Comparable<MerchantProductSale>{

    private Long id;

    private Long companyId;

    private Long merchantId;

    private Long merchantProductId;

    private Long volume4sale = 0l;

    private Integer type;

    private String version;

    public MerchantProductSale(){}

    public MerchantProductSale(Long companyId, Long merchantId, Long merchantProductId, Long volume4sale) {
        this.companyId = companyId;
        this.merchantId = merchantId;
        this.merchantProductId = merchantProductId;
        this.volume4sale = volume4sale;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public Long getMerchantProductId() {
        return merchantProductId;
    }

    public void setMerchantProductId(Long merchantProductId) {
        this.merchantProductId = merchantProductId;
    }

    public Long getVolume4sale() {
        return volume4sale;
    }

    public void setVolume4sale(Long volume4sale) {
        this.volume4sale = volume4sale;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int compareTo(MerchantProductSale o) {
        return this.volume4sale.compareTo(o.volume4sale);
    }
}
