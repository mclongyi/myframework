package com.odianyun.search.whale.index.business.process;

import com.odianyun.architecture.caddy.SystemContext;
import com.odianyun.search.whale.common.remote.PromotionRemoteService;
import com.odianyun.search.whale.common.remote.dto.*;
import com.odianyun.search.whale.data.model.BusinessProduct;
import com.odianyun.search.whale.data.service.MerchantProductPriceService;
import com.odianyun.search.whale.index.business.process.base.BaseProductPriceProcessor;
import com.odianyun.search.whale.index.common.ProcessorApplication;
import com.odianyun.soa.CommonInputDTO;
import org.apache.commons.collections.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import com.odianyun.basics.docking.promotion.business.read.manage.PromotionSearchPageManage;
//import com.odianyun.basics.promotion.docking.read.PromotionSearchPageDocking;
//import com.odianyun.basics.promotion.model.dto.input.MPPromotionInputDTO;
//import com.odianyun.basics.promotion.model.dto.input.MPPromotionListInputDTO;

public class ProductPriceProcessor extends BaseProductPriceProcessor {

    MerchantProductPriceService merchantProductPriceService;
//	PromotionSearchPageManage dockingManage;

    private PromotionRemoteService promotionRemoteService;

    private final int BATCH_NUM = 200;

    public ProductPriceProcessor() {
        merchantProductPriceService = (MerchantProductPriceService) ProcessorApplication.getBean("merchantProductPriceService");
//		dockingManage = PromotionSearchPageDocking.getPromotionSearchPageManage();
        promotionRemoteService = (PromotionRemoteService) ProcessorApplication.getBean("promotionRemoteService");

    }

    @Override
    public void calcProductPrice(Map<Long, BusinessProduct> map, int companyId) throws Exception {

        List<Long> merchantProductIds = new ArrayList<Long>(map.keySet());
        Map<Long, Double> priceMap = merchantProductPriceService.getMerchantProductPricesByIds(merchantProductIds, companyId);
        if (priceMap == null || priceMap.size() == 0) {
            return;
        }
        SystemContext.setCompanyId(Long.valueOf(companyId));

        List<MPPromotionInputDTO> mpPromotionList = new ArrayList();

        for (Map.Entry<Long, BusinessProduct> entry : map.entrySet()) {
            Long merchantProductId = entry.getKey();
            BusinessProduct businessProduct = entry.getValue();
            Double price = priceMap.get(merchantProductId);
            if (price == null) {
                price = 0.0;
            }
            businessProduct.setPrice(price);
            businessProduct.setOrgPrice(price);
            MPPromotionInputDTO mp = new MPPromotionInputDTO();
            mp.setMpId(merchantProductId);
            mp.setNum(1);
            mp.setPrice(new BigDecimal(price));
            mpPromotionList.add(mp);
        }
        calcProductPromotionPrice(map, mpPromotionList);

    }

    private Map<Long, MPPromotionOutputDTO> batchRequest(List<MPPromotionInputDTO> mpPromotionList) throws Exception {
        CommonInputDTO<MPPromotionListInputDTO> input = new CommonInputDTO<MPPromotionListInputDTO>();
        MPPromotionListInputDTO data = new MPPromotionListInputDTO();
        List<MPPromotionInputDTO> requestList = new ArrayList<>();
        data.setMpPromotionListInput(requestList);
        input.setData(data);
        Map<Long, MPPromotionOutputDTO> result = new HashMap<>();
        for (MPPromotionInputDTO dto : mpPromotionList) {
            requestList.add(dto);
            if (requestList.size() == BATCH_NUM) {
//				com.odianyun.basics.promotion.model.dto.output.MPPromotionListOutputDTO mpPromotionListOutputDTO = dockingManage.batchGetMPPromotionsBaseInfo(input);
//				MPPromotionListOutputDTO outputDTO = dockingManage.batchGetMPPromotionsBaseInfo(input);
                MPPromotionListOutputDTO outputDTO = promotionRemoteService.batchGetMPPromotionsBaseInfo(input);
                if (outputDTO != null && outputDTO.getMpPromotionMapOutput() != null) {
                    result.putAll(outputDTO.getMpPromotionMapOutput());
                }
                requestList.clear();
            }
        }

        if (requestList.size() > 0) {
//			com.odianyun.basics.promotion.model.dto.output.MPPromotionListOutputDTO mpPromotionListOutputDTO = dockingManage.batchGetMPPromotionsBaseInfo(input);
//			MPPromotionListOutputDTO outputDTO = dockingManage.batchGetMPPromotionsBaseInfo(input);
            MPPromotionListOutputDTO outputDTO = promotionRemoteService.batchGetMPPromotionsBaseInfo(input);
            if (outputDTO != null && outputDTO.getMpPromotionMapOutput() != null) {
                result.putAll(outputDTO.getMpPromotionMapOutput());
            }
        }

        return result;
    }

    private void calcProductPromotionPrice(Map<Long, BusinessProduct> map, List<MPPromotionInputDTO> mpPromotionList) throws Exception {

        Map<Long, MPPromotionOutputDTO> result = batchRequest(mpPromotionList);
        if (result == null || result.size() == 0) {
            return;
        }

        for (Map.Entry<Long, BusinessProduct> entry : map.entrySet()) {
            Long mpId = entry.getKey();
            BusinessProduct businessProduct = entry.getValue();

            MPPromotionOutputDTO dto = result.get(mpId);
            if (dto == null) {
                continue;
            }
            List<PromotionDTO> list = dto.getPromotionList();
            if (CollectionUtils.isEmpty(list)) {
                continue;
            }
            StringBuilder promotionIdStr = new StringBuilder();
            StringBuilder promotionTypeStr = new StringBuilder();

            for (PromotionDTO promotionDTO : list) {
                Long promotionId = promotionDTO.getPromotionId();
                Integer promotionType = promotionDTO.getFrontPromotionType();
                if (promotionId == null || promotionId == 0 || promotionType == null || promotionType == 0) {
                    continue;
                }
                promotionIdStr.append(promotionId).append(" ");
                promotionTypeStr.append(promotionType).append(" ");
            }

            businessProduct.setPromotionId_search(promotionIdStr.toString());
            businessProduct.setPromotionType_search(promotionTypeStr.toString());

            BigDecimal promPrice = dto.getPromPrice();
            if (promPrice != null) {
                businessProduct.setPrice(promPrice.doubleValue());
            }

        }

    }

}
