package com.odianyun.search.whale.api.recommend;

import com.odianyun.search.whale.api.common.SearchException;
import com.odianyun.search.whale.api.service.SearchCacheService;
import com.odianyun.search.whale.api.service.SearchClient;
import com.odianyun.search.whale.api.service.SearchService;
import com.odianyun.search.whale.common.util.ConfigUtil;
import com.odianyun.soa.client.SoaClientFactoryBean;
import com.odianyun.soa.common.dto.ClientProfile;
import org.apache.commons.lang.StringUtils;

/**
 * Created by zengfenghua on 16/12/12.
 */
public class RecommendClient {

    static String _domainName="search";
    static String _serviceName="SOARecommendService";
    static String _serviceAppName="search";
    static String _serviceVersion="0.1";
    static Long _timeout=6000l;
    static Long _readTimeout=3000l;
    static String soa_config_file_name="search_soa.properties";
    static RecommendService _recommendService;

    static{
        ConfigUtil.loadPropertiesFile(soa_config_file_name);
        String domainName=ConfigUtil.get("recommend.soa.domainName");
        if(StringUtils.isNotEmpty(domainName)){
            _domainName=domainName;
        }
        String serviceName=ConfigUtil.get("recommend.soa.serviceName");
        if(StringUtils.isNotEmpty(serviceName)){
            _serviceName=serviceName;
        }
        String serviceAppName=ConfigUtil.get("recommend.soa.serviceAppName");
        if(StringUtils.isNotEmpty(serviceAppName)){
            _serviceAppName=serviceAppName;
        }
        String serviceVersion=ConfigUtil.get("recommend.soa.serviceVersion");
        if(StringUtils.isNotEmpty(serviceVersion)){
            _serviceVersion=serviceVersion;
        }
    }

    /**
     * @param clientName  调用方的应用名
     * @return 远程服务的代理对象
     */
    public static RecommendService getRecommendService(String clientName){
        return getRecommendService(clientName, _serviceVersion);
    }

    /**
     *
     * @param clientName  调用方的应用名
     * @param serviceVersion  服务的版本号
     * @return 远程服务的代理对象
     */
    public static RecommendService getRecommendService(String clientName,String serviceVersion){
        if(_recommendService==null){
            synchronized (RecommendClient.class) {
                if(_recommendService==null){
                    try{
                        ClientProfile clientProfile=new ClientProfile();
                        clientProfile.setDomainName(_domainName);
                        clientProfile.setServiceName(_serviceName);
                        clientProfile.setServiceVersion(serviceVersion);
                        clientProfile.setServiceAppName(_serviceAppName);
                        clientProfile.setClientAppName(clientName);
                        clientProfile.setTimeout(_timeout);
                        clientProfile.setReadTimeout(_readTimeout);
                        clientProfile.setRedoAble(true);
                        clientProfile.setClientThrottle(false);
                        SoaClientFactoryBean soaClientFactoryBean=new SoaClientFactoryBean(RecommendService.class
                                ,clientProfile);
                        _recommendService=new RecommendCacheService((RecommendService) soaClientFactoryBean.getObject(), clientName);

                    }catch(Exception e){
                        throw new SearchException("get SearchService proxy failed",e);
                    }
                }
            }
        }
        return _recommendService;
    }
}
