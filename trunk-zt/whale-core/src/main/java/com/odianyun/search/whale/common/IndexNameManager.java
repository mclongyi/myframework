package com.odianyun.search.whale.common;

import com.odianyun.search.whale.api.model.req.SearchRequest;
import com.odianyun.search.whale.common.util.ConfigUtil;
import com.odianyun.search.whale.index.api.common.IndexConstants;
import com.odianyun.search.whale.index.api.common.MerchantAreaIndexContants;
import com.odianyun.search.whale.index.api.common.OplusOIndexConstants;
import com.odianyun.search.whale.index.api.common.PointsMpIndexConstants;
import org.apache.log4j.Logger;

/**
 * Created by fishcus on 16/12/16.
 */
public class IndexNameManager {

    static Logger logger = Logger.getLogger(IndexNameManager.class);

    static {
        try {
            ConfigUtil.loadPropertiesFile("spark.properties");
        } catch (Exception e) {
            logger.warn("load properties failed", e);
        }
    }

    public static String getIndexName(){
        boolean isStartWithOplusO = ConfigUtil.getBool("isStartWithOplusO", true);
        if(isStartWithOplusO){
            return OplusOIndexConstants.index_alias;
        }
        return  IndexConstants.index_alias;
    }

    public static String getIndexName(SearchRequest searchRequest){
        if(searchRequest.getRequestType() == IndexConstants.POINTS_SEARCH){
            return PointsMpIndexConstants.index_alias;
        }
        return  getIndexName();
    }

    public static String getGeoIndexName(){
        boolean isStartWithOplusO = ConfigUtil.getBool("isStartWithOplusO", true);
        if(isStartWithOplusO){
            return OplusOIndexConstants.index_alias;
        }
        return  MerchantAreaIndexContants.index_alias;
    }
}
