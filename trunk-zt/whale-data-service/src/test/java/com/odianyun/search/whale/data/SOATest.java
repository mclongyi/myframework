package com.odianyun.search.whale.data;

import com.odianyun.search.whale.api.model.BrandResult;
import com.odianyun.search.whale.api.model.SearchResponse;
import com.odianyun.search.whale.api.model.geo.GeoSearchRequest;
import com.odianyun.search.whale.api.model.geo.GeoSearchResponse;
import com.odianyun.search.whale.api.model.o2o.O2OShopSearchRequest;
import com.odianyun.search.whale.api.model.o2o.O2OShopSearchResponse;
import com.odianyun.search.whale.api.model.req.*;
import com.odianyun.search.whale.api.model.resp.HistoryResponse;
import com.odianyun.search.whale.api.model.resp.ShopSearchResponse;
import com.odianyun.search.whale.api.model.resp.SuggestResponse;
import com.odianyun.search.whale.api.model.selectionproduct.SelectionProductSearchRequest;
import com.odianyun.search.whale.api.model.selectionproduct.SelectionProductSearchResponse;
import com.odianyun.search.whale.common.remote.AgentRemoteService;
import com.odianyun.search.whale.common.remote.RemoteSoaService;
import com.odianyun.search.whale.common.remote.RemoteSoaServiceEnum;
import com.odianyun.search.whale.common.remote.dto.CommissionType;
import com.odianyun.search.whale.common.remote.dto.MpCommission;
import com.odianyun.search.whale.common.remote.dto.ValidateMpResultDTO;
import com.odianyun.soa.InputDTO;
import com.odianyun.soa.OutputDTO;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author hs
 * @date 2018/8/27.
 */
public class SOATest extends TestCase {

    public static void main(String[] args) throws Exception {
        System.setProperty("global.config.path", "/Users/hs/Desktop/lyf/env-edu");

        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext-common.xml");
        AgentRemoteService agentRemoteService = (AgentRemoteService)applicationContext.getBean("agentRemoteService");

        List<ValidateMpResultDTO> validateMpResultDTOS = agentRemoteService.validateMpInCommission(Arrays.asList(1L, 2L));
        List<MpCommission> mpCommissions = agentRemoteService.queryOrderMpTotalMoney(Arrays.asList(1095047100000116L, 1052045300001862L));
        System.out.println("=====================");
    }

    /**
     * SOA 服务都是通的
     *
     * @param args
     * @throws Exception
     */
    public static void main1(String[] args) throws Exception {
        System.setProperty("global.config.path", "/Users/hs/Desktop/lyf/env-edu");

        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext-common.xml");
        RemoteSoaService4Test remoteSoaService = new RemoteSoaService4Test();

        InputDTO<HistoryReadRequest> inputDTO1 = new InputDTO<>();
        inputDTO1.setCompanyId(30L);
        HistoryReadRequest historyReadRequest = new HistoryReadRequest();
        historyReadRequest.setUserId(10009733 + "");
        inputDTO1.setData(historyReadRequest);
        OutputDTO<HistoryResponse> outputDTO1 = remoteSoaService.call(RemoteSoaServiceEnum4Test.SOAHistoryService_autoSearchHistoryStandard, inputDTO1);
        System.out.println("===============end  SOAHistoryService_autoSearchHistoryStandard==================");

        InputDTO<SuggestRequest> inputDTO2 = new InputDTO<>();
        OutputDTO<SuggestResponse> outputDTO2 = remoteSoaService.call(RemoteSoaServiceEnum4Test.SOASuggestService_autoCompleteStandard, inputDTO2);
        System.out.println("===============end  SOASuggestService_autoCompleteStandard==================");

        InputDTO<SearchRequest> inputDTO3 = new InputDTO<>();
        OutputDTO<SearchResponse> outputDTO3 = remoteSoaService.call(RemoteSoaServiceEnum4Test.SOASearchService_searchStandard, inputDTO3);
        System.out.println("===============end  SOASearchService_searchStandard==================");

        InputDTO<ReloadCacheRequest> inputDTO4 = new InputDTO<>();
        remoteSoaService.call(RemoteSoaServiceEnum4Test.SOASearchCacheService_reloadCacheStandard, inputDTO4);
        System.out.println("===============end  SOASearchCacheService_reloadCacheStandard==================");

        InputDTO<O2OShopSearchRequest> inputDTO5 = new InputDTO<>();
        O2OShopSearchRequest request = new O2OShopSearchRequest();
        inputDTO5.setData(request);
        OutputDTO<O2OShopSearchResponse> outputDTO5 = remoteSoaService.call(RemoteSoaServiceEnum4Test.SOAO2oShopSearchService_shopSearchStandard, inputDTO5);
        System.out.println("===============end  SOAO2oShopSearchService_shopSearchStandard==================");

        InputDTO<GeoSearchRequest> inputDTO6 = new InputDTO<>();
        GeoSearchRequest request1 = new GeoSearchRequest();
        inputDTO6.setData(request1);
        OutputDTO<GeoSearchResponse> outputDTO6 = remoteSoaService.call(RemoteSoaServiceEnum4Test.SOAGeoSearchService_searchStandard, inputDTO6);
        System.out.println("===============end  SOAGeoSearchService_searchStandard==================");

        InputDTO<O2OShopSearchRequest> inputDTO7 = new InputDTO<>();
        O2OShopSearchRequest request2 = new O2OShopSearchRequest();
        inputDTO7.setData(request2);
        OutputDTO<O2OShopSearchResponse> outputDTO7 = remoteSoaService.call(RemoteSoaServiceEnum4Test.SOAO2oShopSearchService_shopSearchStandard, inputDTO7);
        System.out.println("===============end  SOAO2oShopSearchService_shopSearchStandard==================");

        InputDTO<SelectionProductSearchRequest> inputDTO8 = new InputDTO<>();
        SelectionProductSearchRequest request8 = new SelectionProductSearchRequest();
        inputDTO8.setData(request8);
        OutputDTO<SelectionProductSearchResponse> outputDTO8 = remoteSoaService.call(RemoteSoaServiceEnum4Test.SOASelectionProductSearchService_selectionSearchStandard, inputDTO8);
        System.out.println("===============end  SOASelectionProductSearchService_selectionSearchStandard==================");

        InputDTO<ShopListSearchRequest> inputDTO9 = new InputDTO<>();
        ShopListSearchRequest request9 = new ShopListSearchRequest();
        inputDTO9.setData(request9);
        OutputDTO<ShopSearchResponse> outputDTO9 = remoteSoaService.call(RemoteSoaServiceEnum4Test.SOAShopService_searchStandard, inputDTO9);
        System.out.println("===============end  SOAShopService_searchStandard==================");

        InputDTO<BrandSearchRequest> inputDTO10 = new InputDTO<>();
        OutputDTO<BrandResult> outputDTO10 = remoteSoaService.call(RemoteSoaServiceEnum4Test.SOASearchBusinessService_getBrandStandard, inputDTO10);
        System.out.println("===============end  SOASearchBusinessService_getBrandStandard==================");


    }
}
