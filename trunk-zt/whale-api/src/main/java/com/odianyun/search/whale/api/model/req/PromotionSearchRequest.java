package com.odianyun.search.whale.api.model.req;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fishcus on 16/11/13.
 */
@Data
@NoArgsConstructor
public class PromotionSearchRequest implements Serializable {

    private Long brandId;

    private List<Integer> promotionTypeList;

    private List<Long> promotionIdList;

    private Long companyId;

    private int start = 0;

    private int count = 10;

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Integer> getPromotionTypeList() {
        return promotionTypeList;
    }

    public void setPromotionTypeList(List<Integer> promotionTypeList) {
        this.promotionTypeList = promotionTypeList;
    }

    public List<Long> getPromotionIdList() {
        return promotionIdList;
    }

    public void setPromotionIdList(List<Long> promotionIdList) {
        this.promotionIdList = promotionIdList;
    }
}
