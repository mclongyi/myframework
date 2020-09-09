package com.odianyun.search.whale.req.builder;

//
//import com.odianyun.search.backend.api.service.RelQueryServiceClient;
import com.odianyun.search.backend.api.service.RelQueryService;
import com.odianyun.search.backend.api.service.RelQueryServiceClient;
import com.odianyun.search.backend.model.Relevance;
import com.odianyun.search.whale.api.model.req.BaseSearchRequest;
import com.odianyun.search.whale.es.request.ESSearchRequest;
import com.odianyun.search.whale.index.api.common.IndexFieldConstants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.TermFilterBuilder;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.script.ScriptScoreFunctionBuilder;


import java.util.List;
import java.util.logging.Logger;

/**
 * Created by admin on 2016/6/1.
 */
public class FunctionScoreBuilder implements RequestScoreBuilder{
    private RelQueryService relQueryService;
    static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(FunctionScoreBuilder.class);



    @Override
    public void  alterScoreBySingleTokenForCate(ESSearchRequest esSearchRequest, BaseSearchRequest searchRequest) {
        QueryBuilder queryBuilder = esSearchRequest.getQueryBuilder();
        if(StringUtils.isBlank(searchRequest.getKeyword())){
            return;
        }else {
            try {
                if(searchRequest.getCompanyId()==null)return;
                int companyId = searchRequest.getCompanyId();
                List<Relevance> relevances = null;
                if(companyId==0)return;
                relevances  = relQueryService.getRelevanceListForCate(searchRequest.getKeyword().trim(),companyId);
                if(CollectionUtils.isEmpty(relevances))return;
                esSearchRequest.setQueryBuilder(this.functionScoreInit(relevances,queryBuilder));
            }catch (Exception e){
                logger.error(e.getMessage(),e);
            }
        }
    }

    private QueryBuilder functionScoreInit(List<Relevance> relevances,QueryBuilder queryBuilder){
        FunctionScoreQueryBuilder functionScoreQueryBuilder = new FunctionScoreQueryBuilder(queryBuilder);
        for(Relevance r :relevances){
            org.elasticsearch.index.query.FilterBuilder filterBuilder = new TermFilterBuilder(IndexFieldConstants.CATEGORYID,r.getRelObjectId());
            String script = "1+"+r.getRelIndex();
            ScriptScoreFunctionBuilder scriptScoreFunctionBuilder = new ScriptScoreFunctionBuilder();
            scriptScoreFunctionBuilder.script(script);
            functionScoreQueryBuilder.add(filterBuilder,scriptScoreFunctionBuilder);

        }

        functionScoreQueryBuilder.boostMode("sum");
        return functionScoreQueryBuilder;
    }


        public void setRelQueryService(RelQueryService relQueryService) {
            this.relQueryService = relQueryService;
        }
}
