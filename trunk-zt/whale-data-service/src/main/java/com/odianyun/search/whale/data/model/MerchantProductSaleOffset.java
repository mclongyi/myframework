package com.odianyun.search.whale.data.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by juzhongzheng on 2016-08-23.
 */
public class MerchantProductSaleOffset {
    //mp_id
    private Long mpId;
    //销量偏差量
    private Long offset = 0L;

    public static final Map<String, String> resultMap = new HashMap<String, String>();

    static{
        resultMap.put("mpId", "merchant_product_id");
        resultMap.put("offset", "offset");
    }

    public Long getMpId() {
        return mpId;
    }

    public void setMpId(Long mpId) {
        this.mpId = mpId;
    }

    public Long getOffset() {
        return offset;
    }

    public void setOffset(Long offset) {
        this.offset = offset;
    }

    public static Map<String, String> getResultMap() {
        return resultMap;
    }
}
