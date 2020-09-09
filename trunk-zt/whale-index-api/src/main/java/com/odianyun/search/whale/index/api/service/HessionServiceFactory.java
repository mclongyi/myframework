package com.odianyun.search.whale.index.api.service;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import com.caucho.hessian.client.HessianProxyFactory;


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
				throw new Exception(
						"failed to generate new search instance.", e);
			}
			return t;
		}
		
	}


}
