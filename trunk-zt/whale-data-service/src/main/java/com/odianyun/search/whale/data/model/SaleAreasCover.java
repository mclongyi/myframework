package com.odianyun.search.whale.data.model;

/**
 * Created by admin on 2017/2/6.
 */
public class SaleAreasCover {
    private Long saleAreaId;//销售区域表id
    private Long areaCode;//区域code
    private Long parentCode;//父区域id
    private String name;//区域名称
    private Integer level;//优先级

    public Long getSaleAreaId() {
        return saleAreaId;
    }

    public void setSaleAreaId(Long saleAreaId) {
        this.saleAreaId = saleAreaId;
    }

    public Long getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(Long areaCode) {
        this.areaCode = areaCode;
    }

    public Long getParentCode() {
        return parentCode;
    }

    public void setParentCode(Long parentCode) {
        this.parentCode = parentCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "SaleAreasCover{" +
                "saleAreaId=" + saleAreaId +
                ", areaCode=" + areaCode +
                ", parentCode=" + parentCode +
                ", name='" + name + '\'' +
                ", level=" + level +
                '}';
    }
}
