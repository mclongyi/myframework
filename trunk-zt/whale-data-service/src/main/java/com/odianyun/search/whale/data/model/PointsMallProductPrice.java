package com.odianyun.search.whale.data.model;

/**
 * Created by fishcus on 17/8/21.
 */
public class PointsMallProductPrice {
    private Long id;
    private Long pointsMallProductId;
    private Long pointRuleId;
    private Long mpId;
    private Double price;
    private Double pointPrice;
    private Integer refType;
    private Long refId;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPointsMallProductId() {
        return pointsMallProductId;
    }

    public void setPointsMallProductId(Long pointsMallProductId) {
        this.pointsMallProductId = pointsMallProductId;
    }

    public Long getPointRuleId() {
        return pointRuleId;
    }

    public void setPointRuleId(Long pointRuleId) {
        this.pointRuleId = pointRuleId;
    }

    public Long getMpId() {
        return mpId;
    }

    public void setMpId(Long mpId) {
        this.mpId = mpId;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getPointPrice() {
        return pointPrice;
    }

    public void setPointPrice(Double pointPrice) {
        this.pointPrice = pointPrice;
    }

    public Integer getRefType() {
        return refType;
    }

    public void setRefType(Integer refType) {
        this.refType = refType;
    }

    public Long getRefId() {
        return refId;
    }

    public void setRefId(Long refId) {
        this.refId = refId;
    }
}
