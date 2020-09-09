package com.odianyun.search.whale.misc;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.odianyun.search.whale.api.common.SearchException;
import com.odianyun.search.whale.api.model.req.ShopSearchRequest;

public class ShopSearchRequestValidator {
	
	static Logger logger = Logger.getLogger(ShopSearchRequestValidator.class);

	public static boolean validate(ShopSearchRequest shopRequest) throws SearchException{
		if(CollectionUtils.isEmpty(shopRequest.getMerchantIdList())){
			logger.error("shopRequest merchantId is null or 0");
			return false;
		}
		return true;
	}

}
