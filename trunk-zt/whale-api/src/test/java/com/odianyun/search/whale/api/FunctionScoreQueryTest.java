package com.odianyun.search.whale.api;

import com.odianyun.search.whale.api.model.MerchantProduct;
import com.odianyun.search.whale.api.model.SearchResponse;
import com.odianyun.search.whale.api.model.req.SearchRequest;
import com.odianyun.search.whale.api.model.req.SortType;
import com.odianyun.search.whale.api.service.HessionServiceFactory;
import com.odianyun.search.whale.api.service.SearchService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/6/1.
 */
public class FunctionScoreQueryTest extends AbstractTest{
    public static void main(String[] args)  throws Exception{
        String serviceUrl="http://192.168.20.58:8080/search/soa/SOASearchService";
        String serviceUrl1="http://172.16.2.31:8080/whale-core/soa/SOASearchService";
//        String serviceUrl="http://192.168.20.13:8080/search/soa/SOASearchService";
        SearchService searchService= HessionServiceFactory.getService(serviceUrl1, SearchService.class);
        SearchRequest searchRequest=new SearchRequest(1);
        searchRequest.setKeyword("苹果");
        searchRequest.setCompanyId(1);
        List<SortType> sortTypes=new ArrayList<SortType>();
//        sortTypes.add(SortType.volume4sale_desc);
//        sortTypes.add(SortType.create_time_desc);
        searchRequest.setSortTypeList(sortTypes);
        SearchResponse searchResponse=searchService.search(searchRequest);
        for(MerchantProduct m:searchResponse.getMerchantProductResult()){
            System.out.println("  categoryId  "+m.getCategoryId()+"  categoryName  "+m.getCategoryName() +"  productId  "+m.getProductId()+"  productName  "+m.getProductName()+"  companyId  "+m.getCompanyId());
        }
    }


}
