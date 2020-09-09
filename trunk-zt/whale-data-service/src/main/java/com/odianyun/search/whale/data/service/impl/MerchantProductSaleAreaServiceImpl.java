package com.odianyun.search.whale.data.service.impl;

import com.odianyun.search.whale.data.dao.MerchantProductSaleAreaDao;
import com.odianyun.search.whale.data.model.*;
import com.odianyun.search.whale.data.service.AbstractCompanyDBService;
import com.odianyun.search.whale.data.service.MerchantProductSaleAreaService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * Created by admin on 2016/12/7.
 */
public class MerchantProductSaleAreaServiceImpl extends AbstractCompanyDBService implements MerchantProductSaleAreaService{

    Logger logger = Logger.getLogger(MerchantProductSaleAreaServiceImpl.class);
    private MerchantProductSaleAreaDao merchantProductSaleAreaDao;
    private Map<Integer,MerProSaleAreaContext> merProSaleAreaContexts = new HashMap<Integer,MerProSaleAreaContext>();


    @Override
    protected void tryReload(int companyId) throws Exception {
        MerProSaleAreaContext merProSaleAreaContext = new MerProSaleAreaContext();
        loadSaleAreaCover(merProSaleAreaContext,companyId);
        loadDefaultSaleArea(merProSaleAreaContext,companyId);
        merProSaleAreaContexts.put(companyId,merProSaleAreaContext);
    }

    private void loadDefaultSaleArea(MerProSaleAreaContext merProSaleAreaContext, int companyId) throws Exception {
        Map<Long,Long> defaultSaleAreaIdMap = merchantProductSaleAreaDao.queryAllDefaultSaleAreaIds(companyId);//key sale_area_id
        Map<Long,Set<SaleAreasCover>> tmpMap = new HashMap<Long,Set<SaleAreasCover>>();//key merchantId
        Map<Long,List<SaleAreasCover>> areaMap = merProSaleAreaContext.saleAreasCoversMap;//key sale_area_id
        if(defaultSaleAreaIdMap != null && defaultSaleAreaIdMap.size() > 0){
            for(Map.Entry<Long,Long> entry : defaultSaleAreaIdMap.entrySet()){
                if(areaMap.containsKey(entry.getKey())){
                    Set<SaleAreasCover> cover = tmpMap.get(entry.getValue());
                    if(cover == null){
                        cover = new HashSet<SaleAreasCover>();
                    }
                    List<SaleAreasCover> tmpList = areaMap.get(entry.getKey());
                    if(tmpList!=null){
                        cover.addAll(tmpList);
                    }
                    tmpMap.put(entry.getValue(),cover);
                }
            }
        }
        merProSaleAreaContext.merchantSaleAreaMap =tmpMap;
    }

    public void reload(List<Long> ids,int companyId){

    }

    private void loadSaleAreaCover(MerProSaleAreaContext merProSaleAreaContext,Integer companyId){
        try {
            List<SaleAreasCover> saleAreaList = merchantProductSaleAreaDao.queryAllSaleArea(companyId);
            if(null != saleAreaList && CollectionUtils.isNotEmpty(saleAreaList)){
                Map<Long,List<SaleAreasCover>> tmpMap = new HashMap<Long,List<SaleAreasCover>>();
                for(SaleAreasCover m:saleAreaList) {
                    List<SaleAreasCover> areas = tmpMap.get(m.getSaleAreaId());
                    if(areas == null){
                        areas = new ArrayList<SaleAreasCover>();
                    }
                    areas.add(m);
                    tmpMap.put(m.getSaleAreaId(),areas);
                }
                merProSaleAreaContext.saleAreasCoversMap = tmpMap;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(),e);
        }
    }

    @Override
    public Map<Long,Set<SaleAreasCover>> queryMerProSaleAreaCoverByMpIds(List<Long> mpIds, Integer companyId) throws Exception {
        if(CollectionUtils.isEmpty(mpIds)){
            throw new Exception("mpIds is null or empty!");
        }
        if(null == companyId){
            throw new Exception("companyId is null!");
        }
        List<MerchantProductSaleArea> mpIdsAreaIds = merchantProductSaleAreaDao.queryAllSaleAreaIdsByMPIds(mpIds,companyId);
        Map<Long,Set<SaleAreasCover>> retMap = new HashMap<Long,Set<SaleAreasCover>>();
        if(!merProSaleAreaContexts.containsKey(companyId)){
            return retMap;
        }
        Map<Long,List<SaleAreasCover>> areaCovers = merProSaleAreaContexts.get(companyId).saleAreasCoversMap;
        if(areaCovers.size()==0){
            return retMap;
        }
        if(CollectionUtils.isNotEmpty(mpIdsAreaIds)){
            for(MerchantProductSaleArea m : mpIdsAreaIds){
                Set<SaleAreasCover> covers = retMap.get(m.getMpId());
                if(covers==null){
                    covers = new HashSet<SaleAreasCover>();
                }
                List<SaleAreasCover> tmpCover = areaCovers.get(m.getSaleAreaId());
                if(tmpCover!=null) {
                    covers.addAll(tmpCover);
                }
                retMap.put(m.getMpId(),covers);
            }
        }
        return retMap;
    }

    @Override
    public Set<SaleAreasCover> queryDefaultSaleCodeByMerchantId(Long merchantId, Integer companyId) throws Exception {
        Set<SaleAreasCover> areaCode = new HashSet<SaleAreasCover>();//如果没有默认销售区域 -1 特殊值表示全国
        if(merProSaleAreaContexts.containsKey(companyId)){
            Map<Long,Set<SaleAreasCover>> tmp  = merProSaleAreaContexts.get(companyId).merchantSaleAreaMap;
            if(tmp.containsKey(merchantId)){
                areaCode = tmp.get(merchantId);
            }
        }
        return areaCode;
    }

