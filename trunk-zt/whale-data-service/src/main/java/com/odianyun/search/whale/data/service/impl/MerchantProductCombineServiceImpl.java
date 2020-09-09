package com.odianyun.search.whale.data.service.impl;

import com.odianyun.search.whale.data.dao.MerchantProductCombineDao;
import com.odianyun.search.whale.data.dao.MerchantProductDao;
import com.odianyun.search.whale.data.model.BusinessProduct;
import com.odianyun.search.whale.data.model.CombineProduct;
import com.odianyun.search.whale.data.model.MerchantProduct;
import com.odianyun.search.whale.data.service.AbstractCompanyDBService;
import com.odianyun.search.whale.data.service.MerchantProductCombineService;
import com.odianyun.search.whale.data.service.MerchantProductService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * Created by Botao on 2016/12/28.
 */
public class MerchantProductCombineServiceImpl extends AbstractCompanyDBService implements MerchantProductCombineService {

    static Logger logger = Logger.getLogger(MerchantProductCombineServiceImpl.class);
    @Autowired
    private MerchantProductCombineDao merchantProductCombineDao;
    @Autowired
    private MerchantProductDao merchantProductDao;

    private Map<Integer,CombineContext> combineContexts = new HashMap<Integer,CombineContext>();

    @Override
    public Map<Long, List<MerchantProduct>> querySubMerchantProducts(List<Long> mpIds, int companyId) throws Exception {
        Map<Long,List<MerchantProduct>> retMap = new HashMap<Long,List<MerchantProduct>>();
        if(!combineContexts.containsKey(companyId)){
            return retMap;
        }
        Map<Long,List<CombineProduct>> combineMap = combineContexts.get(companyId).merchantProductCombineMap;

        Set<Long> ids = new HashSet<Long>();//子商品ids
        List<CombineProduct> combineProductList = new ArrayList<CombineProduct>();//关联表list
        for(Long id: mpIds){
            if(combineMap.containsKey(id)){
                combineProductList.addAll(combineMap.get(id));
            }
        }
        if(combineProductList.size()>0){
            for(CombineProduct cp : combineProductList){
                ids.add(cp.getSub_merchant_prod_id());
            }
        }
        if(ids.size()>0){
            List<Long> merchantProductIds = new ArrayList<>(ids);
            List<MerchantProduct> merchantProductList = merchantProductDao.getMerchantProducts(merchantProductIds,companyId);
            Map<Long,MerchantProduct> merchantProductMap = new HashMap<Long,MerchantProduct>();
            if(merchantProductList != null && merchantProductList.size() > 0){
                for(MerchantProduct mp: merchantProductList){
                    merchantProductMap.put(mp.getId(),mp);
                }
            }
            if(merchantProductMap.size()>0){
                for(CombineProduct cp : combineProductList){
                    if(merchantProductMap.containsKey(cp.getSub_merchant_prod_id())) {
                        List<MerchantProduct> list = retMap.get(cp.getCombine_product_id());
                        if (list == null) {
                            list = new ArrayList<MerchantProduct>();
                        }
                        list.add(merchantProductMap.get(cp.getSub_merchant_prod_id()));
                        retMap.put(cp.getCombine_product_id(),list);
                    }
                }
            }
        }
        return retMap;
    }

    @Override
    public List<CombineProduct> queryCombineProductsByMpids(List<Long> merchantProductIds, int companyId) throws Exception {
        List<CombineProduct> retList = new ArrayList<CombineProduct>();
        if(!combineContexts.containsKey(companyId)){
            return retList;
        }
        Map<Long,List<CombineProduct>> combineMap = combineContexts.get(companyId).merchantProductCombineMap;
        for(Long id:merchantProductIds){
            if(combineMap.containsKey(id)){
                retList.addAll(combineMap.get(id));
            }
        }
        return retList;
    }


    @Override
    protected void tryReload(int companyId) throws Exception {
        CombineContext combineContext = new CombineContext();
        logger.info("reload combine product start....");
        loadCombineProduct(combineContext,companyId);
        logger.info("reload combine product end...");
        logger.info("map size = "+combineContext.merchantProductCombineMap.size());
        combineContexts.put(companyId,combineContext);

    }

    private void loadCombineProduct(CombineContext combineContext, int companyId) {
        try {
            List<CombineProduct> combineProductList = merchantProductCombineDao.queryAllCombineProduct(companyId);
            Map<Long,List<CombineProduct>> tmpMap = new HashMap<Long,List<CombineProduct>>();
            if(combineProductList!=null){
                for(CombineProduct cp : combineProductList){
                    List<CombineProduct> list = tmpMap.get(cp.getCombine_product_id());
                    if(list == null){
                        list = new ArrayList<CombineProduct>();
                    }
                    list.add(cp);
                    tmpMap.put(cp.getCombine_product_id(),list);
                }
            }
            combineContext.merchantProductCombineMap = tmpMap;
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
    }



    private class CombineContext{
        Map<Long,List<CombineProduct>> merchantProductCombineMap = new HashMap<Long,List<CombineProduct>>();
    }
}
