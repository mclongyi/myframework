package com.odianyun.search.whale.recommend.server;

import com.odianyun.architecture.caddy.SystemContext;
import com.odianyun.search.whale.api.model.MerchantProduct;
import com.odianyun.search.whale.api.model.SearchResponse;
import com.odianyun.search.whale.api.model.req.SearchRequest;
import com.odianyun.search.whale.api.recommend.RecommendRequest;
import com.odianyun.search.whale.api.recommend.RecommendResponse;
import com.odianyun.search.whale.api.recommend.RecommendService;
import com.odianyun.search.whale.server.SearchByIdHandler;
import com.odianyun.search.whale.server.SearchHandler;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zengfenghua on 16/12/12.
 */
public class OrderHandler implements RecommendService{

    @Autowired
    SearchByIdHandler searchByIdHandler;

    @Autowired
    SearchHandler searchHandler;

    @Override
    public RecommendResponse recommend(RecommendRequest recommendRequest) throws Exception {
        int companyId= SystemContext.getCompanyId().intValue();
        List<Long> currentVisitMpIds=recommendRequest.getCurrentVisitMpIds();
        RecommendResponse recommendResponse=new RecommendResponse();
        if(CollectionUtils.isNotEmpty(currentVisitMpIds)){
            Map<Long,MerchantProduct> merchantProductMap=
                    searchByIdHandler.searchById(recommendRequest.getCurrentVisitMpIds(),companyId);
            MerchantProduct merchantProduct=merchantProductMap.get(currentVisitMpIds.get(0));
            if(merchantProduct!=null){
                Long brandId=merchantProduct.getBrandId();
                Long categoryId=merchantProduct.getCategoryId();
                SearchRequest searchRequest=new SearchRequest(companyId);
                searchRequest.setStart(recommendRequest.getStart());
                searchRequest.setCount(recommendRequest.getCount());
                List<Long> brandIds=new ArrayList<>();
                brandIds.add(brandId);
                searchRequest.setBrandIds(brandIds);
                List<Long> categoryIds=new ArrayList<Long>();
                searchRequest.setCategoryIds(categoryIds);
                searchRequest.setExcludeMpIds(currentVisitMpIds);
                searchRequest.setSaleAreaCode(recommendRequest.getSaleAreaCodes());
                searchRequest.setMerchantIdList(recommendRequest.getMerchantIds());
                SearchResponse searchResponse=searchHandler.handle(searchRequest);
                recommendResponse.setMerchantProducts(searchResponse.getMerchantProductResult());
            }
        }

        return recommendResponse;
    }
}
