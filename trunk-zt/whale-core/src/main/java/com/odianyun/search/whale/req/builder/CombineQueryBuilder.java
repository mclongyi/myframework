package com.odianyun.search.whale.req.builder;

import com.odianyun.search.whale.data.model.ProductType;
import com.odianyun.search.whale.index.api.common.IndexFieldConstants;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;

/**
 * Created by fishcus on 16/11/13.
 */
public class CombineQueryBuilder {

    public static int NORMAL = -1;

    public static int MAIN = 1;

    public static QueryBuilder buildCombineQuery(){
        return buildCombineQuery(BaseQueryStrBuilder.IS_COMBINE_DISPLAY);
    }

    public static QueryBuilder buildCombineQuery(boolean isCombine){
        BoolQueryBuilder boolFilterBuilder = new BoolQueryBuilder();
        if(isCombine){
            boolFilterBuilder.should(new TermQueryBuilder(IndexFieldConstants.TYPE_OF_PRODUCT, ProductType.NORMAL.getCode()));
            boolFilterBuilder.should(new TermQueryBuilder(IndexFieldConstants.TYPE_OF_PRODUCT, ProductType.VIRTUAL_CODE.getCode()));
            boolFilterBuilder.should(new TermQueryBuilder(IndexFieldConstants.TYPE_OF_PRODUCT, ProductType.COMBINE.getCode()));
        }else{
            boolFilterBuilder.mustNot(new TermQueryBuilder(IndexFieldConstants.TYPE_OF_PRODUCT, ProductType.VIRTUAL_CODE.getCode()));
        }

       return boolFilterBuilder;
    }

}
