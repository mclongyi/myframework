package com.odianyun.search.whale.server;

import com.odianyun.search.whale.api.common.SearchException;
import com.odianyun.search.whale.api.model.ManagementType;
import com.odianyun.search.whale.api.model.MerchantProduct;
import com.odianyun.search.whale.api.model.req.PromotionSearchRequest;
import com.odianyun.search.whale.api.model.resp.PromotionSearchResponse;
import com.odianyun.search.whale.common.IndexNameManager;
import com.odianyun.search.whale.common.MerchantProductConvertor;
import com.odianyun.search.whale.common.RequestFieldsBuilder;
import com.odianyun.search.whale.es.api.ESService;
import com.odianyun.search.whale.es.request.ESSearchRequest;
import com.odianyun.search.whale.index.api.common.IndexConstants;
import com.odianyun.search.whale.index.api.common.IndexFieldConstants;
import com.odianyun.search.whale.req.builder.CombineQueryBuilder;
import com.odianyun.search.whale.req.builder.SearchQueryStrBuilder;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHitField;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * Created by fishcus on 16/11/13.
 */
public class PromotionSearchHandler {

    @Autowired
    MerchantProductConvertor merchantProductConvertor;

    static Logger logger = Logger.getLogger(PromotionSearchHandler.class);

    public PromotionSearchResponse handle(PromotionSearchRequest searchRequest) throws SearchException {
        Date startTime = new Date();

        PromotionSearchResponse response = new PromotionSearchResponse();
        try {
            BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
            boolQueryBuilder.must(new TermQueryBuilder(IndexFieldConstants.COMPANYID, searchRequest.getCompanyId()));
            boolQueryBuilder.must(new TermQueryBuilder(IndexFieldConstants.BRANDID_SEARCH, String.valueOf(searchRequest.getBrandId())));

            List<Integer> promotionTypeList = searchRequest.getPromotionTypeList();
            if(CollectionUtils.isNotEmpty(promotionTypeList)){
                BoolQueryBuilder promotionTypeBuilder = new BoolQueryBuilder();
                for(Integer promotionType : promotionTypeList){
                    promotionTypeBuilder.should(new TermQueryBuilder(IndexFieldConstants.PROMOTIOM_TYPE_SEARCH, promotionType));
                }
                boolQueryBuilder.must(promotionTypeBuilder);
            }

            List<Long> promotionIdList = searchRequest.getPromotionIdList();
            if(CollectionUtils.isNotEmpty(promotionIdList)){
                BoolQueryBuilder promotionIdBuilder = new BoolQueryBuilder();
                for(Long promotionId : promotionIdList){
                    promotionIdBuilder.should(new TermQueryBuilder(IndexFieldConstants.PROMOTIOM_ID_SEARCH, promotionId));
                }
                boolQueryBuilder.must(promotionIdBuilder);
            }

            boolQueryBuilder.must(new TermQueryBuilder(IndexFieldConstants.MANAGEMENT_STATE, ManagementType.ON_SHELF.getCode()));

            boolQueryBuilder.must(CombineQueryBuilder.buildCombineQuery());

            ESSearchRequest esSearchRequest = new ESSearchRequest(IndexNameManager.getIndexName(),
                    IndexConstants.index_type);
            esSearchRequest.setQueryBuilder(boolQueryBuilder);
            esSearchRequest.setStart(searchRequest.getStart());
            esSearchRequest.setCount(searchRequest.getCount());
            RequestFieldsBuilder.build(esSearchRequest);
            esSearchRequest.getFields().add(IndexFieldConstants.PROMOTIOM_ID_SEARCH);
            esSearchRequest.getFields().add(IndexFieldConstants.PROMOTIOM_TYPE_SEARCH);

            SearchResponse searchResponse = ESService.search(esSearchRequest);
            SearchHits searchHits = searchResponse.getHits();
            response.setTotalHits(searchHits.getTotalHits());
            SearchHit[] searchHitArray = searchHits.getHits();
            for (SearchHit hit : searchHitArray) {
                Map<String, SearchHitField> data = hit.fields();
                MerchantProduct merchantProduct = merchantProductConvertor.convertFromSearchHitField(data);
                fillPromotionRelation(merchantProduct,data);
                response.getMerchantProductResult().add(merchantProduct);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        Date endTime = new Date();
        response.setCostTime(endTime.getTime() - startTime.getTime());
        return response;
    }

    private void fillPromotionRelation(MerchantProduct merchantProduct, Map<String, SearchHitField> data) {
        String promotionIdStr = (String)data.get(IndexFieldConstants.PROMOTIOM_ID_SEARCH).getValues().get(0);
        String promotionTypeStr = (String)data.get(IndexFieldConstants.PROMOTIOM_TYPE_SEARCH).getValues().get(0);
        if(StringUtils.isBlank(promotionIdStr) || StringUtils.isBlank(promotionTypeStr)){
            return;
        }
        String[] promotionIdStrs = promotionIdStr.split(" ");
        String[] promotionTypeStrs = promotionTypeStr.split(" ");
        if(promotionIdStrs == null || promotionIdStrs.length == 0
                || promotionTypeStrs == null || promotionTypeStrs.length == 0
                || promotionIdStrs.length != promotionTypeStrs.length){
            return;
        }
        Map<Integer,List<Long>> promotionRelationMap = new HashMap<Integer,List<Long>>();
        int length = promotionIdStrs.length;
        for(int i = 0; i < length; i++){
            Long promotionId = Long.valueOf(promotionIdStrs[i]);
            Integer promotionType = Integer.valueOf(promotionTypeStrs[i]);
            List<Long> promotionIdList = promotionRelationMap.get(promotionType);
            if(CollectionUtils.isEmpty(promotionIdList)){
                promotionIdList = new ArrayList<Long>();
                promotionRelationMap.put(promotionType,promotionIdList);
            }
            promotionIdList.add(promotionId);
        }
        merchantProduct.setPromotionRelationMap(promotionRelationMap);

    }
}
