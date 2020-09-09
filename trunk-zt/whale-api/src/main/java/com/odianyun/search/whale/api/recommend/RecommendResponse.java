package com.odianyun.search.whale.api.recommend;

import com.odianyun.search.whale.api.model.MerchantProduct;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zengfenghua on 16/12/11.
 */
public class RecommendResponse implements java.io.Serializable{

    private List<MerchantProduct> merchantProducts=new ArrayList<MerchantProduct>();

    public List<MerchantProduct> getMerchantProducts() {
        return merchantProducts;
    }

    public void setMerchantProducts(List<MerchantProduct> merchantProducts) {
        this.merchantProducts = merchantProducts;
    }


    @Override
    public String toString() {
        return "RecommendResponse{" +
                "merchantProducts=" + merchantProducts +
                '}';
    }
}
