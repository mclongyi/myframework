package com.odianyun.search.whale.common.remote.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hs
 * @date 2018/8/27.
 */

@Data
@NoArgsConstructor
public class MPPromotionOutputDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long mpId;
    private Long promotionId;
    private Long merchantId;
    private String merchantName;
    private Integer promotionType;
    private Integer frontPromotionType;
    private Integer buyNum;
    private BigDecimal price;
    private BigDecimal promPrice;
    private List<PromotionDTO> promotionList = new ArrayList();
    private List<PromotionRuleDTO> ruleList = new ArrayList();

    public Long getMpId() {
        return this.mpId;
    }

    public void setMpId(Long mpId) {
        this.mpId = mpId;
    }

    public Long getPromotionId() {
        return this.promotionId;
    }

    public void setPromotionId(Long promotionId) {
        this.promotionId = promotionId;
    }

    public Long getMerchantId() {
        return this.merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantName() {
        return this.merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public Integer getPromotionType() {
        return this.promotionType;
    }

    public void setPromotionType(Integer promotionType) {
        this.promotionType = promotionType;
    }

    public List<PromotionDTO> getPromotionList() {
        return this.promotionList;
    }

    public void setPromotionList(List<PromotionDTO> promotionList) {
        this.promotionList = promotionList;
    }

    public Integer getFrontPromotionType() {
        return this.frontPromotionType;
    }

    public void setFrontPromotionType(Integer frontPromotionType) {
        this.frontPromotionType = frontPromotionType;
    }

    public Integer getBuyNum() {
        return this.buyNum;
    }

    public void setBuyNum(Integer buyNum) {
        this.buyNum = buyNum;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPromPrice() {
        return this.promPrice;
    }

    public void setPromPrice(BigDecimal promPrice) {
        this.promPrice = promPrice;
    }

    public List<PromotionRuleDTO> getRuleList() {
        return this.ruleList;
    }

    public void setRuleList(List<PromotionRuleDTO> ruleList) {
        this.ruleList = ruleList;
    }
}

