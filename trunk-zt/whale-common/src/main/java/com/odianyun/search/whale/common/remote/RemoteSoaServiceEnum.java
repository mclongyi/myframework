package com.odianyun.search.whale.common.remote;

import com.odianyun.search.whale.common.remote.dto.MPPromotionListOutputDTO;
import com.odianyun.search.whale.common.remote.dto.MpCommission;
import com.odianyun.search.whale.common.remote.dto.ValidateMpResultDTO;
import com.odianyun.soa.OutputDTO;
import com.odianyun.soa.client.business.SoaMethodLocatorMetadata;
import com.odianyun.soa.client.business.SoaTypeReference;

import java.util.List;

/**
 * Created by hs on 2018/8/2.
 */
public enum RemoteSoaServiceEnum implements SoaMethodLocatorMetadata {

    promotion_batchGetMPPromotions(DomainEnum.promotion, "PromotionSearchPageClient", "batchGetMPPromotions",
            new SoaTypeReference<OutputDTO<MPPromotionListOutputDTO>>() {
            }, "0.1", 6000L, 3000L, true),
    agent_commissionReadService_validateMpInCommission(DomainEnum.agent, "commissionReadService", "validateMpInCommission",
            new SoaTypeReference<OutputDTO<List<ValidateMpResultDTO>>>() {
            }, "0.1", 6000L, 3000L, true),
    agent_commissionReadService_queryOrderMpTotalMoney(DomainEnum.agent, "commissionReadService", "queryOrderMpTotalMoney",
            new SoaTypeReference<OutputDTO<List<MpCommission>>>() {
            }, "0.1", 6000L, 3000L, true)
    ;

    private DomainEnum domainEnum;

    private String serviceName;// 服务名称

    private String methodName;// 方法名

    private SoaTypeReference soaTypeReference; // 返回对象类型

    private String serviceVersion;// 版本

    private Long timeout; // timeout 必须大于 readTimeout

    private Long readTimeout;

    private Boolean isRecordLog;


    private RemoteSoaServiceEnum(DomainEnum domainEnum, String serviceName, String methodName, SoaTypeReference soaTypeReference, String serviceVersion, Long timeout, Long readTimeout, Boolean isRecordLog) {
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

    public DomainEnum getDomainEnum() {
        return this.domainEnum;
    }
}
