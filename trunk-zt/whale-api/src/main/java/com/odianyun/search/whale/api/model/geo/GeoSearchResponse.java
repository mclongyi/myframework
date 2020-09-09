package com.odianyun.search.whale.api.model.geo;

import com.odianyun.search.whale.api.model.Merchant;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 地理位置搜索返回结果
 *
 * @author zengfenghua
 */
@Data
@NoArgsConstructor
public class GeoSearchResponse implements java.io.Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    //用户当前位置或者收货地址对应的areaCode
    private Long areaCode;

    private List<Merchant> merchants;

    private int companyId;

    private long totalHit;

    public List<Merchant> getMerchants() {
        return merchants;
    }

    public void setMerchants(List<Merchant> merchants) {
        this.merchants = merchants;
    }

    public Long getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(Long areaCode) {
        this.areaCode = areaCode;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public long getTotalHit() {
        return totalHit;
    }

    public void setTotalHit(long totalHit) {
        this.totalHit = totalHit;
    }

    @Override
    public String toString() {
        return "GeoSearchResponse [areaCode=" + areaCode + ", merchants="
                + merchants + "]";
    }


}
