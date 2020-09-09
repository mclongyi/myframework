package com.odianyun.search.whale.common.remote.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author hs
 * @date 2018/8/27.
 */

@Data
@NoArgsConstructor
public class PromotionRuleDTO implements Serializable, Comparable<PromotionRuleDTO> {
    private static final long serialVersionUID = 1L;
    private Long id;
    private Long promotionId;
    private boolean isSuperposition;
    private Integer conditionType;
    private Long conditionValue;
    private Integer contentType;
    private Integer contentValue;
    private String description;
    private Long merchantId;
    private String merchantName;
    private Boolean flag = true;
    private Integer level;
    private String iconUrl;
    private Integer promotionTimes;
    private BigDecimal discountAmount;
    private BigDecimal lackingValue;
    private Boolean lackingFlag = false;
    private Integer freeShipping = 0;
    private Integer multiLimit;
    private Integer conditionValueUnit;
    private Date conditionStartTime;
    private Date conditionEndTime;
    private String conditionEndTimeStr;
    private BigDecimal presellAmount;
    private List<ProductInfoDTO> virMpAndChilds;

    public Boolean getLackingFlag() {
        return this.lackingFlag;
    }

    public void setLackingFlag(Boolean lackingFlag) {
        this.lackingFlag = lackingFlag;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPromotionId() {
        return this.promotionId;
    }

    public void setPromotionId(Long promotionId) {
        this.promotionId = promotionId;
    }

    public boolean isSuperposition() {
        return this.isSuperposition;
    }

    public void setIsSuperposition(boolean isSuperposition) {
        this.isSuperposition = isSuperposition;
    }

    public Integer getConditionType() {
        return this.conditionType;
    }

    public void setConditionType(Integer conditionType) {
        this.conditionType = conditionType;
    }

    public Long getConditionValue() {
        return this.conditionValue;
    }

    public void setConditionValue(Long conditionValue) {
        this.conditionValue = conditionValue;
    }

    public Integer getContentType() {
        return this.contentType;
    }

    public void setContentType(Integer contentType) {
        this.contentType = contentType;
    }

    public Integer getContentValue() {
        return this.contentValue;
    }

    public void setContentValue(Integer contentValue) {
        this.contentValue = contentValue;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Boolean getFlag() {
        return this.flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public Integer getLevel() {
        return this.level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getIconUrl() {
        return this.iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public Integer getPromotionTimes() {
        return this.promotionTimes;
    }

    public void setPromotionTimes(Integer promotionTimes) {
        this.promotionTimes = promotionTimes;
    }

    public Integer getFreeShipping() {
        return this.freeShipping;
    }

    public void setFreeShipping(Integer freeShipping) {
        this.freeShipping = freeShipping;
    }

    public BigDecimal getLackingValue() {
        return this.lackingValue;
    }

    public void setLackingValue(BigDecimal lackingValue) {
        this.lackingValue = lackingValue;
    }

    public BigDecimal getDiscountAmount() {
        return this.discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public Integer getMultiLimit() {
        return this.multiLimit;
    }

    public void setMultiLimit(Integer multiLimit) {
        this.multiLimit = multiLimit;
    }

    public Integer getConditionValueUnit() {
        return this.conditionValueUnit;
    }

    public void setConditionValueUnit(Integer conditionValueUnit) {
        this.conditionValueUnit = conditionValueUnit;
    }

    public Date getConditionStartTime() {
        return this.conditionStartTime;
    }

    public void setConditionStartTime(Date conditionStartTime) {
        this.conditionStartTime = conditionStartTime;
    }

    public Date getConditionEndTime() {
        return this.conditionEndTime;
    }

    public void setConditionEndTime(Date conditionEndTime) {
        this.conditionEndTime = conditionEndTime;
    }

    public BigDecimal getPresellAmount() {
        return this.presellAmount;
    }

    public void setPresellAmount(BigDecimal presellAmount) {
        this.presellAmount = presellAmount;
    }

    public String getConditionEndTimeStr() {
        return this.conditionEndTimeStr;
    }

    public void setConditionEndTimeStr(String conditionEndTimeStr) {
        this.conditionEndTimeStr = conditionEndTimeStr;
    }

    public List<ProductInfoDTO> getVirMpAndChilds() {
        return this.virMpAndChilds;
    }

    public void setVirMpAndChilds(List<ProductInfoDTO> virMpAndChilds) {
        this.virMpAndChilds = virMpAndChilds;
    }


    @Override
    public int compareTo(PromotionRuleDTO o) {
        Long conditionValue = o.getConditionValue();
        return this.conditionValue.compareTo(conditionValue);
    }
}