package com.odianyun.search.whale.shop.req;

import com.odianyun.search.whale.api.model.ManagementType;
import com.odianyun.search.whale.api.model.req.ShopListSearchRequest;
import com.odianyun.search.whale.api.model.resp.ShopSearchResponse;
import com.odianyun.search.whale.common.RequestFieldsBuilder;
import com.odianyun.search.whale.common.query.KeywordQueryBuilder;
import com.odianyun.search.whale.es.request.ESSearchRequest;
import com.odianyun.search.whale.geo.req.GeoQueryBuilder;
import com.odianyun.search.whale.index.api.common.IndexConstants;
import com.odianyun.search.whale.index.api.common.IndexFieldConstants;
import com.odianyun.search.whale.index.api.common.MerchantAreaIndexFieldContants;
import com.odianyun.search.whale.req.builder.BaseQueryStrBuilder;
import com.odianyun.search.whale.req.builder.CombineQueryBuilder;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.query.support.QueryInnerHitBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by fishcus on 16/12/6.
 */
public class ShopListQueryBuilder implements ShopRequestBuilder{

    @Autowired
    GeoQueryBuilder geoQueryBuilder;

    @Override
    public void build(ESSearchRequest esSearchRequest, ShopListSearchRequest shopRequest, ShopSearchResponse shopResponse) throws Exception {


        if(StringUtils.isNotBlank(shopRequest.getKeyword())){
            QueryBuilder keywordQuery = KeywordQueryBuilder.buildKeywordQuery(shopRequest.getKeyword());
            if(keywordQuery != null){
                BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
                QueryInnerHitBuilder innerHitBuilder = new QueryInnerHitBuilder()
                        .setFrom(0)
                        .setSize(shopRequest.getNum())
                        .setFetchSource(false);
                List<String> mpFields = RequestFieldsBuilder.buildInnerFields();
                if(CollectionUtils.isNotEmpty(mpFields)){
//                    String[] fields=new String[mpFields.size()];
//                    innerHitBuilder.setFetchSource(mpFields.toArray(fields),null);
                    for(String mpField : mpFields){
//                        innerHitBuilder.field(mpField);
                        innerHitBuilder.addFieldDataField(mpField);
                    }
                }
                QueryBuilder childQuery = QueryBuilders.hasChildQuery(IndexConstants.index_type, boolQuery)
                        .innerHit(innerHitBuilder);
                boolQuery.must(keywordQuery);
                boolQuery.must(new TermQueryBuilder(IndexFieldConstants.MANAGEMENT_STATE, ManagementType.ON_SHELF.getCode()));
                if(BaseQueryStrBuilder.IS_COMBINE_DISPLAY){
                    boolQuery.must(CombineQueryBuilder.buildCombineQuery());
                }
                esSearchRequest.setQueryBuilder(childQuery);
            }
        }

        geoQueryBuilder.build(esSearchRequest,shopRequest);

    }
}
