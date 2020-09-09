package com.odianyun.search.whale.api.service;

import java.net.MalformedURLException;

import com.caucho.hessian.client.HessianProxyFactory;
import com.odianyun.search.whale.api.common.SearchException;

/**
 * MandyService 工厂类
 */
public class SearchServiceFactory {

	public SearchServiceFactory(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}

	private String serviceUrl;

	private SearchService searchService;

	public SearchService getSearchService() {
		if(searchService != null) {
			 synchronized (this) {
				if (searchService != null) {
					HessianProxyFactory factory = new HessianProxyFactory();
					try {
						searchService = (SearchService) factory.create(
								SearchService.class, serviceUrl);
					} catch (MalformedURLException e) {
						throw new SearchException(
								"failed to generate new search instance.", e);
					}
					return searchService;
				}			
			}
		}
		return searchService;
	}

}
