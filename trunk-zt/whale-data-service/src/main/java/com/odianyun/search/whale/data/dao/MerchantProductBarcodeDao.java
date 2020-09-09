package com.odianyun.search.whale.data.dao;

import com.odianyun.search.whale.data.model.ProductBarcodeBind;

import java.util.List;
import java.util.Map;

/**
 * Created by user on 2017/1/20.
 * 商品和关联ean码
 */
public interface MerchantProductBarcodeDao {

    //批量
    public List<ProductBarcodeBind> queryEanByDestBarcode(List<String> srcBarcodes, Integer companyId);
}
