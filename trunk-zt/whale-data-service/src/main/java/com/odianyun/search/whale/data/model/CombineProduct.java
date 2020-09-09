package com.odianyun.search.whale.data.model;

/**
 * Created by jzz on 2017/1/11.
 * 组合品
 */
public class CombineProduct {
    private Long id;//关联表id
    private Long combine_product_id;//组合品id mpid
    private Long sub_merchant_prod_id;//子品id
    private Integer count;//子品个数
    private Long companyId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCombine_product_id() {
        return combine_product_id;
    }

    public void setCombine_product_id(Long combine_product_id) {
        this.combine_product_id = combine_product_id;
    }

    public Long getSub_merchant_prod_id() {
        return sub_merchant_prod_id;
    }

    public void setSub_merchant_prod_id(Long sub_merchant_prod_id) {
        this.sub_merchant_prod_id = sub_merchant_prod_id;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    @Override
    public String toString() {
        return "CombineProduct{" +
                "id=" + id +
                ", combine_product_id=" + combine_product_id +
                ", sub_merchant_prod_id=" + sub_merchant_prod_id +
                ", count=" + count +
                ", companyId=" + companyId +
                '}';
    }
}
