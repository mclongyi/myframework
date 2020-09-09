package com.odianyun.search.whale.data.model;

import java.util.List;

/**
 * Created by user on 2017/1/20.
 */
public class ProductBarcodeBind {

    //ean码
    private String srcBarcode;

    //关联的ean码
    private String destBarcode;


    public String getSrcBarcode() {
        return srcBarcode;
    }

    public void setSrcBarcode(String srcBarcode) {
        this.srcBarcode = srcBarcode;
    }

    public String getDestBarcode() {
        return destBarcode;
    }

    public void setDestBarcode(String destBarcode) {
        this.destBarcode = destBarcode;
    }

}
