package com.odianyun.search.whale.common.remote;


import com.odianyun.common.utils.ProjectUtils;
import com.odianyun.soa.InputDTO;
import com.odianyun.soa.client.business.SoaBasicsClient;
import com.odianyun.soa.client.business.SoaClientExecuteException;
import com.odianyun.soa.common.dto.RequestConfig;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service("remoteSoaService")
public class RemoteSoaService implements RemoteSoa {

    private static String CLIENT_APPNAME = "search";

    private static Map<String, SoaBasicsClient> map = new HashMap();

    @Override
    public <T> T call(RemoteSoaServiceEnum remoteSoaServiceEnum, InputDTO<?> inputDTO) {
        Object result = null;
        try {
            SoaBasicsClient soaBasicsClient = getSoaBasicsClient(remoteSoaServiceEnum);
            result = soaBasicsClient.call(remoteSoaServiceEnum, inputDTO, remoteSoaServiceEnum.getSoaTypeReference());
        } catch (Throwable e) {
            throw new SoaClientExecuteException("json call error", e);
        }
        return (T) result;
    }


    @Override
    public <T> T call(RemoteSoaServiceEnum remoteSoaServiceEnum, InputDTO<?> inputDTO, String targetUrl) {
        Object result = null;
        try {
            SoaBasicsClient soaBasicsClient = getSoaBasicsClient(remoteSoaServiceEnum, targetUrl);
            result = soaBasicsClient.call(remoteSoaServiceEnum, inputDTO, remoteSoaServiceEnum.getSoaTypeReference());
        } catch (Throwable e) {
            throw new SoaClientExecuteException("json call error", e);
        }
        return (T) result;
    }


    @Override


    public <T> T call(RemoteSoaServiceEnum remoteSoaServiceEnum, Object object) {
        Object result = null;
        try {
            SoaBasicsClient soaBasicsClient = getSoaBasicsClient(remoteSoaServiceEnum);
            result = soaBasicsClient.call(remoteSoaServiceEnum, object, remoteSoaServiceEnum.getSoaTypeReference());
        } catch (Throwable e) {
            throw new SoaClientExecuteException("json call error", e);
        }
        return (T) result;
    }


    private SoaBasicsClient getSoaBasicsClient(RemoteSoaServiceEnum remoteSoaServiceEnum) {
        DomainEnum domainEnum = remoteSoaServiceEnum.getDomainEnum();
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


    private SoaBasicsClient getSoaBasicsClient(RemoteSoaServiceEnum remoteSoaServiceEnum, String targetUrl) {
        DomainEnum domainEnum = remoteSoaServiceEnum.getDomainEnum();
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


    private String getKey(DomainEnum domainEnum) {

        return domainEnum.getDomainName() + "-" + domainEnum.getServiceAppName();
    }

}