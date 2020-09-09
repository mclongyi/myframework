package com.odianyun.search.whale.data.dao.impl;

import com.odianyun.search.whale.data.common.ServiceConstants;
import com.odianyun.search.whale.data.dao.MerchantProductBarcodeDao;
import com.odianyun.search.whale.data.model.ProductBarcodeBind;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by user on 2017/1/20.
 */
public class MerchantProductBarcodeDaoImpl extends SqlMapClientDaoSupport implements MerchantProductBarcodeDao {

    @Override
    public List<ProductBarcodeBind> queryEanByDestBarcode(List<String> destBarcode,Integer companyId) {

        HashMap<String,Object> paramMap = new HashMap<>();
        paramMap.put(ServiceConstants.DEST_BARCODE, destBarcode);
        paramMap.put(ServiceConstants.COMPANYID, companyId);
        return getSqlMapClientTemplate().queryForList("queryEansByDestbarcode",paramMap);


    }

}
