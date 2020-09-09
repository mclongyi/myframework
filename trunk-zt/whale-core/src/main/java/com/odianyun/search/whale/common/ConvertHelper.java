package com.odianyun.search.whale.common;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.odianyun.search.backend.model.RequestContext;
import com.odianyun.search.whale.api.model.req.BaseSearchRequest;
import com.odianyun.search.whale.api.model.req.SearchRequest;
import com.odianyun.search.whale.api.model.req.ShopSearchRequest;
import com.odianyun.search.whale.tracker.OmqSendProcessor;



public abstract class ConvertHelper {

	private final static SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd");
	private final static Logger logger = Logger.getLogger(ConvertHelper.class);

	private static String parseLongStr(List<Long> list) {
		StringBuilder ret = new StringBuilder();
		if (null != list) {
			for (Long i : list) {
				ret.append(i + ",");
			}
		}
		return ret.length() > 0 ? ret.substring(0, ret.length() - 1).toString() : "";
	}

	private static String parseIntStr(List<Integer> list) {
		StringBuilder ret = new StringBuilder();
		if (null != list) {
			for (Integer l : list) {
				ret.append(l + ",");
			}
		}
		return ret.length() > 0 ? ret.substring(0, ret.length() - 1).toString() : "";
	}
	
	private static RequestContext CreateCommonContext(BaseSearchRequest request) {
		
		RequestContext requestContext = new RequestContext();
		requestContext.setUserId(request.getUserId() == null ? "" : request.getUserId());
		
		requestContext.setPoolName(request.getClientInfo().getPoolName() == null ? "" : request.getClientInfo().getPoolName());
		requestContext.setIP(request.getClientInfo().getIP() == null ? "" : request.getClientInfo().getIP());
		requestContext.setModel(request.getClientInfo().getModel() == null ? "" : request.getClientInfo().getModel());
		requestContext.setOs(request.getClientInfo().getOs() == null ? "" : request.getClientInfo().getOs());
		requestContext.setDeviceId(request.getClientInfo().getDeviceId() == null ? "" : request.getClientInfo().getDeviceId());
		requestContext.setAppVersion(request.getClientInfo().getAppVersion() == null ? "" : request.getClientInfo().getAppVersion());
		requestContext.setBrowserVersion(request.getClientInfo().getBrowserVersion() == null ? "" : request.getClientInfo().getBrowserVersion());
		
		requestContext.setCompanyId(request.getCompanyId() == null ? 0 : request.getCompanyId());
		requestContext.setKeyword(request.getKeyword() == null ? "" : request.getKeyword());
		requestContext.setCategoryIds(parseLongStr(request.getCategoryIds()));
		requestContext.setBrandIds(parseLongStr(request.getBrandIds()));
		if(request.getRequestTime()==null){
			request.setRequestTime(new Date());
		}
		requestContext.setRequestTime(request.getRequestTime());
		requestContext.setDate(request.getRequestTime() == null ? null : timeFormat.format(request.getRequestTime()));
		requestContext.setAttrValueIds(parseLongStr(request.getAttrValueIds()));
		requestContext.setCoverProvinceIds(parseIntStr(request.getCoverProvinceIds()));
		if (null != request.getPriceRange()) {
			requestContext
					.setPriceRange(new StringBuilder().append(request.getPriceRange().getMinPrice()).append("-").append(request.getPriceRange().getMaxPrice()).toString());
		}
		requestContext.setPageNum(request.getStart());
		requestContext.setPageCount(request.getCount());
		return requestContext;
	}

	public static RequestContext ConvertToRequestContext(SearchRequest request) {

		RequestContext requestContext = CreateCommonContext(request);
		requestContext.setNavCategoryIds(parseLongStr(request.getNavCategoryIds()));
		requestContext.setRequestType(RequestType.SEARCH.ordinal());
		return requestContext;
	}
	
	
	public static RequestContext ConvertToRequestContext(ShopSearchRequest request) {
		
		RequestContext requestContext = CreateCommonContext(request);
		requestContext.setMerchantCategoryIds(parseLongStr(request.getMerchantCategoryIds()));
		requestContext.setMerchantId(request.getMerchantId() == null ? "" : request.getMerchantId().toString());
		requestContext.setRequestType(RequestType.SHOP_SEARCH.ordinal());
		return requestContext;
	}


}
