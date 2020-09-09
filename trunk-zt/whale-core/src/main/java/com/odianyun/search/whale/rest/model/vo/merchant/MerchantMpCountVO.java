package com.odianyun.search.whale.rest.model.vo.merchant;

import java.io.Serializable;

/**
 * Created by Fu Yifan on 2017/11/13.
 */
public class MerchantMpCountVO implements Serializable {
    private static final long serialVersionUID = 8849438488809653097L;

    private Long merchantId;
    private Long mpNum; // 门店商品数
    private Long newMpNum; // 门店新增商品数

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public Long getMpNum() {
        return mpNum;
    }

    public void setMpNum(Long mpNum) {
        this.mpNum = mpNum;
    }

    public Long getNewMpNum() {
        return newMpNum;
    }

    public void setNewMpNum(Long newMpNum) {
        this.newMpNum = newMpNum;
    }
}
