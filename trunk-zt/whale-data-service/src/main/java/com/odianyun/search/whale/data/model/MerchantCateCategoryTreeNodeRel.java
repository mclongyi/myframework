package com.odianyun.search.whale.data.model;

/**
 * Created by zengfenghua on 17/7/17.
 */
public class MerchantCateCategoryTreeNodeRel {

    private Long merchantId;

    private Long cateTreeNodeId;

    private Long merchantCateTreeNodeId;


    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public Long getCateTreeNodeId() {
        return cateTreeNodeId;
    }

    public void setCateTreeNodeId(Long cateTreeNodeId) {
        this.cateTreeNodeId = cateTreeNodeId;
    }

    public Long getMerchantCateTreeNodeId() {
        return merchantCateTreeNodeId;
    }

    public void setMerchantCateTreeNodeId(Long merchantCateTreeNodeId) {
        this.merchantCateTreeNodeId = merchantCateTreeNodeId;
    }
}
