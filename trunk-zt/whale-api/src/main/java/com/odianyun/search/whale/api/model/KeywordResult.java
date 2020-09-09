package com.odianyun.search.whale.api.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by zengfenghua on 16/12/4.
 */
@Data
@NoArgsConstructor
public class KeywordResult implements java.io.Serializable{

    private String keyword;

    private Long brandId;

    public boolean isBrandWord(){
       if(brandId!=null && brandId!=0){
           return true;
       }
        return false;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }


    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }
}
