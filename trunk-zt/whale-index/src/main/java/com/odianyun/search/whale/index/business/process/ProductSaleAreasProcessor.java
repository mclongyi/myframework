package com.odianyun.search.whale.index.business.process;


import com.odianyun.search.whale.data.model.BusinessProduct;
import com.odianyun.search.whale.data.model.SaleAreasCover;
import com.odianyun.search.whale.data.service.AreaService;
import com.odianyun.search.whale.data.service.MerchantProductSaleAreaService;
import com.odianyun.search.whale.index.business.process.base.BaseProductSaleAreasProcessor;
import com.odianyun.search.whale.index.common.O2OConstants;
import com.odianyun.search.whale.index.common.ProcessorApplication;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by admin on 2016/12/8.
 */
public class ProductSaleAreasProcessor extends BaseProductSaleAreasProcessor {

    AreaService areaService;
    MerchantProductSaleAreaService merchantProductSaleAreaService;
    public ProductSaleAreasProcessor(){
        merchantProductSaleAreaService = (MerchantProductSaleAreaService) ProcessorApplication.getBean("merchantProductSaleAreaService");
        areaService = (AreaService) ProcessorApplication.getBean("areaService");
    }
	private Logger logger = LoggerFactory.getLogger(ProductSaleAreasProcessor.class);
    @Override
    public void calcProductSaleAreas(Map<Long, BusinessProduct> map, int companyId) throws Exception {
        //List<Long> mpIds = new ArrayList<>(map.keySet());
    	//外卖修改 start
		List<Long> mpIdsO2O = null;
		List<Long> mpIds = new ArrayList<Long>();
		for(Map.Entry<Long, BusinessProduct> entry : map.entrySet()){
			Long mpId = entry.getKey();
			BusinessProduct businessProduct = entry.getValue();
			if(O2OConstants.MemberType_O2O.equals(businessProduct.getMerchantType())) {
				if(mpIdsO2O == null) {
					mpIdsO2O = new ArrayList<Long>();
				}
				Long seriesParentId;
				if(businessProduct.getSeriesParentId() == null || businessProduct.getSeriesParentId() == 0L) {
					seriesParentId = mpId;
				}else {
					seriesParentId = businessProduct.getSeriesParentId();
				}
				mpIdsO2O.add(seriesParentId);
			}else {
				mpIds.add(mpId);
			}
		}
		Map<Long,Set<SaleAreasCover>> saleAreaCodesO2O = null;
		if(mpIdsO2O != null && mpIdsO2O.size() > 0) {
		    saleAreaCodesO2O = merchantProductSaleAreaService.queryMerProSaleAreaCoverByMpIds(mpIdsO2O,companyId);
		}
		if(saleAreaCodesO2O == null || saleAreaCodesO2O.size() == 0) {
			saleAreaCodesO2O = new HashMap<Long,Set<SaleAreasCover>>();
		}
		Map<Long,Set<SaleAreasCover>> saleAreaCodes = new HashMap<Long,Set<SaleAreasCover>>();
		Map<Long, List<Long>> supplierMerchantProductRel = new HashMap<Long, List<Long>>();
		if(mpIds != null && mpIds.size() > 0) {
			saleAreaCodes = merchantProductSaleAreaService.queryMerProSaleAreaCoverByMpIds(mpIds,companyId);
			supplierMerchantProductRel = merchantProductSaleAreaService.querySupplierMerchantProductRelByMpIds(mpIds, companyId);
		}
        // 通过mpId到supplier_merchant_product_rel表获取其供应商id
        
        /*if(null==saleAreaCodes){
            return;
        }*/
        for(Map.Entry<Long,BusinessProduct> entry : map.entrySet()){
            Long id = entry.getKey();
            BusinessProduct businessProduct = entry.getValue();
            Set<SaleAreasCover> saleAreasCovers = null;
            if(O2OConstants.MemberType_O2O.equals(businessProduct.getMerchantType())) {
            	saleAreasCovers = saleAreaCodesO2O.get(businessProduct.getSeriesParentId());
            }else {
            	saleAreasCovers = saleAreaCodes.get(id);
            }
            //外卖修改 end
            if(CollectionUtils.isEmpty(saleAreasCovers)){
                saleAreasCovers = new HashSet<>();
            }
            // 获取商家或者供应商的销售区域返回值
            if(supplierMerchantProductRel.size() > 0) {
            List<Long> supplierIdList = supplierMerchantProductRel.get(businessProduct.getId());
            	if(supplierIdList != null && supplierIdList.size() > 0) {
		            Set<SaleAreasCover> merchantSaleAreasCover = merchantProductSaleAreaService.queryMerProSaleAreaByMerchantIds(supplierIdList, companyId);
		            saleAreasCovers.addAll(merchantSaleAreasCover);
		            // 通过商家类目查找是否设置销售区域
		            Set<SaleAreasCover> merchantCategorySaleAreasCover = merchantProductSaleAreaService.queryMerchantCategorySaleAreasByMerchantCategory(supplierIdList, businessProduct.getCategoryId(), companyId);
		            saleAreasCovers.addAll(merchantCategorySaleAreasCover);
            	}

            }


            // 设置销售区域到商品对象中
            if (CollectionUtils.isNotEmpty(saleAreasCovers)) {
                businessProduct.setSaleAreaCodes(areaCodeBuild(saleAreasCovers));
                businessProduct.setSearchAreaCodes(searchAreaCodeBuild(saleAreasCovers,companyId));
            } else {
                Set<SaleAreasCover> defaultAreaCode = merchantProductSaleAreaService.queryDefaultSaleCodeByMerchantId(businessProduct.getMerchantId(),companyId);
                if(CollectionUtils.isNotEmpty(defaultAreaCode)){
                    businessProduct.setSaleAreaCodes(areaCodeBuild(defaultAreaCode));
                    businessProduct.setSearchAreaCodes(searchAreaCodeBuild(defaultAreaCode,companyId));
                }else {
                    businessProduct.setSaleAreaCodes("-1");//默认全国 -1 特殊标记
                    businessProduct.setSearchAreaCodes("-1");
                }
            }
        }
    }

    private String areaCodeBuild(Set<SaleAreasCover> saleAreasCovers) {
        StringBuffer retStr = new StringBuffer("");
        int i = 0;
        for(SaleAreasCover cover : saleAreasCovers){
            i++;
            retStr.append(cover.getAreaCode());
            if(i < saleAreasCovers.size()){
                retStr.append(" ");
            }
        }
        return retStr.toString();
    }


    private String searchAreaCodeBuild(Set<SaleAreasCover> saleAreasCovers,int companyId) throws Exception{
        StringBuffer retCodes = new StringBuffer();
        Set<Long> areaCodeSet = new HashSet<Long>();
        for(SaleAreasCover cover : saleAreasCovers){
            List<Long> codes = areaService.getAllParentAreaCode(cover.getAreaCode(),companyId);
            codes.add(cover.getAreaCode());
            if(CollectionUtils.isNotEmpty(codes)){
                areaCodeSet.addAll(codes);
            }
        }
        for(Long code:areaCodeSet){
            retCodes.append(code).append(" ");
        }
        return retCodes.toString();
    }
}
