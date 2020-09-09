package com.odianyun.search.whale.data.model;

import java.util.Date;

/**
 * Created by fishcus on 17/8/19.
 */
public class PointsMallProduct {

    private BusinessProduct businessProduct;
    private Long id;
    private Long mpId;
    private String mpName;
    private Long merchantId;
    private Integer refType;
    private Integer pointType;

    private String refName;
    private String refPic;
    private Long refId;
    private Integer source;
    private Long categoryId;
    private String categoryIdSearch;
    private String navCategoryIdSearch;
    private String categoryNameSearch;
    private String tagWords;

    private Integer managementState;
    private Long exchangedNum;
    private Long totalExchanged;
    private Long totalLimit;

    private Date startTime;
    private Date endTime;
    private Integer priceType;
    private Integer priceMode;
    private Double price;
    private Double pointPrice;

    private Long companyId;
    private Long ruleId;
    // 积分规则是否默认
    private Integer isDefault = 0;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMpId() {
        return mpId;
    }

    public void setMpId(Long mpId) {
        this.mpId = mpId;
    }

    public String getMpName() {
        return mpName;
    }

    public void setMpName(String mpName) {
        this.mpName = mpName;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public Integer getRefType() {
        return refType;
    }

    public void setRefType(Integer refType) {
        this.refType = refType;
    }

    public String getRefName() {
        return refName;
    }

    public void setRefName(String refName) {
        this.refName = refName;
    }

    public String getRefPic() {
        return refPic;
    }

    public void setRefPic(String refPic) {
        this.refPic = refPic;
    }

    public Long getRefId() {
        return refId;
    }

    public void setRefId(Long refId) {
        this.refId = refId;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getManagementState() {
        return managementState;
    }

    public void setManagementState(Integer managementState) {
        this.managementState = managementState;
    }

    public Long getExchangedNum() {
        return exchangedNum;
    }

    public void setExchangedNum(Long exchangedNum) {
        this.exchangedNum = exchangedNum;
    }

    public Long getTotalExchanged() {
        return totalExchanged;
    }

    public void setTotalExchanged(Long totalExchanged) {
        this.totalExchanged = totalExchanged;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getPriceType() {
        return priceType;
    }

    public void setPriceType(Integer priceType) {
        this.priceType = priceType;
    }

    public Integer getPriceMode() {
        return priceMode;
    }

    public void setPriceMode(Integer priceMode) {
        this.priceMode = priceMode;
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

    public String getCategoryIdSearch() {
        return categoryIdSearch;
    }

    public void setCategoryIdSearch(String categoryIdSearch) {
        this.categoryIdSearch = categoryIdSearch;
    }

    public String getNavCategoryIdSearch() {
        return navCategoryIdSearch;
    }

    public void setNavCategoryIdSearch(String navCategoryIdSearch) {
        this.navCategoryIdSearch = navCategoryIdSearch;
    }

    public String getCategoryNameSearch() {
        return categoryNameSearch;
    }

    public void setCategoryNameSearch(String categoryNameSearch) {
        this.categoryNameSearch = categoryNameSearch;
    }

    public String getTagWords() {
        return tagWords;
    }

    public void setTagWords(String tagWords) {
        this.tagWords = tagWords;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public BusinessProduct getBusinessProduct() {
        return businessProduct;
    }

    public void setBusinessProduct(BusinessProduct businessProduct) {
        this.businessProduct = businessProduct;
    }

    public Long getRuleId() {
        return ruleId;
    }

    public void setRuleId(Long ruleId) {
        this.ruleId = ruleId;
    }

    public Long getTotalLimit() {
        return totalLimit;
    }

    public void setTotalLimit(Long totalLimit) {
        this.totalLimit = totalLimit;
    }

    public Integer getPointType() {
        return pointType;
    }

    public void setPointType(Integer pointType) {
        this.pointType = pointType;
    }

    public Integer getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Integer isDefault) {
        this.isDefault = isDefault;
    }
}
