package com.odianyun.search.whale.req.builder;

import com.odianyun.search.whale.api.model.selectionproduct.TypeOfProduct;
import com.odianyun.search.whale.data.model.ProductType;
import com.odianyun.search.whale.index.api.common.IndexFieldConstants;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fishcus on 16/11/13.
 */
public class TypeOfProductQueryBuilder {

    public static QueryBuilder buildTypeQuery(List<TypeOfProduct> typeList) {
        BoolQueryBuilder boolFilterBuilder = new BoolQueryBuilder();
        // 避免typeList空指针
        if(typeList == null){
            typeList = new ArrayList<>();
        }
        for(TypeOfProduct typeOfProduct : typeList){
            switch (typeOfProduct){
                case NORMAL:
                    boolFilterBuilder.should(new TermQueryBuilder(IndexFieldConstants.TYPE_OF_PRODUCT, ProductType.NORMAL.getCode()));
                    break;
                case COMBINE:
                    boolFilterBuilder.should(new TermQueryBuilder(IndexFieldConstants.TYPE_OF_PRODUCT, ProductType.COMBINE.getCode()));
                    break;
                case VIRTUAL:
                    boolFilterBuilder.should(new TermQueryBuilder(IndexFieldConstants.TYPE_OF_PRODUCT, ProductType.VIRTUAL_CODE.getCode()));
                    break;
                case SUBPRODUCT:
                    boolFilterBuilder.should(new TermQueryBuilder(IndexFieldConstants.TYPE_OF_PRODUCT, ProductType.MAIN.getCode()));
                    boolFilterBuilder.should(new TermQueryBuilder(IndexFieldConstants.TYPE_OF_PRODUCT, ProductType.SUB_CODE.getCode()));
                    break;
            }
        }

        /**
        if(typeList.contains(TypeOfProduct.VIRTUAL)){
            boolFilterBuilder.should(new TermQueryBuilder(IndexFieldConstants.TYPE_OF_PRODUCT, ProductType.VIRTUAL_CODE.getCode()));
        }else{
            boolFilterBuilder.should(new TermQueryBuilder(IndexFieldConstants.TYPE_OF_PRODUCT, ProductType.MAIN.getCode()));
            boolFilterBuilder.should(new TermQueryBuilder(IndexFieldConstants.TYPE_OF_PRODUCT, ProductType.SUB_CODE.getCode()));
        }
         */

        return boolFilterBuilder;
    }
}
