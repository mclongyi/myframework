package com.odianyun.search.whale.api.model.resp;

import com.odianyun.search.whale.api.model.Merchant;
import com.odianyun.search.whale.api.model.MerchantProduct;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fishcus on 16/11/22.
 */
@Data
@NoArgsConstructor
public class ShopSearchResult implements Serializable {

    private Merchant merchant;

    private List<MerchantProduct> merchantProductList = new ArrayList<>();

    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    public List<MerchantProduct> getMerchantProductList() {
        return merchantProductList;
    }

    public void setMerchantProductList(List<MerchantProduct> merchantProductList) {
        this.merchantProductList = merchantProductList;
    }
}
