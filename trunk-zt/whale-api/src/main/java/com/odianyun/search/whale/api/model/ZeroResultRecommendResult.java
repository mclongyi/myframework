package com.odianyun.search.whale.api.model;


import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ody on 2016/8/16.
 */
@Data
public class ZeroResultRecommendResult implements Serializable {


    //推荐后的keyword recommendedWords
    private String recommendWord;
    
    //可能感兴趣的分词后的词语列表
    private List<String> mayBeInterestedKeyWords;

    //纠正后的搜索结果
    List<MerchantProduct> merchantProducts = new ArrayList<MerchantProduct>();

    public ZeroResultRecommendResult( ) {
    }

    public ZeroResultRecommendResult(String recommendWord, List<MerchantProduct> merchantProducts) {
        this.recommendWord = recommendWord;
        this.merchantProducts = merchantProducts;
    }

    public String getRecommendWord() {
        return recommendWord;
    }

    public void setRecommendWord(String recommendWord) {
        this.recommendWord = recommendWord;
    }

    public List<MerchantProduct> getMerchantProducts() {
        return merchantProducts;
    }

    public void setMerchantProducts(List<MerchantProduct> merchantProducts) {
        this.merchantProducts = merchantProducts;
    }
    public List<String> getMayBeInterestedKeyWords() {
        return mayBeInterestedKeyWords;
    }

    public void setMayBeInterestedKeyWords(List<String> mayBeInterestedKeyWords) {
        this.mayBeInterestedKeyWords = mayBeInterestedKeyWords;
    }

}
