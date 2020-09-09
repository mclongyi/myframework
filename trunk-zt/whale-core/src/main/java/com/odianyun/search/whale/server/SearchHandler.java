package com.odianyun.search.whale.server;

import com.odianyun.search.whale.api.common.SearchException;
import com.odianyun.search.whale.api.model.SearchResponse;
import com.odianyun.search.whale.api.model.req.SearchRequest;
import com.odianyun.search.whale.common.IndexNameManager;
import com.odianyun.search.whale.es.api.ESService;
import com.odianyun.search.whale.es.request.ESSearchRequest;
import com.odianyun.search.whale.index.api.common.IndexConstants;
import com.odianyun.search.whale.misc.SearchRequestValidator;
import com.odianyun.search.whale.req.builder.RequestBuilder;
import com.odianyun.search.whale.req.builder.RequestScoreBuilder;
import com.odianyun.search.whale.resp.handler.HotWordRecomendHandler;
import com.odianyun.search.whale.resp.handler.ResponseHandler;
import com.odianyun.search.whale.resp.handler.ZeroResultHandler;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.List;

public class SearchHandler {

    static Logger logger = Logger.getLogger(SearchHandler.class);

    RequestScoreBuilder requestScoreBuilder;

    List<RequestBuilder> requestBuilders;

    List<ResponseHandler<?>> responseHandlers;

    ZeroResultHandler zeroResultHandler;

    HotWordRecomendHandler hotWordRecomendHandler;

    private boolean enableZeroResult = true;
    private boolean enableHotWordRecomend = false;
    private boolean enableMergeResult = true;
    public static final int lessResultSize = 1;

//	@Autowired
//	CompanyRoutingService companyRoutingService;

    public SearchResponse handle(SearchRequest searchRequest) throws SearchException {
        SearchResponse searchResponse = new SearchResponse();

        Date startTime = new Date();
        if (!SearchRequestValidator.validate(searchRequest)) {
            return searchResponse;
        }//检查搜索请求参数合法性

        try {
            //1.智能分词,求取分词后的交集
            logger.info("1.智能分词,求取分词后的交集,k="+searchRequest.getKeyword()+",tokens="+searchRequest.getTokens());
            org.elasticsearch.action.search.SearchResponse esSearchResponse = search(searchRequest, searchResponse);

            //2.智能分词,求取分词后的并集
            if (enableMergeResult) {
                if (esSearchResponse.getHits().getTotalHits() < lessResultSize
                        && StringUtils.isNotEmpty(searchRequest.getKeyword())
                        &&  searchRequest.getTokens().size()>1) {
                    searchRequest.setIsMergeResult(true);
                    logger.info("2.智能分词,求取分词后的并集,k="+searchRequest.getKeyword()+",tokens="+searchRequest.getTokens());
                    esSearchResponse = search(searchRequest,searchResponse);
                }
            }

            //3.最大粒度分词,求取分词后的并集
            if (enableZeroResult) {
                logger.info("3.最大粒度分词,求取分词后的并集,k="+searchRequest.getKeyword()+",tokens="+searchRequest.getTokens());
                searchRequest.setIsMergeResult(true);
                zeroResultHandler.handle(esSearchResponse, searchResponse, searchRequest);
            }

            //热词推荐
            if (enableHotWordRecomend) {
                hotWordRecomendHandler.handle(esSearchResponse, searchResponse, searchRequest);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        Date endTime = new Date();
        searchResponse.costTime = endTime.getTime() - startTime.getTime();
        searchRequest.setRequestTime(startTime);
        return searchResponse;
    }

    public org.elasticsearch.action.search.SearchResponse search(SearchRequest searchRequest, SearchResponse searchResponse) throws Exception {
        searchResponse.setCompanyId(searchRequest.getCompanyId());
        ESSearchRequest esSearchRequest = new ESSearchRequest(IndexNameManager.getIndexName(searchRequest), IndexConstants.index_type);
        if (requestBuilders != null) {//construct elastic request parameter
            for (RequestBuilder builder : requestBuilders) {
                builder.build(esSearchRequest, searchRequest);
            }
        }
        org.elasticsearch.action.search.SearchResponse esSearchResponse = ESService.search(esSearchRequest);
        if (responseHandlers != null) {//处理es返回结果并转成业务结果集
            for (ResponseHandler responseHandler : responseHandlers) {
                responseHandler.handle(esSearchResponse, searchResponse,
                        esSearchRequest, searchRequest);
            }
        }
        return esSearchResponse;
    }


    public List<RequestBuilder> getRequestBuilders() {
        return requestBuilders;
    }

    public void setRequestBuilders(List<RequestBuilder> requestBuilders) {
        this.requestBuilders = requestBuilders;
    }

    public List<ResponseHandler<?>> getResponseHandlers() {
        return responseHandlers;
    }

    public void setResponseHandlers(List<ResponseHandler<?>> responseHandlers) {
        this.responseHandlers = responseHandlers;
    }

    public RequestScoreBuilder getRequestScoreBuilder() {
        return requestScoreBuilder;
    }

    public void setRequestScoreBuilder(RequestScoreBuilder requestScoreBuilder) {
        this.requestScoreBuilder = requestScoreBuilder;
    }

    public ZeroResultHandler getZeroResultHandler() {
        return zeroResultHandler;
    }

    public void setZeroResultHandler(ZeroResultHandler zeroResultHandler) {
        this.zeroResultHandler = zeroResultHandler;
    }

    public HotWordRecomendHandler getHotWordRecomendHandler() {
        return hotWordRecomendHandler;
    }

    public void setHotWordRecomendHandler(HotWordRecomendHandler hotWordRecomendHandler) {
        this.hotWordRecomendHandler = hotWordRecomendHandler;
    }

    public boolean isEnableZeroResult() {
        return enableZeroResult;
    }

    public void setEnableZeroResult(boolean enableZeroResult) {
        this.enableZeroResult = enableZeroResult;
    }

    public boolean isEnableHotWordRecomend() {
        return enableHotWordRecomend;
    }

    public void setEnableHotWordRecomend(boolean enableHotWordRecomend) {
        this.enableHotWordRecomend = enableHotWordRecomend;
    }
}
