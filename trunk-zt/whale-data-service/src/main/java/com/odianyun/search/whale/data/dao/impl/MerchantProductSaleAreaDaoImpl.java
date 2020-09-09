package com.odianyun.search.whale.data.dao.impl;

import com.odianyun.search.whale.data.common.ServiceConstants;
import com.odianyun.search.whale.data.dao.MerchantProductSaleAreaDao;
import com.odianyun.search.whale.data.model.*;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import java.util.*;

/**
 * Created by admin on 2016/12/7.
 */
public class MerchantProductSaleAreaDaoImpl extends SqlMapClientDaoSupport implements MerchantProductSaleAreaDao {
    @Override
    public List<MerchantProductSaleArea> queryAllSaleAreaIds(Integer companyId) throws Exception {
        return getSqlMapClientTemplate().queryForList("queryAllSaleAreaIds",companyId);
    }

    @Override
    public List<SaleAreasCover> querySaleAreaByAreaIds(List<Long> areaIds, Integer companyId) throws Exception {
        Map<String,Object> param = new HashMap<String,Object>();
        param.put(ServiceConstants.IDS,areaIds);
        param.put(ServiceConstants.COMPANYID,companyId);
        return getSqlMapClientTemplate().queryForList("querySaleAreaByAreaIds",param);
    }

    @Override
    public List<SaleAreasCover> queryAllSaleArea(Integer companyId) throws Exception {
        return getSqlMapClientTemplate().queryForList("queryAllSaleArea", companyId);
    }

    @Override
    public List<MerchantProductSaleArea> queryAllSaleAreaIdsByMPIds(List<Long> mpIds, Integer companyId) throws Exception {
        Map<String,Object> param = new HashMap<String,Object>();
        param.put(ServiceConstants.IDS,mpIds);
        param.put(ServiceConstants.COMPANYID,companyId);
        return getSqlMapClientTemplate().queryForList("queryAllSaleAreaIdsByMPIds", param);
    }

    @Override
    public Map<Long, Long> queryAllDefaultSaleAreaIds(int companyId) throws Exception {
        return getSqlMapClientTemplate().queryForMap("queryAllDefaultSaleAreaIds", companyId, "sale_area_id", "merchant_id");
    }

    @Override
    public Map<Long, Long> queryAllDefaultSaleAreaIdsByMerchantIds(Set<Long> merchantIds, Integer companyId) throws Exception {
        Map<String,Object> param = new HashMap<String,Object>();
        param.put(ServiceConstants.IDS,new ArrayList<>(merchantIds));
        param.put(ServiceConstants.COMPANYID,companyId);
        return getSqlMapClientTemplate().queryForMap("queryAllDefaultSaleAreaIdsByMerchantIds", param, "sale_area_id", "merchant_id");
    }

    @Override
    public List<SupplierMerchantProductRel> querySupplierMerchantProductRelByMpIds(List<Long> mpIds, int companyId) throws Exception {
        Map<String, Object> param = new HashMap<>();
        param.put("mpIds", mpIds);
        param.put("companyId", companyId);
        return getSqlMapClientTemplate().queryForList("querySupplierMerchantProductRelByMpIds", param);
    }

    @Override
    public List<Long> querySaleAreaIdsByMerchantIds(List<Long> merchantIdList, int companyId) throws Exception {
        Map<String, Object> param = new HashMap<>();
        param.put("merchantIdList", merchantIdList);
        param.put("companyId", companyId);
        return getSqlMapClientTemplate().queryForList("querySaleAreaIdsByMerchantIds", param);
    }

    @Override
    public List<Long> querySaleAreaIdsByMerchantCategory(List<Long> supplierIdList, Long categoryId, int companyId) throws Exception {
        Map<String, Object> param = new HashMap<>();
        param.put("supplierIdList", supplierIdList);
        param.put("categoryId", categoryId);
        param.put("companyId", companyId);
        return getSqlMapClientTemplate().queryForList("querySaleAreaIdsByMerchantCategory", param);
    }
}
