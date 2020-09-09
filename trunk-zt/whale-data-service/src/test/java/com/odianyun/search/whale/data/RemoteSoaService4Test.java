package com.odianyun.search.whale.data;


import com.odianyun.common.utils.ProjectUtils;
import com.odianyun.soa.InputDTO;
import com.odianyun.soa.client.business.SoaBasicsClient;
import com.odianyun.soa.client.business.SoaClientExecuteException;
import com.odianyun.soa.common.dto.RequestConfig;

import java.util.HashMap;
import java.util.Map;

public class RemoteSoaService4Test{

    private static String CLIENT_APPNAME = "search";

    private static Map<String, SoaBasicsClient> map = new HashMap();

    public <T> T call(RemoteSoaServiceEnum4Test remoteSoaServiceEnum, InputDTO<?> inputDTO) {
        Object result = null;
        try {
            SoaBasicsClient soaBasicsClient = getSoaBasicsClient(remoteSoaServiceEnum);
            result = soaBasicsClient.call(remoteSoaServiceEnum, inputDTO, remoteSoaServiceEnum.getSoaTypeReference());
        } catch (Throwable e) {
            throw new SoaClientExecuteException("json call error", e);
        }
        return (T) result;
    }


    public <T> T call(RemoteSoaServiceEnum4Test remoteSoaServiceEnum, InputDTO<?> inputDTO, String targetUrl) {
        Object result = null;
        try {
            SoaBasicsClient soaBasicsClient = getSoaBasicsClient(remoteSoaServiceEnum, targetUrl);
            result = soaBasicsClient.call(remoteSoaServiceEnum, inputDTO, remoteSoaServiceEnum.getSoaTypeReference());
        } catch (Throwable e) {
            throw new SoaClientExecuteException("json call error", e);
        }
        return (T) result;
    }

    private SoaBasicsClient getSoaBasicsClient(RemoteSoaServiceEnum4Test remoteSoaServiceEnum) {
        DomainEnum4Test domainEnum = remoteSoaServiceEnum.getDomainEnum();
        String key = getKey(domainEnum);
        if (map.containsKey(key)) {
            return map.get(key);
        } else {
            RequestConfig requestConfig = new RequestConfig();
            requestConfig.setDomainName(remoteSoaServiceEnum.getDomainEnum().getDomainName());
            requestConfig.setServiceAppName(remoteSoaServiceEnum.getDomainEnum().getServiceAppName());
            requestConfig.setServiceName(remoteSoaServiceEnum.getServiceName());
            try {
                String clientAppName = ProjectUtils.getProjectName();
                if (clientAppName == null || "unknown".equals(clientAppName)) {
                    clientAppName = CLIENT_APPNAME;
                }
                requestConfig.setClientAppName(clientAppName);
            } catch (Throwable e) {
                requestConfig.setClientAppName(CLIENT_APPNAME);
            }
            if (remoteSoaServiceEnum.getServiceVersion() != null) {
                requestConfig.setServiceVersion(remoteSoaServiceEnum.getServiceVersion());
            } else {
                requestConfig.setServiceVersion(domainEnum.getServiceVersion());
            }
            if (remoteSoaServiceEnum.getTimeout() != null) {
                requestConfig.setTimeout(remoteSoaServiceEnum.getTimeout());
            } else {
                requestConfig.setTimeout(domainEnum.getTimeout());
            }
            if (remoteSoaServiceEnum.getReadTimeout() != null) {
                requestConfig.setReadTimeout(remoteSoaServiceEnum.getReadTimeout());
            } else {
                requestConfig.setReadTimeout(domainEnum.getReadTimeout());
            }

            SoaBasicsClient soaBasicsClient = new SoaBasicsClient(requestConfig);
            map.put(key, soaBasicsClient);
            return soaBasicsClient;
        }
    }


    private SoaBasicsClient getSoaBasicsClient(RemoteSoaServiceEnum4Test remoteSoaServiceEnum, String targetUrl) {
        DomainEnum4Test domainEnum = remoteSoaServiceEnum.getDomainEnum();
        String key = getKey(domainEnum);
        if (map.containsKey(key)) {
            return map.get(key);
        } else {
            RequestConfig requestConfig = new RequestConfig();
            requestConfig.setDomainName(remoteSoaServiceEnum.getDomainEnum().getDomainName());
            requestConfig.setServiceAppName(remoteSoaServiceEnum.getDomainEnum().getServiceAppName());
            requestConfig.setServiceName(remoteSoaServiceEnum.getServiceName());
            try {
                String clientAppName = ProjectUtils.getProjectName();
                if (clientAppName == null || "unknown".equals(clientAppName)) {
                    clientAppName = CLIENT_APPNAME;
                }
                requestConfig.setClientAppName(clientAppName);
            } catch (Throwable e) {
                requestConfig.setClientAppName(CLIENT_APPNAME);
            }
            if (remoteSoaServiceEnum.getServiceVersion() != null) {
                requestConfig.setServiceVersion(remoteSoaServiceEnum.getServiceVersion());
            } else {
                requestConfig.setServiceVersion(domainEnum.getServiceVersion());
            }
            if (remoteSoaServiceEnum.getTimeout() != null) {
                requestConfig.setTimeout(remoteSoaServiceEnum.getTimeout());
            } else {
                requestConfig.setTimeout(domainEnum.getTimeout());
            }
            if (remoteSoaServiceEnum.getReadTimeout() != null) {
                requestConfig.setReadTimeout(remoteSoaServiceEnum.getReadTimeout());
            } else {
                requestConfig.setReadTimeout(domainEnum.getReadTimeout());
            }
            requestConfig.setTarget(targetUrl);

            SoaBasicsClient soaBasicsClient = new SoaBasicsClient(requestConfig);
            map.put(key, soaBasicsClient);
            return soaBasicsClient;
        }
    }


    private String getKey(DomainEnum4Test domainEnum) {

        return domainEnum.getDomainName() + "-" + domainEnum.getServiceAppName();
    }

}