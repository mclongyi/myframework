package com.odianyun.search.whale.api.service;

import com.odianyun.search.whale.api.common.SearchException;
import com.odianyun.search.whale.api.model.req.HistoryCleanRequest;
import com.odianyun.search.whale.api.model.req.HistoryReadRequest;
import com.odianyun.search.whale.api.model.resp.HistoryResponse;
import com.odianyun.soa.InputDTO;
import com.odianyun.soa.OutputDTO;

public interface HistoryService {

    /**
     * 获取搜索历史记录
     *
     * @param userId
     * @param type
     * @param count
     * @return
     * @throws SearchException
     */
    @Deprecated
    public HistoryResponse autoSearchHistory(HistoryReadRequest request) throws SearchException;

    /**
     * 清除历史记录
     *
     * @param userId
     * @param keywords
     * @param type
     * @throws SearchException
     */
    @Deprecated
    public void cleanSearchHistory(HistoryCleanRequest request) throws SearchException;


    //--------------------------------标准化改造---------------------------------

    /**
     * 获取搜索历史记录
     *
     * @throws SearchException
     */
    public OutputDTO<HistoryResponse> autoSearchHistoryStandard(InputDTO<HistoryReadRequest> inputDTO) throws SearchException;

    /**
     * 清除历史记录
     *
     * @throws SearchException
     */
    public void cleanSearchHistoryStandard(InputDTO<HistoryCleanRequest> inputDTO) throws SearchException;

}