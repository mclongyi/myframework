package com.odianyun.search.whale.common.remote;

/**
 * Created by hs on 2018/8/2.
 */
public enum DomainEnum {


    promotion("basics-promotionDomain", "basics-promotion-service", "0.1", 2*3000L, 3000L),
    agent("agentDomain", "agent-service", "0.1", 2*3000L, 3000L)
    ;


    private String domainName;
    private String serviceAppName;
    private String serviceVersion;
    private Long timeout;
    private Long readTimeout;

    DomainEnum(String domainName, String serviceAppName, String serviceVersion, Long timeout, Long readTimeout) {
        this.domainName = domainName;
        this.serviceAppName = serviceAppName;
        this.serviceVersion = serviceVersion;
        this.timeout = timeout;
        this.readTimeout = readTimeout;
    }

    public String getDomainName() {
        return this.domainName;
    }

    public String getServiceAppName() {
        return this.serviceAppName;
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
}