    @Override
    public Map<Long,Set<SaleAreasCover>> queryDefaultSaleCodeByMerchantIds(Set<Long> merchantIds, Integer companyId) throws Exception{
        Map<Long,Long> defaultSaleAreaIdMap = merchantProductSaleAreaDao.queryAllDefaultSaleAreaIdsByMerchantIds(merchantIds,companyId);
        Map<Long,Set<SaleAreasCover>> tmpMap = new HashMap<Long,Set<SaleAreasCover>>();//key merchantId
        Map<Long,List<SaleAreasCover>> areaMap = merProSaleAreaContexts.get(companyId).saleAreasCoversMap;//key sale_area_id
        if(defaultSaleAreaIdMap != null && defaultSaleAreaIdMap.size() > 0){
            for(Map.Entry<Long,Long> entry : defaultSaleAreaIdMap.entrySet()){
                if(areaMap.containsKey(entry.getKey())){
                    Set<SaleAreasCover> cover = tmpMap.get(entry.getValue());
                    if(cover == null){
                        cover = new HashSet<SaleAreasCover>();
                    }
                    List<SaleAreasCover> tmpList = areaMap.get(entry.getKey());
                    if(tmpList!=null){
                        cover.addAll(tmpList);
                    }
                    tmpMap.put(entry.getValue(),cover);
                }
            }
        }
        return tmpMap;
    }

    @Override
    public Map<Long, List<Long>> querySupplierMerchantProductRelByMpIds(List<Long> mpIds, int companyId) throws Exception {
        Map<Long, List<Long>> supplierMerchantProductRelMap = new HashMap<>();
        List<SupplierMerchantProductRel> supplierMerchantProductRelList = merchantProductSaleAreaDao.querySupplierMerchantProductRelByMpIds(mpIds, companyId);
        for (SupplierMerchantProductRel supplierMerchantProductRel : supplierMerchantProductRelList) {
            Long merchantProductId = supplierMerchantProductRel.getMerchantProductId();
            if (supplierMerchantProductRelMap.containsKey(merchantProductId)) {
                supplierMerchantProductRelMap.get(merchantProductId).add(supplierMerchantProductRel.getSupplierId());
            } else {
                List<Long> supplierIdList = new ArrayList<>();
                supplierIdList.add(supplierMerchantProductRel.getSupplierId());
                supplierMerchantProductRelMap.put(merchantProductId, supplierIdList);
            }
        }
        return supplierMerchantProductRelMap;
    }

    /**
     * 通过商家id或者供应商id获取对应的销售区域
     * @param merchantIdList
     * @param companyId
     * @return
     * @throws Exception
     */
    @Override
    public Set<SaleAreasCover> queryMerProSaleAreaByMerchantIds(List<Long> merchantIdList, int companyId) throws Exception {
        Set<SaleAreasCover> result = new HashSet<>();
        if (merchantIdList == null || merchantIdList.size() == 0) {
            return result;
        }
        List<Long> saleAreaIds = merchantProductSaleAreaDao.querySaleAreaIdsByMerchantIds(merchantIdList, companyId);
        Map<Long,List<SaleAreasCover>> areaCovers = merProSaleAreaContexts.get(companyId).saleAreasCoversMap;
        if (areaCovers.size() == 0) {
            return result;
        }
        if (saleAreaIds != null && saleAreaIds.size() > 0) {
            for (Long saleAreaId : saleAreaIds) {
                List<SaleAreasCover> saleAreasCovers = areaCovers.get(saleAreaId);
                if (saleAreasCovers != null) {
                    result.addAll(saleAreasCovers);
                }
            }
        }
        return result;
    }

    /**
     * 通过商家+类目获取对应的销售区域
     * @param merchantId
     * @param supplierIdList
     *@param categoryId
     * @param companyId   @return
     * @throws Exception
     */
    @Override
    public Set<SaleAreasCover> queryMerchantCategorySaleAreasByMerchantCategory(List<Long> supplierIdList, Long categoryId, int companyId) throws Exception {
        Set<SaleAreasCover> result = new HashSet<>();
        if (supplierIdList == null || categoryId == null) {
            return result;
        }
        List<Long> saleAreaIds = merchantProductSaleAreaDao.querySaleAreaIdsByMerchantCategory(supplierIdList, categoryId, companyId);
        Map<Long,List<SaleAreasCover>> areaCovers = merProSaleAreaContexts.get(companyId).saleAreasCoversMap;
        if (areaCovers.size() == 0) {
            return result;
        }
        if (saleAreaIds != null && saleAreaIds.size() > 0) {
            for (Long saleAreaId : saleAreaIds) {
                List<SaleAreasCover> saleAreasCovers = areaCovers.get(saleAreaId);
                if (saleAreasCovers != null) {
                    result.addAll(saleAreasCovers);
                }
            }
        }
        return result;
    }


    private class MerProSaleAreaContext{
        Map<Long,List<SaleAreasCover>> saleAreasCoversMap = new HashMap<Long,List<SaleAreasCover>>();//key 为area_cover_id
        Map<Long,Set<SaleAreasCover>> merchantSaleAreaMap = new HashMap<Long,Set<SaleAreasCover>>();//商家默认销售区域 key为商家id
    }

    public MerchantProductSaleAreaDao getMerchantProductSaleAreaDao() {
        return merchantProductSaleAreaDao;
    }

    public void setMerchantProductSaleAreaDao(MerchantProductSaleAreaDao merchantProductSaleAreaDao) {
        this.merchantProductSaleAreaDao = merchantProductSaleAreaDao;
    }

    public int getInterval(){
        return 3;
    }
}
