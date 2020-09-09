package com.odianyun.search.whale.api.service;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.caucho.hessian.client.HessianProxyFactory;
import com.odianyun.search.whale.api.common.SearchException;
import com.odianyun.search.whale.api.model.MerchantProduct;
import com.odianyun.search.whale.api.model.SearchResponse;
import com.odianyun.search.whale.api.model.geo.GeoDistanceSearchRequest;
import com.odianyun.search.whale.api.model.geo.GeoSearchRequest;
import com.odianyun.search.whale.api.model.geo.GeoSearchResponse;
import com.odianyun.search.whale.api.model.geo.GeoSearchService;
import com.odianyun.search.whale.api.model.geo.Point;
import com.odianyun.search.whale.api.model.o2o.O2OShopSearchRequest;
import com.odianyun.search.whale.api.model.o2o.O2OShopSearchService;
import com.odianyun.search.whale.api.model.req.SearchRequest;
import com.odianyun.search.whale.api.model.req.ShopSearchRequest;
import com.odianyun.search.whale.api.model.selectionproduct.SelectionProduct;
import com.odianyun.search.whale.api.model.selectionproduct.SelectionProductSearchRequest;
import com.odianyun.search.whale.api.model.selectionproduct.SelectionProductSearchResponse;
import com.odianyun.search.whale.api.model.selectionproduct.SelectionProductSearchService;

public class HessionServiceFactory {
	
	private static Map<String,Object> services=new HashMap<String,Object>();
	
	public static <T> T getService(String serviceUrl,Class<T> clazz) throws Exception{
		T t=(T) services.get(serviceUrl);
		if(t!=null){
			return t;
		}	
		synchronized (HessionServiceFactory.class) {
			t=(T) services.get(serviceUrl);
			if(t!=null){
				return t;
			}
			HessianProxyFactory factory = new HessianProxyFactory();
			try {
				t = (T) factory.create(clazz, serviceUrl);
				services.put(serviceUrl, t);
			} catch (MalformedURLException e) {
				throw new SearchException(
						"failed to generate new search instance.", e);
			}
			return t;
		}
		
	}


}
