package com.odianyun.search.whale.common.remote.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author hs
 * @date 2018/8/27.
 */
@Data
@NoArgsConstructor
public class PromotionDTO implements Serializable {
    private static final long serialVersionUID = 3700960917253859459L;
    private Long promotionId;
    private Integer promotionType;
    private Integer frontPromotionType;
    private Integer contentType;
    private List<PromotionRuleDTO> ruleList = new ArrayList();
    private Date startTime;
    private Date endTime;
    private String iconText;
    private String iconUrl;
    private String iconWeight;
    private boolean isSuperposition;
    private Integer isSeckill;
    private Integer individualLimitNum;
    private Integer totalLimitNum;
    private String ruleDesc;
    private BigDecimal startPrice;
    private BigDecimal promPrice;
    private String descriptions;

    public Long getPromotionId() {
        return this.promotionId;
    }

    public void setPromotionId(Long promotionId) {
        this.promotionId = promotionId;
    }

    public Integer getPromotionType() {
        return this.promotionType;
    }

    public void setPromotionType(Integer promotionType) {
        this.promotionType = promotionType;
    }

    public Integer getFrontPromotionType() {
        return this.frontPromotionType;
    }

    public void setFrontPromotionType(Integer frontPromotionType) {
        this.frontPromotionType = frontPromotionType;
    }

    public Integer getContentType() {
        return this.contentType;
    }

    public void setContentType(Integer contentType) {
        this.contentType = contentType;
    }

    public List<PromotionRuleDTO> getRuleList() {
        return this.ruleList;
    }

    public void setRuleList(List<PromotionRuleDTO> ruleList) {
        this.ruleList = ruleList;
    }

    public Date getStartTime() {
        return this.startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return this.endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getIconText() {
        return this.iconText;
    }

    public void setIconText(String iconText) {
        this.iconText = iconText;
    }

    public String getIconUrl() {
        return this.iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getIconWeight() {
        return this.iconWeight;
    }

    public void setIconWeight(String iconWeight) {
        this.iconWeight = iconWeight;
    }

    public boolean isSuperposition() {
        return this.isSuperposition;
    }

    public void setIsSuperposition(boolean isSuperposition) {
        this.isSuperposition = isSuperposition;
    }

    public Integer getIsSeckill() {
        return this.isSeckill;
    }

    public void setIsSeckill(Integer isSeckill) {
        this.isSeckill = isSeckill;
    }

    public Integer getIndividualLimitNum() {
        return this.individualLimitNum;
    }

    public void setIndividualLimitNum(Integer individualLimitNum) {
        this.individualLimitNum = individualLimitNum;
    }

    public Integer getTotalLimitNum() {
        return this.totalLimitNum;
    }

    public void setTotalLimitNum(Integer totalLimitNum) {
        this.totalLimitNum = totalLimitNum;
    }

    public String getRuleDesc() {
        return this.ruleDesc;
    }

    public void setRuleDesc(String ruleDesc) {
        this.ruleDesc = ruleDesc;
    }

    public BigDecimal getStartPrice() {
        return this.startPrice;
    }

    public void setStartPrice(BigDecimal startPrice) {
        this.startPrice = startPrice;
    }

    public BigDecimal getPromPrice() {
        return this.promPrice;
    }

    public void setPromPrice(BigDecimal promPrice) {
        this.promPrice = promPrice;
    }

    public String getDescriptions() {
        return this.descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }
}
