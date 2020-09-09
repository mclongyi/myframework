package com.odianyun.search.whale.common.remote.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author hs
 * @date 2018/8/27.
 */
public class MPPromotionInputDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long mpId;
    private Long seriesParentId;
    private Long merchantId;
    private String productCode;
    private BigDecimal price;
    private Integer num;

    public MPPromotionInputDTO() {
    }


    public Long getMpId() {
        return this.mpId;
    }

    public Long getSeriesParentId() {
        return this.seriesParentId;
    }

    public void setSeriesParentId(Long seriesParentId) {
        this.seriesParentId = seriesParentId;
    }

    public void setMpId(Long mpId) {
        this.mpId = mpId;
    }

    public Long getMerchantId() {
        return this.merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public String getProductCode() {
        return this.productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getNum() {
        return this.num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return "MPPromotionInputDTO{" +
                "mpId=" + mpId +
                ", seriesParentId=" + seriesParentId +
                ", merchantId=" + merchantId +
                ", productCode='" + productCode + '\'' +
                ", price=" + price +
                ", num=" + num +
                '}';
    }
}