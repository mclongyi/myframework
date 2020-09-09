package com.odianyun.search.whale.data;

import com.odianyun.search.whale.api.model.BrandResult;
import com.odianyun.search.whale.api.model.SearchResponse;
import com.odianyun.search.whale.api.model.geo.GeoSearchResponse;
import com.odianyun.search.whale.api.model.o2o.O2OShopSearchResponse;
import com.odianyun.search.whale.api.model.resp.HistoryResponse;
import com.odianyun.search.whale.api.model.resp.ShopSearchResponse;
import com.odianyun.search.whale.api.model.resp.SuggestResponse;
import com.odianyun.search.whale.api.model.selectionproduct.SelectionProductSearchResponse;
import com.odianyun.soa.OutputDTO;
import com.odianyun.soa.client.business.SoaMethodLocatorMetadata;
import com.odianyun.soa.client.business.SoaTypeReference;

/**
 * Created by hs on 2018/8/2.
 */
public enum RemoteSoaServiceEnum4Test implements SoaMethodLocatorMetadata {

    SOAHistoryService_autoSearchHistoryStandard(DomainEnum4Test.search, "SOAHistoryService", "autoSearchHistoryStandard",
            new SoaTypeReference<OutputDTO<HistoryResponse>>() {
            }, "0.1", 6000L, 3000L, true),
    SOASuggestService_autoCompleteStandard(DomainEnum4Test.search, "SOASuggestService", "autoCompleteStandard",
            new SoaTypeReference<OutputDTO<SuggestResponse>>() {
            }, "0.1", 6000L, 3000L, true),
    SOASearchService_searchStandard(DomainEnum4Test.search, "SOASearchService", "searchStandard",
            new SoaTypeReference<OutputDTO<SearchResponse>>() {
            }, "0.1", 6000L, 3000L, true),
    SOASearchCacheService_reloadCacheStandard(DomainEnum4Test.search, "SOASearchCacheService", "reloadCacheStandard",
            new SoaTypeReference<Object>() {
            }, "0.1", 6000L, 3000L, true),
    SOAO2oShopSearchService_shopSearchStandard(DomainEnum4Test.search, "SOAO2oShopSearchService", "shopSearchStandard",
            new SoaTypeReference<OutputDTO<O2OShopSearchResponse>>() {
            }, "0.1", 6000L, 3000L, true),
    SOAGeoSearchService_searchStandard(DomainEnum4Test.search, "SOAGeoSearchService", "searchStandard",
            new SoaTypeReference<OutputDTO<GeoSearchResponse>>() {
            }, "0.1", 6000L, 3000L, true),
    SOASelectionProductSearchService_selectionSearchStandard(DomainEnum4Test.search, "SOASelectionProductSearchService", "selectionSearchStandard",
            new SoaTypeReference<OutputDTO<SelectionProductSearchResponse>>() {
            }, "0.1", 6000L, 3000L, true),
    SOAShopService_searchStandard(DomainEnum4Test.search, "SOAShopService", "searchStandard",
            new SoaTypeReference<OutputDTO<ShopSearchResponse>>() {
            }, "0.1", 6000L, 3000L, true),
    SOASearchBusinessService_getBrandStandard(DomainEnum4Test.search, "SOASearchBusinessService", "getBrandStandard",
            new SoaTypeReference<OutputDTO<BrandResult>>() {
            }, "0.1", 6000L, 3000L, true),
    ;

    private DomainEnum4Test domainEnum;

    private String serviceName;// 服务名称

    private String methodName;// 方法名

    private SoaTypeReference soaTypeReference; // 返回对象类型

    private String serviceVersion;// 版本

    private Long timeout; // timeout 必须大于 readTimeout

    private Long readTimeout;

    private Boolean isRecordLog;


    private RemoteSoaServiceEnum4Test(DomainEnum4Test domainEnum, String serviceName, String methodName, SoaTypeReference soaTypeReference, String serviceVersion, Long timeout, Long readTimeout, Boolean isRecordLog) {
        this.domainEnum = domainEnum;
        this.serviceName = serviceName;
        this.methodName = methodName;
        this.serviceVersion = serviceVersion;
        this.timeout = timeout;
        this.readTimeout = readTimeout;
        this.soaTypeReference = soaTypeReference;
        this.isRecordLog = isRecordLog;
    }

    public Boolean getRecordLog() {
        return this.isRecordLog;
    }

    public SoaTypeReference getSoaTypeReference() {
        return this.soaTypeReference;
    }

    @Override
    public String getServiceName() {
        return this.serviceName;
    }

    @Override
    public String getMethodName() {
        return this.methodName;
    }

    public String getServiceVersion() {
        return this.serviceVersion;
    }

    public Long getTimeout() {
        return this.timeout;
    }

    public Long getReadTimeout() {
        return this.readTimeout;
    }

    public DomainEnum4Test getDomainEnum() {
        return this.domainEnum;
    }
}
