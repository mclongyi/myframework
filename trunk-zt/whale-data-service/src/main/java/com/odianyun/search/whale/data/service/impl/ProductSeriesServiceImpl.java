package com.odianyun.search.whale.data.service.impl;

import com.odianyun.search.whale.data.dao.ProductSeriesDao;
import com.odianyun.search.whale.data.model.MerchantSeriesAttribute;
import com.odianyun.search.whale.data.model.ProductSeriesAttribute;
import com.odianyun.search.whale.data.service.ProductSeriesService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * Created by fishcus on 16/11/17.
 */
public class ProductSeriesServiceImpl implements ProductSeriesService {

    @Autowired
    ProductSeriesDao productSeriesDao;

    @Override
    public Map<Long, List<ProductSeriesAttribute>> getProductAttrValues(List<Long> virtualMerchantProductIds, int companyId) throws Exception {
        Map<Long, List<ProductSeriesAttribute>> seriesAttributes=new HashMap<Long, List<ProductSeriesAttribute>>();
        // AttributeValue对象中只有merchantSeriesId 和 attrNameId
        List<ProductSeriesAttribute> attributeValues = productSeriesDao.queryProductSeriesAttribute(virtualMerchantProductIds,companyId);
        if(CollectionUtils.isNotEmpty(attributeValues)){
            for(ProductSeriesAttribute attributeValue:attributeValues){
                List<ProductSeriesAttribute> attrValues = seriesAttributes.get(attributeValue.getVirtualMerchantProductId());
                if(attrValues==null){
                    attrValues=new LinkedList<ProductSeriesAttribute>();
                    seriesAttributes.put(attributeValue.getVirtualMerchantProductId(), attrValues);
                }
                attrValues.add(attributeValue);
            }
        }
        return seriesAttributes;
    }

    @Override
    public List<Long> getSeriesMerchantProductIds(List<Long> virtualMerchantProductIds, int companyId) throws Exception {
        List<Long> seriesMerchantProductIds = productSeriesDao.querySeriesMerchantProductIds(virtualMerchantProductIds,companyId);
        seriesMerchantProductIds.addAll(virtualMerchantProductIds);
        return seriesMerchantProductIds;
    }
}
