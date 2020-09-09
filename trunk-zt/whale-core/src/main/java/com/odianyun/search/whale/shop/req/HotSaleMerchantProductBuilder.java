package com.odianyun.search.whale.shop.req;

import com.odianyun.search.whale.api.model.ManagementType;
import com.odianyun.search.whale.api.model.Merchant;
import com.odianyun.search.whale.api.model.req.ShopListSearchRequest;
import com.odianyun.search.whale.api.model.resp.ShopSearchResponse;
import com.odianyun.search.whale.api.model.resp.ShopSearchResult;
import com.odianyun.search.whale.data.service.HotSaleMerchantProductService;
import com.odianyun.search.whale.es.request.ESSearchRequest;
import com.odianyun.search.whale.index.api.common.IndexFieldConstants;
import org.apache.commons.collections.CollectionUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fishcus on 16/11/23.
 */
@Deprecated
public class HotSaleMerchantProductBuilder implements ShopRequestBuilder{

    @Autowired
    HotSaleMerchantProductService hotSaleMerchantProductService;

    @Override
    public void build(ESSearchRequest esSearchRequest, ShopListSearchRequest shopRequest, ShopSearchResponse shopResponse) throws Exception{
        if(!shopRequest.isAdditionalHotProduct()){
            return;
        }

        List<ShopSearchResult> list = shopResponse.getShopResult();

        List<Long> mpIdList = new ArrayList<>();
        for(ShopSearchResult result : list){
            Merchant merchant = result.getMerchant();
            List<Long> mpIds = getHotSaleMerchantProducts(merchant.getId(),shopRequest.getCompanyId(),shopRequest.getNum());
            if(CollectionUtils.isNotEmpty(mpIds)){
                mpIdList.addAll(mpIds);
            }
        }

        if(CollectionUtils.isNotEmpty(mpIdList)){
            BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();

            BoolQueryBuilder mpIdsBoolQueryBuilder = new BoolQueryBuilder();
            boolQueryBuilder.must(mpIdsBoolQueryBuilder);
            for (Long mpId : mpIdList) {
                TermQueryBuilder mpIdQuery = new TermQueryBuilder(IndexFieldConstants.ID, mpId);
                mpIdsBoolQueryBuilder.should(mpIdQuery);
            }
            boolQueryBuilder.must(new TermQueryBuilder(IndexFieldConstants.MANAGEMENT_STATE, ManagementType.ON_SHELF.getCode()));
            esSearchRequest.setQueryBuilder(boolQueryBuilder);
        }


    }

    private List<Long> getHotSaleMerchantProducts(Long merchantId, Integer companyId,Integer num) throws Exception {
        List<Long> merchantIdList = new ArrayList<Long>();
        merchantIdList.add(merchantId);
        return hotSaleMerchantProductService.getHotSaleMerchantProducts(merchantIdList,companyId,0,num);
    }


}
